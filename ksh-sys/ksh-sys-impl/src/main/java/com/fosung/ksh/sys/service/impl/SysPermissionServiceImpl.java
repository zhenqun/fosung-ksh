package com.fosung.ksh.sys.service.impl;

import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.sys.dao.SysPermissionDao;
import com.fosung.ksh.sys.entity.SysPermission;
import com.fosung.ksh.sys.entity.SysRole;
import com.fosung.ksh.sys.entity.SysRolePermission;
import com.fosung.ksh.sys.service.SysPermissionService;
import com.fosung.ksh.sys.service.SysRolePermissionService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangyh
 */
@Slf4j
@Service
public class SysPermissionServiceImpl extends AppJPABaseDataServiceImpl<SysPermission, SysPermissionDao> implements SysPermissionService {

    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {

        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }


    @Resource
    private SysRolePermissionService sysRolePermissionService;

    @Override
    public SysPermission saveAndRole(SysPermission sysPermission) {
        List<SysRole> roles = sysPermission.getRoles();
        Long permissionId = this.save(sysPermission).getId();

        List<SysRolePermission> sysRolePermissionList = roles.stream().map(item -> {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setPermissionId(permissionId);
            sysRolePermission.setRoleId(item.getId());
            return sysRolePermission;
        }).collect(Collectors.toList());
        sysRolePermissionService.saveBatch(sysRolePermissionList);
        return sysPermission;
    }

    @Override
    public void updateAndRole(SysPermission sysPermission) {
        this.update(sysPermission, Sets.newHashSet("permissionId", "permissionName", "permissionDescription"));
        Map<String, Object> param = Maps.newHashMap();
        param.put("permissionId", sysPermission.getId());
        List<SysRolePermission> q = sysRolePermissionService.queryAll(param);
        sysRolePermissionService.delete(q.stream().map(SysRolePermission::getId).collect(Collectors.toList()));

        List<SysRolePermission> sysRolePermissionList = sysPermission.getRoles().stream().map(item -> {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setPermissionId(sysPermission.getId());
            sysRolePermission.setRoleId(item.getId());
            return sysRolePermission;
        }).collect(Collectors.toList());
        sysRolePermissionService.saveBatch(sysRolePermissionList);
    }
}
