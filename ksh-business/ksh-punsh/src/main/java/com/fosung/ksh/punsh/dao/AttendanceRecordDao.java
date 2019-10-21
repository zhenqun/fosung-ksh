package com.fosung.ksh.punsh.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.framework.dao.jpa.annotation.MybatisQuery;

import com.fosung.ksh.punsh.entity.AttendanceRecord;
import com.fosung.ksh.punsh.vo.PunchRecord;
import com.fosung.ksh.punsh.vo.QueryRecordConDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lqyu
 * @date 2019-4-1 10:03
 */
public interface AttendanceRecordDao extends AppJPABaseDao<AttendanceRecord, Long> {


    @MybatisQuery
    List<PunchRecord> queryPunchRecord(QueryRecordConDto queryRecordConDto);

    @MybatisQuery
    List<AttendanceRecord> queryAttend(@Param("dateStart") String dateStart, @Param("dateEnd") String dateEnd, @Param("pin") String pin);

    @MybatisQuery
    List<PunchRecord> queryAttendRecord(QueryRecordConDto queryRecordConDto);
}
