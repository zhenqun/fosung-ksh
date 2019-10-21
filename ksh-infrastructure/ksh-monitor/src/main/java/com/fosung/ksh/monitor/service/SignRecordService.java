package com.fosung.ksh.monitor.service;

import com.fosung.ksh.monitor.dto.PersonRecordInfo;

import java.util.List;

public interface SignRecordService {
    /**
     * 查询人员签到记录，对应海康的黑名单预警信息
     *
     * @param libIds
     * @param startAlarmTime
     * @param endAlarmTime
     * @return
     */
    List<PersonRecordInfo> getBlackList(String libIds, String startAlarmTime, String endAlarmTime, String indexCode);
}
