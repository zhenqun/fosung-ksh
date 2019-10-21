package com.fosung.ksh.organization.service.impl;

import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.organization.dao.OrgLifeTemplateDao;
import com.fosung.ksh.organization.entity.OrgLifeTemplate;
import com.fosung.ksh.organization.service.OrgLifeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wangyh
 */
@Service
public class OrgLifeTemplateServiceImpl extends AppJPABaseDataServiceImpl<OrgLifeTemplate, OrgLifeTemplateDao>
        implements OrgLifeTemplateService {
    @Autowired
    private OrgLifeTemplateDao orgLifeTemplateDao;

    /**
     * 可启用模版个数
     */
    private static final Integer CAN_PUSH_NUM = 5;

    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("title", "title:LIKE");
            put("orgId", "orgId:EQ");
            put("orgIds", "orgId:IN");
            put("isPush", "isPush:EQ");
            put("templateType", "templateType:EQ");

        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }


    /**
     * 组织生活模板可用个数
     *
     * @param orgId
     * @return
     */
    @Override
    public Integer countNum(String orgId) {

        //统计已发布的数据
       Integer count = this.entityDao.countPush(orgId);
        return CAN_PUSH_NUM - count;
    }
}
