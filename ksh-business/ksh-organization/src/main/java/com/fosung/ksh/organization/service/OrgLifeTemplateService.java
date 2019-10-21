package com.fosung.ksh.organization.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.organization.entity.OrgLifeTemplate;

public interface OrgLifeTemplateService extends AppBaseDataService<OrgLifeTemplate,Long> {

    /**
     * 组织生活模板可用个数
     *
     * @param orgId
     * @return
     */
    public Integer countNum(String orgId);
}
