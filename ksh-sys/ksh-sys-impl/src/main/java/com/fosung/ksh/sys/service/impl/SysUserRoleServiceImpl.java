package com.fosung.ksh.sys.service.impl;

import com.fosung.framework.common.util.UtilNumber;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.sys.dao.SysUserRoleDao;
import com.fosung.ksh.sys.dto.SysOrg;
import com.fosung.ksh.sys.entity.SysArea;
import com.fosung.ksh.sys.entity.SysRole;
import com.fosung.ksh.sys.entity.SysUser;
import com.fosung.ksh.sys.entity.SysUserRole;
import com.fosung.ksh.sys.entity.constant.ManageType;
import com.fosung.ksh.sys.handler.SysUserHandlerAdaptor;
import com.fosung.ksh.sys.service.*;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangyh
 */
@Slf4j
@Service
public class SysUserRoleServiceImpl extends AppJPABaseDataServiceImpl<SysUserRole, SysUserRoleDao> implements SysUserRoleService {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysOrgService sysOrgService;


    @Autowired
    private SysAreaService sysAreaService;

    @Resource
    private SysUserService sysUserService;

    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("userHash", "userHash:EQ");
            put("roleId", "roleId:EQ");
            put("roleIdIn", "roleId:IN");
        }
    };

    /**
     * 及联获取role
     *
     * @param userHash
     * @return
     */
    public List<SysRole> queryRoleList(String userHash) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("userHash", userHash);
        List<SysUserRole> list = queryAll(param);
        List<SysRole> roles = Lists.newArrayList();

        list.stream().forEach(sysUserRole -> {
            SysRole sysRole = sysRoleService.getUnionPermission(sysUserRole.getRoleId());
            sysRole.setRoleId(sysUserRole.getId().toString());
            if (sysUserRole.getManageType() == ManageType.ORGANIZATION) {
                SysOrg sysOrg = sysOrgService.getOrgInfo(sysUserRole.getManageId());
                if (sysOrg != null) {
                    sysRole.setManageId(sysOrg.getOrgId());
                    sysRole.setManageCode(sysOrg.getOrgCode());
                    sysRole.setManageName(sysOrg.getOrgName());
                    roles.add(sysRole);
                }
            } else {
                SysArea sysArea = sysAreaService.getAreaInfo(UtilNumber.createLong(sysUserRole.getManageId()));
                if (sysArea != null) {
                    sysRole.setManageId(sysArea.getId().toString());
                    sysRole.setManageCode(sysArea.getAreaCode());
                    sysRole.setManageName(sysArea.getAreaName());
                    roles.add(sysRole);
                }
            }

        });

        return roles;
    }

    @Override
    public List<SysUser> queryUsersByRoleId(Long roleId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("roleId", roleId);
        return queryAll(param).stream().map(item -> sysUserHandlerAdaptor.getUserByHash(item.getUserHash())).collect(Collectors.toList());
    }

    @Override
    public void rebind(Long roleId, Set<String> userHashs) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("roleId", roleId);
        List<SysUserRole> sysUserRoleList = this.queryAll(param);

        Map<String, Long> dbUserHashIdMap = sysUserRoleList.stream().collect(Collectors.toMap(SysUserRole::getUserHash, SysUserRole::getId));

        Set<String> dbUserHash = dbUserHashIdMap.keySet();

        Sets.SetView<String> saveView = Sets.difference(userHashs, dbUserHash);

        saveView.forEach(item -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserHash(item);
            this.save(sysUserRole);
        });

        Sets.SetView<String> deleteView = Sets.difference(dbUserHash, userHashs);

        Set<Long> deleteIds = deleteView.stream().map(dbUserHashIdMap::get).collect(Collectors.toSet());

        this.delete(deleteIds);

    }


    @Autowired
    private SysUserHandlerAdaptor sysUserHandlerAdaptor;


    @Override
    public List<SysUserRole> queryFullAll(Map<String, Object> param) {
        List<SysUserRole> sysUserRoleList = this.queryAll(param);

        Map<String, SysUser> dbUserHashIdMap = sysUserRoleList.stream().map(item -> sysUserHandlerAdaptor.getUserByHash(item.getUserHash())).collect(Collectors.toMap(SysUser::getHash, item -> item));

        sysUserRoleList.forEach(item -> {
            if (item.getManageType() != null && !Strings.isNullOrEmpty(item.getManageId())) {
                if (item.getManageType() == ManageType.ORGANIZATION) {
                    item.setSysOrg(sysOrgService.getOrgInfo(item.getManageId()));
                } else if (item.getManageType() == ManageType.AREA) {
                    item.setSysArea(sysAreaService.getAreaInfo(UtilNumber.createLong(item.getManageId())));
                }
            }
            item.setSysUser(dbUserHashIdMap.get(item.getUserHash()));
        });

        return sysUserRoleList;
    }

    @Override
    public void updateManage(List<SysUserRole> sysUserRoles) {
        this.update(sysUserRoles, Sets.newHashSet("manageType", "manageId"));
    }


    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }
}
