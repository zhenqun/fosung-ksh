package com.fosung.ksh.duty.service;

import com.fosung.ksh.duty.vo.DataCenterRowVo;
import com.fosung.ksh.duty.vo.DataCenterSingleVo;
import com.fosung.ksh.duty.vo.DataCenterVo;

import java.lang.reflect.InvocationTargetException;

/**
 * @program: fosung-ksh
 * @description: 大数据中心服务
 * @author: LZ
 * @create: 2019-05-20 10:35
 **/

public interface DataCenterService {
    /**
     * 获取无感考勤设备点的信息
     * @param cityCode
     * @return
     */
    DataCenterSingleVo getAttendanceData(String cityCode, String type) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;
    /**
     * 获取下一级的无感考勤数据
     * @param cityCode
     * @return
     */
    DataCenterRowVo getChildrenAttendanceData(String cityCode);

    /**
     * 获取镇下面所有村的实时视频
     * @param cityCode
     * @return
     */
    DataCenterRowVo getTownVideo(String cityCode);

    // 获取县级下面镇级的签到情况（各个镇月度 出勤率）
    DataCenterRowVo getTownSignRecode(String cityCode) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;
}
