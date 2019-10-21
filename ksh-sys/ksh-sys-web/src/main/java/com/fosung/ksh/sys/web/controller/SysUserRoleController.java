package com.fosung.ksh.sys.web.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.sys.entity.SysUserRole;
import com.fosung.ksh.sys.service.SysUserRoleService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@Api(description = "系统用户接口", tags = {"2、系统用户接口"})
@RestController
@RequestMapping(value = SysUserRoleController.BASE_PATH)
public class SysUserRoleController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/user-role";


    @Resource
    private SysUserRoleService sysUserRoleService;


    @ApiOperation(value = "通过角色ID获取当前用户列表")
    @RequestMapping(value = "/update-manage", method = RequestMethod.POST)
    public ResponseEntity<List<SysUserRole>> updateManage(@RequestBody List<SysUserRole> sysUserRoles) {
        sysUserRoleService.updateManage(sysUserRoles);
        return Result.success();
    }


    @ApiOperation(value = "通过角色ID获取当前用户列表")
    @RequestMapping(value = "/by-role-id", method = RequestMethod.POST)
    public ResponseEntity<List<SysUserRole>> usersByRoleIds(@RequestParam Long roleId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("roleId", roleId);
        return Result.success(sysUserRoleService.queryFullAll(param));
    }


    @ApiOperation(value = "重新绑定角色")
    @RequestMapping(value = "/rebind", method = RequestMethod.POST)
    public ResponseEntity rebind(@RequestParam Long roleId, @RequestParam Set<String> userIds) {
        sysUserRoleService.rebind(roleId, userIds);
        return Result.success();
    }


    @ApiOperation(value = "给用户添加角色")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity save(@RequestBody SysUserRole sysUserRole) {
        sysUserRoleService.save(sysUserRole);
        return Result.success();
    }



    @ApiOperation(value = "删除已绑定角色")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity delete(@RequestParam Long id) {
        sysUserRoleService.delete(id);
        return Result.success();
    }

}
