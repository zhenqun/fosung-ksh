package com.fosung.ksh.oauth2.server.support;

import com.fosung.framework.common.secure.auth.AppUserDetails;
import com.fosung.framework.common.secure.auth.AppUserDetailsDefault;
import com.fosung.framework.common.secure.auth.AppUserDetailsServiceAdaptor;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.ksh.sys.entity.SysPermission;
import com.fosung.ksh.sys.entity.SysRole;
import com.fosung.ksh.sys.entity.SysUser;
import com.fosung.ksh.sys.service.SysUserRoleService;
import com.fosung.ksh.sys.service.SysUserService;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


/**
 * 用户加载服务
 *
 * @Author : liupeng
 * @Date : 2018-10-16
 * @Modified By
 */
@Slf4j
@Service
public class AppSysUserDetailsService extends AppUserDetailsServiceAdaptor {

    @Autowired
    @Lazy
    protected SysUserService sysUserService;


    @Autowired
    @Lazy
    protected SysUserRoleService sysUserRoleService;

    public AppUserDetails getAppUserDetails() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null) {
//            log.error("获取认证后的 authentication=null");
//            return null;
//        } else if (authentication.getPrincipal() == null) {
//            log.error("获取认证后的 authentication.getPrincipal()==null");
//            return null;
//        } else if (!(authentication.getPrincipal() instanceof AppUserDetails)) {
//            log.debug("获取认证后的 {}.getPrincipal() = {}", authentication.getClass().getSimpleName(), JsonMapper.toJSONString(authentication.getPrincipal()));
//            return null;
//        } else {
//            return (AppUserDetails) authentication.getPrincipal();
//        }
        return null;
    }

    /**
     * 从数据库加载用户信息
     *
     * @param appUserDetailsDefault
     */
    @Override
    public void loadUserProperties(AppUserDetailsDefault appUserDetailsDefault) {
        // 根据用户名获取用户
        SysUser sysUser = sysUserService.getSysUserByName(appUserDetailsDefault.getUsername());

        if (sysUser == null) {
            log.warn("没有找到登录用户: {}", appUserDetailsDefault.getUsername());
            return;
        }

        // 设置认证用户详情，用户id、密码 和 真实姓名
        appUserDetailsDefault.setUserId(sysUser.getId());
        appUserDetailsDefault.setHash(sysUser.getHash());
        appUserDetailsDefault.setPassword(sysUser.getPassword());
        appUserDetailsDefault.setUserRealName(sysUser.getRealName());
    }


    /**
     * 加载用户角色信息
     *
     * @param appUserDetailsDefault
     * @param userRoles
     */
    @Override
    public void loadUserRoles(AppUserDetailsDefault appUserDetailsDefault, Set<String> userRoles) {
        super.loadUserRoles(appUserDetailsDefault, userRoles);

        List<SysRole> roleList = sysUserRoleService.queryRoleList(appUserDetailsDefault.getHash());

        Set<String> sysUserRoles = Sets.newHashSet();

        for (SysRole sysRole : roleList) {
            List<SysPermission> permissionList = sysRole.getPermissions();
            if (UtilCollection.isNotEmpty(permissionList)) {
                for (SysPermission sysPermission : permissionList) {
                    sysUserRoles.add(sysPermission.getPermissionName());
                }
            }
        }

        // 加载角色信息
        if (sysUserRoles != null) {
            userRoles.addAll(sysUserRoles);
        }
    }


}
