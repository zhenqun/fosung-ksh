package com.fosung.ksh.sys.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.sys.entity.SysRole;
import com.fosung.ksh.sys.entity.SysUser;
import com.fosung.ksh.sys.entity.SysUserRole;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysUserRoleService extends AppBaseDataService<SysUserRole, Long> {
    /**
     * 及联获取role
     *
     * @param userHash
     * @return
     */
    public List<SysRole> queryRoleList(String userHash);

    /**
     * 通过角色ID获取用户
     *
     * @param roleId
     * @return
     */
    List<SysUser> queryUsersByRoleId(Long roleId);

    void rebind(Long roleId, Set<String> userHashs);

    List<SysUserRole> queryFullAll(Map<String, Object> param);

    void updateManage(List<SysUserRole> sysUserRoles);
}
