package com.fosung.ksh.duty.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.duty.entity.DutySpecialDay;

import java.util.Date;
import java.util.List;

public interface DutySpecialDayService extends AppBaseDataService<DutySpecialDay, Long> {
    /**
     * 获取特殊值班日期
     *
     * @param shiftId
     * @param specialDate
     * @return
     */
    public DutySpecialDay getDayInfo(Long shiftId, Date specialDate);

    /**
     * 批量保存特殊日期
     *
     * @param specialDayList
     * @param shiftId
     */
    public void saveList(List<DutySpecialDay> specialDayList, Long shiftId);
}
