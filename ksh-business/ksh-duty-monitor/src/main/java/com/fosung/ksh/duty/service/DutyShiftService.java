package com.fosung.ksh.duty.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.duty.entity.DutyShift;

import java.util.Date;

public interface DutyShiftService extends AppBaseDataService<DutyShift, Long> {


    /**
     * 根据areaId获取班次设置
     *
     * @param areaId
     * @return
     */
    public DutyShift getShiftByAreaId(Long areaId);

    /**
     * 验证行政区域在当前时间是否需要值班
     *
     * @param areaId 行政区划ID
     * @param date   时间
     * @return
     */
    public Boolean onDuty(Long areaId, Date date);


    /**
     * 修改值班设定
     *
     * @param dutyShift
     */
    public void edit(DutyShift dutyShift);


    /**
     * 获取班次详情
     *
     * @param areaid
     * @return
     */
    public DutyShift getShiftInfo(Long areaid);
}
