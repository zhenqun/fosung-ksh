package com.fosung.ksh.monitor.task;

import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.task.AppTaskCluster;
import com.fosung.ksh.common.util.UtilBean;
import com.fosung.ksh.duty.config.DutyProperties;
import com.fosung.ksh.monitor.client.CameraVideoClient;
import com.fosung.ksh.monitor.client.MonitorClient;
import com.fosung.ksh.monitor.dto.MonitorInfo;
import com.fosung.ksh.monitor.entity.MonitorCamera;
import com.fosung.ksh.monitor.entity.constant.MonitorStatus;
import com.fosung.ksh.monitor.service.MonitorCameraService;
import com.fosung.ksh.sys.client.SysAreaClient;
import com.fosung.ksh.sys.dto.SysArea;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangyihua
 * @date 2019-05-09 19:27
 */
@Slf4j
@Component
public class MonitorCameraTask {

    /**
     * 同步周期为90秒
     */
    private static final Long SYNC_CYCLE = 1000 * 90L;

    @Autowired
    private DutyProperties dutyProperties;

    @Resource
    private AppTaskCluster appTaskCluster;

    @Autowired
    private MonitorClient monitorClient;


    @Autowired
    private SysAreaClient areaClient;

    @Autowired
    private MonitorCameraService monitorCameraService;

    @Autowired
    private CameraVideoClient cameraVideoClient;

    private static final String VIDEO_TYPE ="HLS";
    /**
     * 定时执行实时视频流存入至缓存当中
     */
    @Scheduled(cron = "0 0 6,12,22 * * ?")
    public void syncVideoReal(){
        if (appTaskCluster.needRunTask()) {
            List<MonitorCamera> monitorCameras = monitorCameraService.queryAll(Maps.newHashMap());
            monitorCameras.forEach(monitorCamera->{
                cameraVideoClient.getCameraVideoRealTime(monitorCamera.getIndexCode(), VIDEO_TYPE,"REFRESH");
            });
        }
    }
    /**
     * 定时同步
     */
    @Scheduled(fixedDelay = 1000L * 60)
    public void syncRecord()
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (appTaskCluster.needRunTask()) {
            Date syncTime = new Date();
            List<MonitorInfo> remoteList = monitorClient.queryMonitorList();
            List<MonitorCamera> dbList = monitorCameraService.queryAll(Maps.newHashMap());

            List<MonitorInfo> addList = Lists.newArrayList();
            List<MonitorCamera> updateList = Lists.newArrayList();
            List<MonitorCamera> deleteList = Lists.newArrayList();


            // 新增的设备
            addList = remoteList.stream().filter(monitorInfo ->
                    UtilString.isNotBlank(monitorInfo.getIndexCode())
                            && getMonitorCamera(dbList, monitorInfo.getIndexCode()) == null).collect(Collectors.toList());


            // 删除的设备
            deleteList = dbList.stream().filter(monitorCamera ->
                    UtilString.isNotBlank(monitorCamera.getIndexCode())
                            && getMonitorInfo(remoteList, monitorCamera.getIndexCode()) == null).collect(Collectors.toList());


            // 修改的设备，修改在线状态
            for (MonitorCamera monitorCamera : dbList) {
                MonitorInfo monitorInfo = getMonitorInfo(remoteList, monitorCamera.getIndexCode());
                if (monitorInfo != null) {
                    monitorCamera.setMonitorStatus("1".equals(monitorInfo.getIsOnline()) ? MonitorStatus.ONLINE : MonitorStatus.OFFLINE);
                    updateList.add(monitorCamera);
                }
            }

            // 新增设备入库
            add(addList, syncTime);

            // 修改设备状态数据
            update(updateList, syncTime);

            // 批量设置未知设备状态
            delete(deleteList);


            log.debug("\n设备同步成功，共同步：\n新增{}条设备，\n修改{}条设备,\n未知{}条设备", addList.size(), updateList.size(), deleteList.size());

        }
    }


    /**
     * 新增数据
     *
     * @param monitorInfoList
     */
    private void add(List<MonitorInfo> monitorInfoList, Date syncTime) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        for (MonitorInfo monitorInfo : monitorInfoList) {
            String parentCode = monitorInfo.getParentIndexCode();
            SysArea areaInfo = areaClient.getAreaInfo(parentCode);

            MonitorCamera monitorCamera = new MonitorCamera();
            UtilBean.copyProperties(monitorCamera, monitorInfo);
            monitorCamera.setMonitorStatus("1".equals(monitorInfo.getIsOnline()) ? MonitorStatus.ONLINE : MonitorStatus.OFFLINE);
            // 如果自己的行政区域表没有进行维护，或者海康后台数据维护错误，则不进行入库
            if (areaInfo == null) {
                log.error("设备的行政编码，没有进行维护，无法入库,设备信息{}", JsonMapper.toJSONString(monitorInfo,true));
            } else {
                monitorCamera.setVillageId(areaInfo.getAreaId());
                monitorCamera.setSyncTime(syncTime);
                monitorCameraService.save(monitorCamera);
            }

        }
    }


    /**
     * 批量同步设备状态
     *
     * @param list
     * @param syncTime
     */
    private void update(List<MonitorCamera> list, Date syncTime) {
        Set<String> updateFields = Sets.newHashSet("syncTime", "monitorStatus");
        for (MonitorCamera monitorCamera : list) {
            monitorCamera.setSyncTime(syncTime);
        }
        monitorCameraService.update(list, updateFields);
    }

    /**
     * 批量删除设备
     *
     * @param list
     */
    private void delete(List<MonitorCamera> list) {
        Set<String> updateFields = Sets.newHashSet("monitorStatus");
        for (MonitorCamera monitorCamera : list) {
            monitorCamera.setMonitorStatus(MonitorStatus.UNKNOWN);
        }
        monitorCameraService.update(list, updateFields);
    }


    private MonitorCamera getMonitorCamera(List<MonitorCamera> list, String indexCode) {
        for (MonitorCamera monitorCamera : list) {
            if (indexCode.equals(monitorCamera.getIndexCode())) {
                return monitorCamera;
            }
        }
        return null;
    }

    private MonitorInfo getMonitorInfo(List<MonitorInfo> list, String indexCode) {
        for (MonitorInfo monitorCamera : list) {
            if (indexCode.equals(monitorCamera.getIndexCode())) {
                return monitorCamera;
            }
        }
        return null;
    }

//    private Boolean hasCityCode(String cityCode) {
//        if (UtilString.isEmpty(cityCode)) {
//            return false;
//        }
//        for (String code : dutyProperties.getCityCodes()) {
//            if (cityCode.startsWith(code)) {
//                return true;
//            }
//        }
//        return false;
//
//    }
}
