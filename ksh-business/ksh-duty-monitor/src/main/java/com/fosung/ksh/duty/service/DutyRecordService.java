package com.fosung.ksh.duty.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.duty.entity.DutyRecord;

import java.util.Date;

public interface DutyRecordService extends AppBaseDataService<DutyRecord,Long> {
    /**
     * 根据身份证号获取人员数据
     * @param idCard
     * @return
     */
    public DutyRecord getByAlarmId(String alarmId,String humanId);


    public Date queryMaxTime();
}
