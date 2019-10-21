package com.fosung.ksh.punsh.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.framework.dao.jpa.annotation.MybatisQuery;
import com.fosung.ksh.punsh.entity.AdministrativeDivision;
import com.fosung.ksh.punsh.vo.Administration;


/**
 * @author lqyu
 * @date 2019-4-4 10:08
 */
public interface AdministrationDao extends AppJPABaseDao<AdministrativeDivision, Long> {

    @MybatisQuery
    Administration queryAdminList(String cityCode);
}
