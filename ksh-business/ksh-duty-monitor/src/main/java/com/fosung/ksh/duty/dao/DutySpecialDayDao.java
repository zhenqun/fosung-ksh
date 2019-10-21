package com.fosung.ksh.duty.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.framework.dao.jpa.annotation.MybatisQuery;
import com.fosung.ksh.duty.entity.DutySpecialDay;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface DutySpecialDayDao extends AppJPABaseDao<DutySpecialDay, Long> {


    @Query("delete  from DutySpecialDay  where shiftId = ?1")
    @Modifying
    void deleteAllByShiftId(Long shiftId);

    /**
     * 获取特殊值班日期
     *
     * @param shiftId
     * @param specialDate
     * @return
     */
    @MybatisQuery
    DutySpecialDay getDayInfo(@Param("shiftId") Long shiftId, @Param("specialDate") Date specialDate);
}
