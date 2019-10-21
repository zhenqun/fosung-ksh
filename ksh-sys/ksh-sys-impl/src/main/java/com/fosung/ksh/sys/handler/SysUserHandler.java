package com.fosung.ksh.sys.handler;

import com.fosung.ksh.sys.dto.SysOrg;
import com.fosung.ksh.sys.entity.SysRole;
import com.fosung.ksh.sys.entity.SysUser;
import com.fosung.ksh.sys.service.SysOrgService;
import com.fosung.ksh.sys.service.SysUserRoleService;
import com.fosung.ksh.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Order(0)
public class SysUserHandler implements UserHandler {


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysOrgService sysOrgService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 通过hash获取用户详细信息
     *
     * @param hash
     * @return
     */
    public SysUser getUserByHash(String hash) {
        SysUser sysUser = sysUserService.getSysUserByHash(hash);
        if (sysUser == null) {
            return null;
        }

        SysOrg sysOrg = sysOrgService.getOrgInfo(sysUser.getOrgId());
        List<SysRole> roles = sysUserRoleService.queryRoleList(sysUser.getHash());
        sysUser.setOrgCode(sysOrg.getOrgCode());
        sysUser.setOrgName(sysOrg.getOrgName());
        sysUser.setRoles(roles);
        sysUser.setPassword(null);
        return sysUser;
    }

    /**
     * 通过hash获取用户减项信息
     *
     * @param hash
     * @return
     */
    @Override
    public SysUser getSimpleUserByHash(String hash) {
        SysUser sysUser = sysUserService.getSysUserByHash(hash);
        if (sysUser == null) {
            return null;
        }

        SysOrg sysOrg = sysOrgService.getOrgInfo(sysUser.getOrgId());
        sysUser.setOrgCode(sysOrg.getOrgCode());
        sysUser.setOrgName(sysOrg.getOrgName());
        sysUser.setPassword(null);
        return sysUser;
    }

}
