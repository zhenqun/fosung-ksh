package com.fosung.ksh.duty.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.ksh.duty.entity.DutySchedule;
import com.fosung.ksh.duty.vo.DutyScheduleVo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DutyScheduleDao extends AppJPABaseDao<DutySchedule, Long> {


}
