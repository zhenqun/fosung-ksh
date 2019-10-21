package com.fosung.ksh.sys.service;


import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.sys.entity.SysPermission;

/**
 * 权限管理
 */
public interface SysPermissionService extends AppBaseDataService<SysPermission, Long> {

    SysPermission saveAndRole(SysPermission sysPermission);

    void updateAndRole(SysPermission sysPermission);
}
