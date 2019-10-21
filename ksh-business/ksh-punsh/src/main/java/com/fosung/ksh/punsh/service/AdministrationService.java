package com.fosung.ksh.punsh.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.punsh.entity.AdministrativeDivision;
import com.fosung.ksh.punsh.vo.Administration;


/**
 * @author lqyu
 * @date 2019-4-4 10:10
 */
public interface AdministrationService extends AppBaseDataService<AdministrativeDivision,Long> {


    /**
     * 查询行政机构
     * @param cityCode
     * @return
     */
    Administration queryAdminList(String cityCode);
}
