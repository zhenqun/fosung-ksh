package com.fosung.ksh.sys.service;


import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.sys.entity.SysRole;

import java.util.List;

/**
 * 角色管理
 */
public interface SysRoleService extends AppBaseDataService<SysRole, Long> {

    public List<SysRole> queryByClientIdAndRoleName(String clientId, String rolename);
    /**
     * 查询sysRole，并进行及联
     *
     * @param id
     * @return
     */
    public SysRole getUnionPermission(Long id);

    void deleteCheck(List<Long> ids) throws Exception;
}
