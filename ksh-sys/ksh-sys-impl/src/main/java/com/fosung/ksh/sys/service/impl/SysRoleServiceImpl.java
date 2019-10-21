package com.fosung.ksh.sys.service.impl;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.sys.dao.SysRoleDao;
import com.fosung.ksh.sys.entity.SysPermission;
import com.fosung.ksh.sys.entity.SysRole;
import com.fosung.ksh.sys.entity.SysRolePermission;
import com.fosung.ksh.sys.entity.SysUserRole;
import com.fosung.ksh.sys.service.SysRolePermissionService;
import com.fosung.ksh.sys.service.SysRoleService;
import com.fosung.ksh.sys.service.SysUserRoleService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangyh
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends AppJPABaseDataServiceImpl<SysRole, SysRoleDao> implements SysRoleService {

    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    @Autowired
    private SysUserRoleService sysUserRoleService;
    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {

            put("clientId", "clientId:EQ");
            put("roleName", "roleName:EQ");
            put("roleNameLike", "roleName:LIKE");
            put("roleNameIn", "roleName:IN");
        }
    };

    @Override
    public List<SysRole> queryByClientIdAndRoleName(String clientId, String rolename) {
        Map<String, Object> areaMap = Maps.newHashMap();
        areaMap.put("clientId", clientId);
        areaMap.put("roleName", rolename);
        return this.queryAll(areaMap);
    }

    /**
     * 查询sysRole，并进行及联
     *
     * @param id
     * @return
     */
    public SysRole getUnionPermission(Long id) {
        SysRole role = get(id);
        List<SysPermission> permissionList = sysRolePermissionService.queryPermissionList(id);
        role.setPermissions(permissionList);
        return role;
    }

    @Override
    public void deleteCheck(List<Long> ids) throws Exception {
        Map<String,Object> param = Maps.newHashMap();
        param.put("roleIdIn",ids);
        List<SysRolePermission> sysRolePermissionList = sysRolePermissionService.queryAll(param);
        if (UtilCollection.isNotEmpty(sysRolePermissionList)){
            throw new Exception("删除角色失败，请删除关联数据");
        }
        List<SysUserRole> sysUserRoleList = sysUserRoleService.queryAll(param);
        if (UtilCollection.isNotEmpty(sysUserRoleList)){
            throw new Exception("删除角色失败，请删除关联数据");
        }
        super.delete(ids);
    }


    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }
}
