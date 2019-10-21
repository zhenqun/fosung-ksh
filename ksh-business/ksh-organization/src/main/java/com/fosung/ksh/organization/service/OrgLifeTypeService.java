package com.fosung.ksh.organization.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.organization.entity.OrgLifeType;

import java.util.List;

public interface OrgLifeTypeService extends AppBaseDataService<OrgLifeType, Long> {
    /**
     * 批量保存组织生活类型
     *
     * @param orgLifeId
     * @param list
     */
    public void save(Long orgLifeId, List<OrgLifeType> list);
}
