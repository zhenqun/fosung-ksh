package com.fosung.ksh.organization.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.organization.entity.OrgLifeBranch;

import java.util.List;

public interface OrgLifeBranchService extends AppBaseDataService<OrgLifeBranch,Long> {
    /**
     * 批量保存组织生活类型
     * @param orgLifeId
     * @param list
     */
    public void save(Long orgLifeId, List<OrgLifeBranch> list);
}
