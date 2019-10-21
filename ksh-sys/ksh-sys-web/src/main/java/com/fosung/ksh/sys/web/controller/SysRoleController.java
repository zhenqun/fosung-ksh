package com.fosung.ksh.sys.web.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.sys.entity.SysRole;
import com.fosung.ksh.sys.service.SysRoleService;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangyh
 */
@Api(description = "系统角色接口", tags = {"2、系统角色接口"})
@RestController
@RequestMapping(value = SysRoleController.BASE_PATH)
public class SysRoleController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/role";


    @Resource
    private SysRoleService sysRoleService;


    @ApiOperation(value = "获取角色列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<SysRole> query() {
        //获取查询参数
        Map<String, Object> searchParam = getParametersStartingWith(getHttpServletRequest(), DEFAULT_SEARCH_PREFIX);
        List<SysRole> sysRoleList = sysRoleService.queryAll(searchParam);
        return Result.success(sysRoleList);
    }

    @ApiOperation(value = "根据角色ID,获取角色详细信息")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity<SysRole> getUserInfo(@RequestParam Long id) {
        return Result.success(sysRoleService.get(id));
    }



    @ApiOperation(value = "保存或修改角色")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<SysRole> saveOrUpdate(@RequestBody SysRole sysRole) {
        if (sysRole.getId() == null){
            sysRole = sysRoleService.save(sysRole);
        }else {
            sysRoleService.update(sysRole, Sets.newHashSet("clientId","clientName","roleName","roleDescription"));
        }
        return Result.success(sysRole);
    }


    @ApiOperation(value = "删除角色")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<SysRole> saveOrUpdate(@RequestParam List<Long> ids) throws Exception {
        sysRoleService.deleteCheck(ids);
        return Result.success();
    }

}
