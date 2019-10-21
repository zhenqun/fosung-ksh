package com.fosung.ksh.duty.controller;


import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.constant.DateTrunc;
import com.fosung.ksh.duty.dto.DutyArea;
import com.fosung.ksh.duty.service.DataCenterService;
import com.fosung.ksh.duty.service.DutyVillageRecordService;
import com.fosung.ksh.duty.vo.DataCenterRowVo;
import com.fosung.ksh.duty.vo.DataCenterSingleVo;
import com.fosung.ksh.sys.client.SysAreaClient;
import com.fosung.ksh.sys.dto.SysArea;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;


/**
 * 大数据中接口
 */
@Slf4j
@RestController
@RequestMapping(value = DataCenterController.BASE_PATH)
public class DataCenterController extends AppIBaseController {
    public static final String BASE_PATH = "/datacentor";

    @Autowired
    private DataCenterService dataCenterService;

    /**
     * 获取签到的相关服务
     */
    @Autowired
    private DutyVillageRecordService dutyVillageRecordService;

    @Autowired
    private SysAreaClient sysAreaClient;

    /**
     * 大数据中心获取镇级别的无感考勤的相关数据
     */
    @RequestMapping("/{getParamType}/{cityCode}")
    public DataCenterSingleVo getAttendanceData(@PathVariable("getParamType") String getParamType,
                                                @PathVariable("cityCode") String cityCode) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return dataCenterService.getAttendanceData(cityCode, getParamType);
    }

    /**
     * 获取下一级的无感考勤数据
     *
     * @param cityCode
     * @return
     */
    @RequestMapping("/getChildrenAttendance/{cityCode}")
    public DataCenterRowVo getChildrenAttendanceData(@PathVariable("cityCode") String cityCode) {
        return dataCenterService.getChildrenAttendanceData(cityCode);
    }


    /**
     * 大数据中心获取镇级别的无感考勤的相关数据
     */
    @RequestMapping("/today/{getParamType}/{cityCode}")
    public DataCenterSingleVo getTodayAttendanceData(@PathVariable("getParamType") String getParamType,
                                                     @PathVariable("cityCode") String cityCode) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        DataCenterSingleVo dataCenterSingleVo = new DataCenterSingleVo();

        SysArea sysArea = sysAreaClient.getAreaInfo(cityCode);
        // 获取县级的签到情况 （今日已出勤，今日应出勤，今日出勤率）
        DutyArea areaCamera = (DutyArea) dutyVillageRecordService.countSelfRate(DateTrunc.DAY, sysArea.getAreaId(), new Date());
        switch (getParamType) {
            case "attendanceNumber": //今日出勤
                dataCenterSingleVo.setItemValue(areaCamera.getSignNumber().toString());
                break;
            case "shouldAttendanceNumber":  //今日应出勤
                dataCenterSingleVo.setItemValue((areaCamera.getSignNumber() + areaCamera.getNotSignNumber()) + "");
                break;
            case "attendanceRate":   //出勤率
                dataCenterSingleVo.setItemValue(areaCamera.getSignRate());

        }
        return dataCenterSingleVo;
    }

    /**
     * 获取镇下面各个村的实时视频
     */

    @RequestMapping("/getTownVideo/{cityCode}")
    public DataCenterRowVo getTownVideo(@PathVariable("cityCode") String cityCode) {
        return dataCenterService.getTownVideo(cityCode);
    }

    /**
     * 获取镇下面各个村签到率
     */
    @RequestMapping("/getTownSignRecode/{cityCode}")
    public DataCenterRowVo getTownSignRecode(@PathVariable("cityCode") String cityCode) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return dataCenterService.getTownSignRecode(cityCode);
    }

}
