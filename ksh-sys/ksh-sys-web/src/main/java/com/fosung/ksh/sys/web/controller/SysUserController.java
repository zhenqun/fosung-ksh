package com.fosung.ksh.sys.web.controller;

import com.fosung.framework.common.secure.auth.AppUserDetailsDefault;
import com.fosung.framework.common.secure.auth.constant.UserSource;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.common.util.DownloadFileUtil;
import com.fosung.ksh.common.util.UtilBean;
import com.fosung.ksh.common.util.UtilEasyPoi;
import com.fosung.ksh.sys.dto.ImportUser;
import com.fosung.ksh.sys.dto.SysOrg;
import com.fosung.ksh.sys.entity.SysDtUser;
import com.fosung.ksh.sys.entity.SysRole;
import com.fosung.ksh.sys.entity.SysUser;
import com.fosung.ksh.sys.handler.SysUserHandlerAdaptor;
import com.fosung.ksh.sys.service.SysDtUserService;
import com.fosung.ksh.sys.service.SysOrgService;
import com.fosung.ksh.sys.service.SysUserRoleService;
import com.fosung.ksh.sys.service.SysUserService;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ResponseHeader;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    private SysDtUserService sysDtUserService;

    @Autowired
    private SysOrgService orgService;

    @ApiOperation(value = "通过获取当前登录用户信息")
    @RequestMapping(value = "/import-template")
    public ResponseEntity importTemplate() throws IOException {
        String fileName = UUID.randomUUID() + ".xls";// 文件名
        ResponseEntity<InputStreamResource> response = null;
        try {
            response = DownloadFileUtil.download("", "import-user-templet.xlsx", "导入模板");
        } catch (Exception e) {
        }
        return response;
    }

    @ApiOperation(value = "通过获取当前登录用户信息")
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public ResponseEntity<Map> importUser(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = Maps.newHashMap();
        List<ImportUser> importSysUserList = UtilEasyPoi.importExcel(file, 0, 1, ImportUser.class)
                .stream()
                .filter(item -> item != null && !Strings.isNullOrEmpty(item.getUsername()) && !Strings.isNullOrEmpty(item.getPassword()))
                .collect(Collectors.toList());

        List<ImportUser> errorList = Lists.newArrayList();
        List<ImportUser> successList = Lists.newArrayList();

        for (ImportUser importUser : importSysUserList) {
            try {
                sysUserService.saveImportUser(importUser);
                successList.add(importUser);
            } catch (Exception e) {
                e.printStackTrace();
                errorList.add(importUser);
            }
        }
        result.put("errorList", errorList);
        result.put("successList", successList);

        return Result.success(result);
    }

    @ApiOperation(value = "通过角色ID获取当前用户列表")
    @RequestMapping(value = "/by-role-id", method = RequestMethod.POST)
    public ResponseEntity<List<SysUser>> usersByRoleIds(@RequestParam Long roleId) {
        return Result.success(sysUserRoleService.queryUsersByRoleId(roleId));
    }


    @ApiOperation(value = "通过获取当前登录用户信息")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<SysUser> save(@RequestBody SysUser sysUser) {
        if (sysUser.getId() == null) {
            if (Strings.isNullOrEmpty(sysUser.getPassword())) {
                sysUser.setPassword("dtlzzhdj");
            }
            sysUser = sysUserService.registerUser(sysUser);
        } else {
            sysUserService.updateAndHst(sysUser);
        }
        return Result.success(sysUser);
    }

    @ApiOperation(value = "通过获取当前登录用户信息")
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ResponseEntity<SysUser> resetPassword(@RequestBody SysUser sysUser,
                                                 @RequestParam(value = "plainPassword", defaultValue = "dtlzzhdj") String plainPassword) {
        sysUser.setPassword(plainPassword);
        sysUserService.resetPassword(sysUser);
        return Result.success();
    }

    @ApiOperation(value = "通过获取当前登录用户信息")
    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ResponseEntity changePassword(@RequestParam String newPlainPassword,
                                         @RequestParam String plainPasswordConfirm) {

        if (!newPlainPassword.equals(plainPasswordConfirm)) {
            return ResponseEntity.badRequest().body("密码输入不一致");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserDetailsDefault appUserDetailsDefault = (AppUserDetailsDefault) authentication.getPrincipal();

        if (appUserDetailsDefault.getUserSource() != UserSource.LOCAL) {
            return ResponseEntity.badRequest().body("目前只支持修改本地用户密码！");
        }
        sysUserService.changePassword(sysUserHandlerAdaptor.getUserByHash(appUserDetailsDefault.getHash()), newPlainPassword);
        return Result.success();
    }

    @ApiOperation(value = "通过获取当前登录用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public ResponseEntity<SysUser> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserDetailsDefault appUserDetailsDefault = (AppUserDetailsDefault) authentication.getPrincipal();

        if (appUserDetailsDefault == null) {
            return null;
        }
        return Result.success(sysUserHandlerAdaptor.getUserByHash(appUserDetailsDefault.getHash()));
    }


    @ApiOperation(value = "获取当前登录用户信息，在token失效时，该接口会刷新token")
    @RequestMapping(value = "/principal", method = RequestMethod.POST)
    public ResponseEntity<SysUser> principal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserDetailsDefault appUserDetailsDefault = (AppUserDetailsDefault) authentication.getPrincipal();

        if (appUserDetailsDefault == null) {
            return null;
        }
        return Result.success(sysUserHandlerAdaptor.getUserByHash(appUserDetailsDefault.getHash()));
    }


    @ApiOperation(value = "获取当前登录用户信息，在token失效时，该接口会刷新token",
            responseHeaders = @ResponseHeader(name = "X-Token", description = "鉴权token,用户访问后台接口必须携带该参数"))
    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public ResponseEntity<List<SysRole>> queryRoleList(@RequestParam String userHash) {
        return Result.success(sysUserRoleService.queryRoleList(userHash));
    }


    @ApiOperation(value = "根据用户hash,获取用户详细信息")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity<SysUser> getUserInfo(@RequestParam String userHash) {
        return Result.success(sysUserHandlerAdaptor.getUserByHash(userHash));
    }

    @ApiOperation(value = "根据user-name,获取用户详细信息，该接口会返回用户密码")
    @RequestMapping(value = "/user-name", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> getUserInfoByUserName(@RequestParam String userName) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        SysUser sysUser = sysUserService.getSysUserByName(userName);
        Map<String, Object> map = sysUser == null ? null : UtilBean.transBean2Map(sysUser);
        return Result.success(map);
    }


    @ApiOperation(value = "获取党组织下的全部本地用户")
    @RequestMapping(value = "/local-user-list", method = RequestMethod.POST)
    public ResponseEntity<List<SysUser>> queryLocalUserByOrgId(@RequestParam String orgId) {
        return Result.success(sysUserService.queryLocalUserByOrgId(orgId));
    }

    @ApiOperation(value = "获取党组织下的全部党员")
    @RequestMapping(value = "/dt-user-list", method = RequestMethod.POST)
    public ResponseEntity<List<SysUser>> queryDTUserByOrgId(@RequestParam String orgId) {
        return Result.success(sysUserService.queryDTUserByOrgId(orgId));
    }


    @ApiOperation(value = "获取具有某个权限的用户")
    @RequestMapping(value = "/permission-user-list", method = RequestMethod.POST)
    public ResponseEntity<List<SysUser>> queryUserByPermissionList(@RequestParam String permissionName,
                                                                   @RequestParam String manageId,
                                                                   @RequestParam String clientId) {
        List<String> hashList = sysUserService.queryUserByPermissionList(permissionName, manageId, clientId);
        List<SysUser> userList = hashList.stream()
                .map(hash -> sysUserHandlerAdaptor.getSimpleUserByHash(hash))
                .collect(Collectors.toList());

        return Result.success(userList);
    }


    @ApiOperation(value = "获取党组织下的全部党员")
    @RequestMapping(value = "/permission-all-user-list", method = RequestMethod.POST)
    public ResponseEntity<List<SysUser>> queryAllUserByPermissionList(@RequestParam String permissionName,
                                                                      @RequestParam String manageId,
                                                                      @RequestParam String clientId) {

        List<SysOrg> orgs = orgService.queryOrgAllChild(manageId);
        List<String> ids = com.google.common.collect.Lists.newArrayList();
        orgs.stream().filter(org -> org != null).forEach(org -> {
            ids.add(org.getOrgId());
        });
        ids.add(manageId);
        List<String> hashList = Lists.newArrayList();
        ids.stream().forEach(orgId -> {
            List<String> userHashList = sysUserService.queryUserByPermissionList(permissionName, manageId, clientId);
            hashList.addAll(userHashList);
        });

        List<SysUser> userList = hashList.stream()
                .map(hash -> sysUserHandlerAdaptor.getSimpleUserByHash(hash))
                .collect(Collectors.toList());

        return Result.success(userList);
    }


    /**
     * 查询目录下当前时间以后同步的数据
     *
     * @param date
     * @return
     */
    @ApiOperation(value = "获取增量修改数据")
    @RequestMapping(value = "/update-list", method = RequestMethod.POST)
    public ResponseEntity<List<SysDtUser>> queryUpdateList(
            @RequestParam("orgId") String orgId,
            @RequestParam Date date) {
        return Result.success(sysDtUserService.queryUpdateList(orgId, date));
    }

}
