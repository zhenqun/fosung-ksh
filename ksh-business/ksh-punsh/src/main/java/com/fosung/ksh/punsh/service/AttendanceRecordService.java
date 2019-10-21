package com.fosung.ksh.punsh.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.punsh.entity.AttendanceRecord;
import com.fosung.ksh.punsh.vo.PunchRecord;
import com.fosung.ksh.punsh.vo.QueryRecordConDto;


import java.text.ParseException;
import java.util.List;

/**
 * @date 2019-4-1 10:04
 */
public interface AttendanceRecordService extends AppBaseDataService<AttendanceRecord, Long> {

    public List<PunchRecord> queryPunchRecord(int pagenum, int pagesize, QueryRecordConDto queryRecordConDto) throws ParseException;

    List<AttendanceRecord> queryAttend(String dateStart, String dateEnd, String pin);

    List<PunchRecord> queryAttendRecord(String cityName, String status, String userName, String dateTime, String orgCode, String flag);
}
