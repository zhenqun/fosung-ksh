package com.fosung.ksh.duty.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.duty.entity.DutyWorkDay;

import java.util.Date;
import java.util.List;

public interface DutyWorkDayService extends AppBaseDataService<DutyWorkDay, Long> {
//    /**
//     * 为乡镇添加默认的工作设置
//     *
//     * @param cityCode
//     * @return
//     */
//    List<DutyWorkDay> setDutyWorkDayList(String cityCode);

    /**
     * 获取今日是否为值班时间
     *
     * @param shiftId
     * @param date
     * @return
     */
    public boolean onDutyWorkDay(Long shiftId, Date date);

    /**
     * 批量保存特殊日期
     *
     * @param shiftId
     */
    public void saveList(List<DutyWorkDay> workDayList, Long shiftId);
}
