package com.fosung.ksh.organization.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fosung.ksh.organization.dao.OrgLifePeopleDao;
import com.fosung.ksh.organization.entity.OrgLifePeople;
import com.fosung.ksh.organization.entity.OrgLifeType;
import com.fosung.ksh.organization.service.OrgLifePeopleService;
import org.assertj.core.util.Sets;
import org.springframework.stereotype.Service;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrgLifePeopleServiceImpl extends AppJPABaseDataServiceImpl<OrgLifePeople, OrgLifePeopleDao>
        implements OrgLifePeopleService {

    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("orgLifeId", "orgLifeId:EQ");
            put("branchId", "branchId:EQ");
            put("orgLifeIdIN", "orgLifeId:IN");
            put("personnelType","personnelType:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }

    /**
     * 批量保存数据
     *
     * @param orgLifeId
     * @param list
     */
    @Override
    @Transactional
    public void save(Long orgLifeId, List<OrgLifePeople> list) {
        if (list != null) {
            this.entityDao.deleteByOrgLifeId(orgLifeId);
            for (OrgLifePeople orgLifePeople : list) {
                OrgLifePeople data = this.entityDao.get(orgLifeId, orgLifePeople.getPersonnelHash());
                if (data == null) {
                    orgLifePeople.setId(null);
                    orgLifePeople.setOrgLifeId(orgLifeId);
                    save(orgLifePeople);
                } else {
                    data.setDel(false);
                    update(data, Sets.newLinkedHashSet("del"));
                }
            }
        }
    }
}
