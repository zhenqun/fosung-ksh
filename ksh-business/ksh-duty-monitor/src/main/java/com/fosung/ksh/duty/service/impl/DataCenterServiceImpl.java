package com.fosung.ksh.duty.service.impl;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.ksh.common.constant.DateTrunc;
import com.fosung.ksh.duty.dto.DutyArea;
import com.fosung.ksh.duty.service.DataCenterService;
import com.fosung.ksh.duty.service.DutyVillageRecordService;
import com.fosung.ksh.duty.vo.DataCenterRowItemPropertieVo;
import com.fosung.ksh.duty.vo.DataCenterRowItemVo;
import com.fosung.ksh.duty.vo.DataCenterRowVo;
import com.fosung.ksh.duty.vo.DataCenterSingleVo;
import com.fosung.ksh.monitor.client.CameraVideoClient;
import com.fosung.ksh.monitor.entity.MonitorCamera;
import com.fosung.ksh.monitor.service.MonitorCameraService;
import com.fosung.ksh.sys.client.SysAreaClient;
import com.fosung.ksh.sys.dto.SysArea;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: fosung-ksh
 * @description: 大数据中心获取服务的实现类
 * @author: LZ
 * @create: 2019-05-20 10:37
 **/
@Service
public class DataCenterServiceImpl implements DataCenterService {
    /**
     * 获取实时视频流相关
     */
    @Autowired
    private CameraVideoClient cameraVideoClient;
    /**
     * 设备相关服务
     */
    @Autowired
    private MonitorCameraService cameraService;


    @Autowired
    private SysAreaClient sysAreaClient;
    /**
     * 获取签到的相关服务
     */
    @Autowired
    private DutyVillageRecordService dutyVillageRecordService;

    @Override
    public DataCenterSingleVo getAttendanceData(String cityCode, String paramType) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        DataCenterSingleVo dataCenterSingleVo = new DataCenterSingleVo();
        switch (paramType) {
            case "attendancePointsNumber": //考勤点数量
                DutyArea camera = getCamera(cityCode);
                dataCenterSingleVo.setItemValue((camera.getOnlineNumber() + camera.getOfflineNumber()) + "");
                break;
            case "runPercentage":    //运行白分比
                DutyArea townDutyArea = getCamera(cityCode);
                dataCenterSingleVo.setItemValue(townDutyArea.getOnlineRate());
                break;
            case "attendanceDays":    //累计考勤天数
                dataCenterSingleVo.setItemValue(getAllRecordDateNumber(cityCode).toString());
                break;
            case "attendanceRateMonth":   //本月出勤率
                try {
                    dataCenterSingleVo.setItemValue(getAttendanceRateMonth(cityCode));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
        }
        return dataCenterSingleVo;
    }

    /**
     * 获取下一级的无感考勤数据
     *
     * @param cityCode
     * @return
     */
    @Override
    public DataCenterRowVo getChildrenAttendanceData(String cityCode) {
        DataCenterRowVo dataCenterRowVo = new DataCenterRowVo();
        // 获取下一级组织机构
        ArrayList<DataCenterRowItemVo> dataCenterRowItemVos = Lists.newArrayList();

        SysArea area = sysAreaClient.getAreaInfo(cityCode);

        // 获取县级下面的镇级
        List<DutyArea> areaCameras = cameraService.queryChildrenRateList(area.getAreaId());
        areaCameras.forEach(DutyArea -> {
            DataCenterRowItemVo dataCenterRowItemVo = new DataCenterRowItemVo();
            dataCenterRowItemVo.setItemName(DutyArea.getAreaName());
            dataCenterRowItemVo.setItemValue(DutyArea.getAreaCode());

            // 数据结构是一个镇下挂有4个数据
            List<DataCenterRowItemPropertieVo> dataCenterRowItemPropertieVos = Lists.newArrayList();

            // 考勤点数量
            DutyArea camera = getCamera(DutyArea.getAreaCode());

            dataCenterRowItemPropertieVos.add(0, DataCenterRowItemPropertieVo.builder().itemName("考勤点总数").itemValue((camera.getOnlineNumber() + camera.getOfflineNumber()) + "").num(0).build());
            // 运行白分比
            dataCenterRowItemPropertieVos.add(1, DataCenterRowItemPropertieVo.builder().itemName("设备正常运行率").itemValue(camera.getOnlineRate()).num(1).build());
            // 累计考勤天数
            dataCenterRowItemPropertieVos.add(2, DataCenterRowItemPropertieVo.builder().itemName("累计考勤天数").itemValue(getAllRecordDateNumber(DutyArea.getAreaCode()).toString()).num(2).build());
            // 本月出勤率
            try {
                dataCenterRowItemPropertieVos.add(3, DataCenterRowItemPropertieVo.builder().itemName("平均出勤率").itemValue(getAttendanceRateMonth(DutyArea.getAreaCode())).num(3).build());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            dataCenterRowItemVo.setProperties(dataCenterRowItemPropertieVos);

            dataCenterRowItemVos.add(dataCenterRowItemVo);
        });

        dataCenterRowVo.setData(dataCenterRowItemVos);

        return dataCenterRowVo;
    }


    // 获取县级设备情况  （当前考勤点数，设备运行率）
    public DutyArea getCamera(String cityCode) {
        SysArea area = sysAreaClient.getAreaInfo(cityCode);
        Assert.notNull(area, "错误的areaCode" + cityCode);
        DutyArea areaCamera = (DutyArea) cameraService.countSelfRate(area.getAreaId());
        return areaCamera;
    }

    // 获取累计考勤天数
    public Integer getAllRecordDateNumber(String cityCode) {
        List<String> allRecordDate = dutyVillageRecordService.getAllRecordDate();
        return UtilCollection.isEmpty(allRecordDate) ? 0 : allRecordDate.size();
    }

    // 获取县本月出勤率
    public String getAttendanceRateMonth(String cityCode) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        SysArea sysArea = sysAreaClient.getAreaInfo(cityCode);
        Assert.notNull(sysArea, "错误的areaCode" + cityCode);
        DutyArea townDutyArea = (DutyArea) dutyVillageRecordService.countSelfRate(DateTrunc.MONTH, sysArea.getAreaId(), new Date());
        return townDutyArea.getSignRate();
    }

    // 获取县级下面的镇级设备情况和实时视频流
//    @RedisCacheable(timeUnit = TimeUnit.HOURS,expired = 8)
    @Override
    public DataCenterRowVo getTownVideo(String cityCode) {
        DataCenterRowVo dataCenterRowVo = new DataCenterRowVo();
        List<DataCenterRowItemVo> dataCenterRowItemVos = new ArrayList<>();
        SysArea area = sysAreaClient.getAreaInfo(cityCode);
        Assert.notNull(area, "错误的areaCode" + cityCode);
        // 获取县级下面的镇级设备情况 （获取所有县级，然后将视频流查询出来，存入）
        List<DutyArea> areaCameras = cameraService.queryChildrenRateList(area.getAreaId());
        areaCameras.forEach(DutyArea -> {
            DataCenterRowItemVo dataCenterRowItemVo = new DataCenterRowItemVo();
            dataCenterRowItemVo.setItemName(DutyArea.getAreaName());

            List<DataCenterRowItemPropertieVo> dataCenterRowItemPropertieVos = Lists.newArrayList();
            // 根据镇获取镇下面所有的村
            List<SysArea> sysAreas = cameraService.queryVillageHasCameraList(DutyArea.getAreaId());

            // 根据村获取视频流
            sysAreas.forEach(SysArea -> {

                // 获取实时视频流
                MonitorCamera monitorCamera = cameraService.getByVillageId(SysArea.getAreaId());
                if (null != monitorCamera) {
                    String hls = cameraVideoClient.getCameraVideoRealTime(monitorCamera.getIndexCode(), "HLS", "GET");
                    dataCenterRowItemPropertieVos.add(DataCenterRowItemPropertieVo.builder().itemValue(hls).itemName(SysArea.getAreaName()).build());
                }


            });

            // 设置村下面村的数量
            dataCenterRowItemVo.setItemValue(sysAreas.size() + "");

            dataCenterRowItemVo.setProperties(dataCenterRowItemPropertieVos);
            dataCenterRowItemVos.add(dataCenterRowItemVo);
        });
        dataCenterRowVo.setData(dataCenterRowItemVos);
        return dataCenterRowVo;
    }

    // 获取县级下面镇级的签到情况（各个镇月度 出勤率）
    @Override
    public DataCenterRowVo getTownSignRecode(String cityCode) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        DataCenterRowVo dataCenterRowVo = new DataCenterRowVo();
        List<DataCenterRowItemVo> dataCenterRowItemVos = new ArrayList<>();

        SysArea sysArea = sysAreaClient.getAreaInfo(cityCode);
        Assert.notNull(sysArea, "错误的areaCode" + cityCode);
        // 获取县级下面镇级的签到情况（各个镇月度 出勤率）
        List<DutyArea> areaCameras = dutyVillageRecordService.queryChildrenRateList(DateTrunc.MONTH, sysArea.getAreaId(), new Date());
        areaCameras.forEach(DutyArea -> {
            DataCenterRowItemVo dataCenterRowItemVo = new DataCenterRowItemVo();
            dataCenterRowItemVo.setItemName(DutyArea.getAreaName());
            dataCenterRowItemVo.setItemValue(((DutyArea) DutyArea).getSignRate());
            dataCenterRowItemVos.add(dataCenterRowItemVo);
        });
        dataCenterRowVo.setData(dataCenterRowItemVos);
        return dataCenterRowVo;
    }
}
