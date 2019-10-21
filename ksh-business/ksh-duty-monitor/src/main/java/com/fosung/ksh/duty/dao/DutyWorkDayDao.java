package com.fosung.ksh.duty.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.ksh.duty.entity.DutyWorkDay;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DutyWorkDayDao extends AppJPABaseDao<DutyWorkDay, Long> {
    @Query("delete  from DutyWorkDay  where shiftId = ?1")
    @Modifying
    void deleteAllByShiftId(Long shiftId);
}
