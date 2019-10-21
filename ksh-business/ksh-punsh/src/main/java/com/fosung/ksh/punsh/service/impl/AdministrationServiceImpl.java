package com.fosung.ksh.punsh.service.impl;

import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.punsh.dao.AdministrationDao;
import com.fosung.ksh.punsh.entity.AdministrativeDivision;
import com.fosung.ksh.punsh.service.AdministrationService;
import com.fosung.ksh.punsh.vo.Administration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lqyu
 * @date 2019-4-4 10:10
 */
@Service
public class AdministrationServiceImpl extends AppJPABaseDataServiceImpl<AdministrativeDivision, AdministrationDao>
        implements AdministrationService {


    @Override
    public Map<String, String> getQueryExpressions() {
        return null;
    }

    @Override
    public Administration queryAdminList(String cityCode) {
        if (StringUtils.isBlank(cityCode)){
            return new Administration();
        }
        return this.entityDao.queryAdminList(cityCode);
    }


}
