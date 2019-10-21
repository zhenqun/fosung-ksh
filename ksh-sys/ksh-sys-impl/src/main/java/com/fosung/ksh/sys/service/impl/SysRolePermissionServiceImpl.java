package com.fosung.ksh.sys.service.impl;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.sys.dao.SysRolePermissionDao;
import com.fosung.ksh.sys.entity.SysPermission;
import com.fosung.ksh.sys.entity.SysRole;
import com.fosung.ksh.sys.entity.SysRolePermission;
import com.fosung.ksh.sys.service.SysPermissionService;
import com.fosung.ksh.sys.service.SysRolePermissionService;
import com.fosung.ksh.sys.service.SysRoleService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangyh
 */
@Slf4j
@Service
public class SysRolePermissionServiceImpl extends AppJPABaseDataServiceImpl<SysRolePermission, SysRolePermissionDao> implements SysRolePermissionService {

    @Autowired
    private SysPermissionService sysPermissionService;

    @Autowired
    private SysRoleService sysRoleService;
    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("roleId", "roleId:EQ");
            put("roleIdIn", "roleId:IN");
            put("permissionId", "permissionId:EQ");
        }
    };

    public List<SysPermission> queryPermissionList(Long roleId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("roleId", roleId);
        List<SysRolePermission> list = queryAll(param);
        List<SysPermission> permissions = Lists.newArrayList();
        list.stream().forEach(sysRolePermission -> {
            SysPermission sysPermission = sysPermissionService.get(sysRolePermission.getPermissionId());
            permissions.add(sysPermission);
        });

        return permissions;
    }

    @Override
    public List<SysRole> queryRoleList(Long permissionId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("permissionId", permissionId);
        List<SysRolePermission> list = queryAll(param);
        return list.stream().map(sysRolePermission -> sysRoleService.get(sysRolePermission.getRoleId())).collect(Collectors.toList());
    }

    /**
     * @param roleId
     * @param permissionId
     * @return
     */
    public SysRolePermission get(Long roleId, Long permissionId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("permissionId", permissionId);
        param.put("roleId", roleId);
        List<SysRolePermission> list = queryAll(param);
        return UtilCollection.isNotEmpty(list) ? list.get(0) : null;
    }


    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }
}
