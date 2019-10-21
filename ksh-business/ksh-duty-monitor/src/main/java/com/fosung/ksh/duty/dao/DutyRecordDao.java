package com.fosung.ksh.duty.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.ksh.duty.entity.DutyRecord;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface DutyRecordDao extends        AppJPABaseDao<DutyRecord, Long> {

    @Query("select max(dutySignTime) from DutyRecord ")
    public Date queryMaxTime();
}
