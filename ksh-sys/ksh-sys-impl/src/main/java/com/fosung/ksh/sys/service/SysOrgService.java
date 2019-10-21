package com.fosung.ksh.sys.service;

import com.fosung.ksh.sys.dto.SysOrg;

import java.util.List;
import java.util.Map;

public interface SysOrgService {

    /**
     * 根据组织机构id查询详情
     *
     * @param orgId 组织机构id
     * @return
     */
    SysOrg getOrgInfo(String orgId);

    /**
     * 根据组织机构Id获取下级所有组织
     *
     * @param orgId 组织机构id
     * @return
     */
    List<SysOrg> queryOrgList(String orgId);

    /**
     * 获取下级所有党支部
     * @param orgId
     * @return
     */
    public List<SysOrg> queryOrgBranchInfo(String orgId);

    /**
     * 根据父级ID获取所有直接、简介简项库组织数据,不包含根节点
     * 如果存在子集并且不是当前组织ID，则继续递归去获取数据
     */
    List<SysOrg> queryOrgAllChild(String rootOrgId);

    /**
     * 根据父级ID获取所有直接、简介简项库组织数据,不包含根节点
     * 如果存在子集并且不是当前组织ID，则继续递归去获取数据
     */
    List<Map<String, Object>> queryOrgAllChild(String rootOrgId, String orgName);
}
