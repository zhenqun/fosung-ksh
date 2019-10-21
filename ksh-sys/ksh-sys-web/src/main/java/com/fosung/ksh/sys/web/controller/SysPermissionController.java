package com.fosung.ksh.sys.web.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.dto.DtoUtil;
import com.fosung.ksh.common.dto.handler.KshDTOCallbackHandler;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.sys.entity.SysPermission;
import com.fosung.ksh.sys.service.SysPermissionService;
import com.fosung.ksh.sys.service.SysRolePermissionService;
import com.fosung.ksh.sys.service.SysRoleService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Api(description = "系统权限接口", tags = {"2、系统权限接口"})
@RestController
@RequestMapping(value = SysPermissionController.BASE_PATH)
public class SysPermissionController extends AppIBaseController {

    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/permission";

    @Resource
    private SysPermissionService sysPermissionService;
    @Resource
    private SysRolePermissionService sysRolePermissionService;
    @Resource
    private SysRoleService sysRoleService;

    @ApiOperation(value = "获取权限列表")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseEntity<SysPermission> query(
            @RequestParam(required = false, value = "pagenum", defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @RequestParam(required = false, value = "pagesize", defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) {

        //获取查询参数
        Map<String, Object> searchParam = getParametersStartingWith(getHttpServletRequest(), DEFAULT_SEARCH_PREFIX);
        //执行分页查询
        Page<SysPermission> meetingTemplatePage = sysPermissionService.queryByPage(searchParam, pageNum, pageSize);
        DtoUtil.handler(meetingTemplatePage.getContent(), getDtoCallbackHandler());
        return Result.success(meetingTemplatePage);
    }


    @ApiOperation(value = "获取权限列表，不分页")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<List<SysPermission>> list() {
        return Result.success(sysPermissionService.queryAll(Maps.newHashMap()));
    }

    @PostMapping("delete")
    public ResponseEntity delete(@RequestParam(value = "id") List<Long> ids) {
        sysPermissionService.delete(ids);
        return Result.success();
    }

    @PostMapping("get")
    public ResponseEntity<SysPermission> get(@RequestParam(value = "id") Long id) {
        SysPermission sysPermission = sysPermissionService.get(id);
        if (sysPermission != null) {
            getDtoCallbackHandler().doHandler(sysPermission);
        }
        return Result.success(sysPermission);
    }


    @PostMapping("save")
    public ResponseEntity<SysPermission> save(@RequestBody SysPermission sysPermission) {
        if (sysPermission.getId() != null) {
            sysPermissionService.updateAndRole(sysPermission);
        } else {
            sysPermission = sysPermissionService.saveAndRole(sysPermission);
        }
        return Result.success(sysPermission);
    }


    /**
     * 获取PO到DTO转换的接口。主要用于在前端展示数据之前首先将数据格式处理完成。
     *
     * @return
     */
    public KshDTOCallbackHandler getDtoCallbackHandler() {
        //创建转换接口类
        KshDTOCallbackHandler<SysPermission> dtoCallbackHandler = new KshDTOCallbackHandler<SysPermission>() {
            @Override
            public void doHandler(SysPermission dto) {
                dto.setRoles(sysRolePermissionService.queryRoleList(dto.getId()));
            }
        };
        return dtoCallbackHandler;
    }
}
