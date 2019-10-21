package com.fosung.ksh.organization.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.organization.entity.OrgLifePeople;

import java.util.List;

public interface OrgLifePeopleService extends AppBaseDataService<OrgLifePeople,Long> {
    /**
     * 批量保存组织生活类型
     * @param orgLifeId
     * @param list
     */
    public void save(Long orgLifeId, List<OrgLifePeople> list);
}
