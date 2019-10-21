package com.fosung.ksh.sys.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.sys.entity.SysPermission;
import com.fosung.ksh.sys.entity.SysRole;
import com.fosung.ksh.sys.entity.SysRolePermission;

import java.util.List;

public interface SysRolePermissionService extends AppBaseDataService<SysRolePermission, Long> {
    public List<SysPermission> queryPermissionList(Long roleId);


    public List<SysRole> queryRoleList(Long permissionId);

    /**
     * @param roleId
     * @param permissionId
     * @return
     */
    public SysRolePermission get(Long roleId, Long permissionId);
}
