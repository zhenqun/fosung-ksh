package com.fosung.ksh.monitor.service.impl;

import com.fosung.framework.common.util.UtilBean;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilReflection;
import com.fosung.framework.dao.config.mybatis.page.MybatisPageRequest;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.duty.dto.DutyArea;
import com.fosung.ksh.monitor.dao.MonitorCameraDao;
import com.fosung.ksh.monitor.entity.MonitorCamera;
import com.fosung.ksh.monitor.entity.constant.MonitorStatus;
import com.fosung.ksh.monitor.service.MonitorCameraService;
import com.fosung.ksh.sys.client.SysAreaClient;
import com.fosung.ksh.sys.dto.SysArea;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * todo 该类设备在线率需进行优化，需要考虑一个村有多个设备的情况
 */
@Slf4j
@Service
public class MonitorCameraServiceImpl extends AppJPABaseDataServiceImpl<MonitorCamera, MonitorCameraDao>
        implements MonitorCameraService {

    @Autowired
    private SysAreaClient areaClient;
    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("villageId", "villageId:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }


    /**
     * 镇级及以上统计自身设备在线率，离线率
     *
     * @return
     */
    public DutyArea countSelfRate(Long areaId) {
        List<Long> areaIdList = getVillageIdList(areaId);
        List<MonitorCamera> monitorCameraList = this.entityDao.queryList(areaIdList);

        DutyArea dutyArea = new DutyArea();
        SysArea sysArea = areaClient.getAreaInfo(areaId);
        UtilBean.copyProperties(sysArea,dutyArea);

        Long onlineNumber = monitorCameraList.stream()
                .filter(monitorCamera -> monitorCamera.getMonitorStatus() == MonitorStatus.ONLINE).count();
        Long offLineNumber = monitorCameraList.stream()
                .filter(monitorCamera -> monitorCamera.getMonitorStatus() == MonitorStatus.OFFLINE).count();
        dutyArea.setOnlineNumber(onlineNumber.intValue());
        dutyArea.setOfflineNumber(offLineNumber.intValue());
        return dutyArea;
    }


    /**
     * 分页查询设备状态信息
     *
     * @param areaId
     * @param monitorStatus
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<MonitorCamera> queryPageList(Long areaId, String monitorStatus, int pageNum, int pageSize) {
        List<SysArea> areas = areaClient.queryBranchList(areaId);
        List<Long> areaIdList = areas.stream().map(SysArea::getAreaId).collect(Collectors.toList());
        Map<Long, SysArea> areaMap = areas.stream().collect(Collectors.toMap(SysArea::getAreaId, sysArea -> sysArea));
        Page<MonitorCamera> page = this.entityDao.queryPageList(areaIdList, monitorStatus, MybatisPageRequest.of(pageNum, pageSize));
        page.getContent().stream().forEach(monitorCamera -> {
            SysArea sysArea = areaMap.get(monitorCamera.getVillageId());
            if (sysArea != null) {
                monitorCamera.setVillageCode(sysArea.getAreaCode());
                monitorCamera.setVillageName(sysArea.getAreaName());
                sysArea = areaClient.getAreaInfo(sysArea.getParentId());
                monitorCamera.setTownId(sysArea.getAreaId());
                monitorCamera.setTownCode(sysArea.getAreaCode());
                monitorCamera.setTownName(sysArea.getAreaName());

            }
        });

        return page;
    }


    /**
     * 镇级及以上统计下级设备在线率，离线率，设备在线数量，离线数量
     *
     * @return
     */
    public List<DutyArea> queryChildrenRateList(Long id) {
        // 获取下级刑侦机构id
        List<SysArea> areaList = areaClient.queryAreaList(id, "");

        List<DutyArea> areaCameraList = Lists.newArrayList();

        areaList.stream().forEach(sysArea -> {
            DutyArea areaCamera = countSelfRate(sysArea.getAreaId());
            areaCameraList.add(areaCamera);
        });
        return areaCameraList;
    }


    /**
     * 根据设备code获取设备详情
     *
     * @param villageId
     * @return
     */
    public MonitorCamera getByVillageId(Long villageId) {
        Map<String, Object> searchParam = Maps.newHashMap("villageId", villageId);
        List<MonitorCamera> list = queryAll(searchParam);
        return UtilCollection.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public void preSaveHandler(MonitorCamera entity) {
        List<MonitorCamera> monitorCameras = queryAll(Maps.newHashMap("villageId", entity.getVillageId()));
        if (UtilCollection.isNotEmpty(monitorCameras)){
            log.error("{}挂载多个设备，行政id：{}重复，将删除之前",entity.getName(),entity.getCameraId());
            List<Long> collect = monitorCameras.stream().map(MonitorCamera::getId).collect(Collectors.toList());
            delete(collect);
        }
        super.preSaveHandler(entity);
    }

    public List<MonitorCamera> queryByVillageId(Long villageId ) {
        List<MonitorCamera> monitorCameras = queryAll(Maps.newHashMap("villageId", villageId));
        return monitorCameras;
    }
    /**
     * 获取有监控设备的村庄列表,并默认缓存10分钟
     * 防止重复查询数据库，优化查询速度
     *
     * @return
     */
//    @RedisCacheable(expired = 10, timeUnit = TimeUnit.MINUTES)
    public List<SysArea> queryVillageHasCameraList(Long areaId) {
        List<SysArea> sysAreaList = areaClient.queryBranchList(areaId);
        List<Long> villageIdList = sysAreaList.stream().map(SysArea::getAreaId).collect(Collectors.toList());
        List<MonitorCamera> monitorCameraList = this.entityDao.queryList(villageIdList);
        Map<Long, MonitorCamera> monitorMap = monitorCameraList.stream().collect(Collectors.toMap(MonitorCamera::getVillageId, monitor -> monitor));

        sysAreaList = sysAreaList.stream().filter(sysArea -> monitorMap.containsKey(sysArea.getAreaId())).collect(Collectors.toList());

        return sysAreaList;
    }


    /**
     * 获取所有村级的行政区域ID
     *
     * @param areaId
     * @return
     */
    private List<Long> getVillageIdList(Long areaId) {
        List<SysArea> areas = areaClient.queryBranchList(areaId);
        List<Long> idList = areas.stream().map(SysArea::getAreaId).collect(Collectors.toList());
        return idList;
    }

}
