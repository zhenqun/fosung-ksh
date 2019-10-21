package com.fosung.ksh.oauth2.server.controller;

import com.fosung.framework.common.secure.auth.AppUserDetailsDefault;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.sys.entity.SysUser;
import com.fosung.ksh.sys.handler.SysUserHandlerAdaptor;
import com.fosung.ksh.sys.service.SysUserRoleService;
import com.fosung.ksh.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangyh
 */
@Api(description = "系统用户接口", tags = {"2、系统用户接口"})
@RestController
@RequestMapping(value = SysUserController.BASE_PATH)
public class SysUserController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/user";


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserHandlerAdaptor sysUserHandlerAdaptor;

    @Autowired
    private SysUserRoleService sysUserRoleService;


    @ApiOperation(value = "通过获取当前登录用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public ResponseEntity<SysUser> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal == null || !(principal instanceof AppUserDetailsDefault)) {
            return null;
        }
        AppUserDetailsDefault appUserDetailsDefault = (AppUserDetailsDefault) principal;
        SysUser sysUser = sysUserHandlerAdaptor.getUserByHash(appUserDetailsDefault.getHash());
        sysUser.setUserSource(appUserDetailsDefault.getUserSource());
        return Result.success(sysUser);
    }


    @ApiOperation(value = "获取当前登录用户信息，在token失效时，该接口会刷新token")
    @RequestMapping(value = "/principal", method = RequestMethod.POST)
    public ResponseEntity<SysUser> principal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal == null || !(principal instanceof AppUserDetailsDefault)) {
            return null;
        }
        AppUserDetailsDefault appUserDetailsDefault = (AppUserDetailsDefault) principal;
        SysUser sysUser = sysUserHandlerAdaptor.getUserByHash(appUserDetailsDefault.getHash());
        sysUser.setUserSource(appUserDetailsDefault.getUserSource());
        return Result.success(sysUser);
    }


}
