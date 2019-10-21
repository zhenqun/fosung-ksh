package com.fosung.ksh.organization.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.ksh.organization.entity.OrgLifeTemplate;
import org.springframework.data.jpa.repository.Query;

public interface OrgLifeTemplateDao extends AppJPABaseDao<OrgLifeTemplate, Long> {

    /**
     * 统计已经发布的模版个数
     * @param orgId
     * @return
     */
    @Query(value = "select count(id) from OrgLifeTemplate where templateType = 'BRANCH' and isPush = true  and orgId = ?1")
    public Integer countPush(String orgId);
}