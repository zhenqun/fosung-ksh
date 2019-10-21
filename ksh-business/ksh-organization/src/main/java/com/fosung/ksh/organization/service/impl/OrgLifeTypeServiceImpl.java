package com.fosung.ksh.organization.service.impl;

import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.organization.dao.OrgLifeTypeDao;
import com.fosung.ksh.organization.entity.OrgLifeType;
import com.fosung.ksh.organization.service.OrgLifeTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrgLifeTypeServiceImpl extends AppJPABaseDataServiceImpl<OrgLifeType, OrgLifeTypeDao>
        implements OrgLifeTypeService {

    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("orgLifeId", "orgLifeId:EQ");
            put("classificationId", "classificationId:EQ");
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
    public void save(Long orgLifeId, List<OrgLifeType> list) {
        if (list != null) {
            this.entityDao.deleteByOrgLifeId(orgLifeId);
            for (OrgLifeType orgLifeType : list) {
                orgLifeType.setId(null);
                orgLifeType.setOrgLifeId(orgLifeId);
            }
            saveBatch(list);
        }
    }

}
