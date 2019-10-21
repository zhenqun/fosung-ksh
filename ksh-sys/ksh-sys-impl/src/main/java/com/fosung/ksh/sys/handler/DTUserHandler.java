package com.fosung.ksh.sys.handler;

import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilString;
import com.fosung.ksh.sys.config.SysProperties;
import com.fosung.ksh.sys.entity.SysRole;
import com.fosung.ksh.sys.entity.SysUser;
import com.fosung.ksh.sys.service.SysUserRoleService;
import com.google.common.collect.Lists;
import com.mzlion.core.json.TypeRef;
import com.mzlion.easyokhttp.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@Order(100)
public class DTUserHandler implements UserHandler {


    @Autowired
    private SysProperties sysProperties;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 通过hash获取用户详细信息
     * todo 后期需要变更为从本地数据库中获取数据
     *
     * @param hash
     * @return
     */
    public SysUser getUserByHash(String hash) {
        List<JSONObject> jsons = HttpClient.textBody(sysProperties.getUrls().getSimpleData() + "/queryUserInfoByHash/").json(Lists.newArrayList(hash)).asBean(new TypeRef<List<JSONObject>>() {
        });

        log.debug("用户查询:{}={}", hash, jsons);
        if (UtilCollection.isEmpty(jsons)) {
            return null;
        }
        JSONObject json = jsons.get(0);
        json.remove("userId");
        json.put("username", json.getString("userName"));
        json.put("realName", json.getString("userName"));
        SysUser sysUser = JsonMapper.toJavaObject(json, SysUser.class);

        // 本地角色和SSO中的角色进行合并，并去掉没有manageid的角色
        List<SysRole> roles = Lists.newArrayList();
        if (UtilCollection.isNotEmpty(sysUser.getRoles())) {
            roles = sysUser.getRoles().stream()
                    .filter(role -> UtilString.isNotEmpty(role.getManageId())).collect(Collectors.toList());
        }
        List<SysRole> localRoles = sysUserRoleService.queryRoleList(sysUser.getHash());
        roles.addAll(localRoles);

        // 如果用户未配置任何角色，则设置为所属组织的普通用户
        if (UtilCollection.isEmpty(roles)) {
            SysRole sysRole = new SysRole();
            sysRole.setRoleId(UUID.randomUUID().toString());
            sysRole.setRoleDescription("普通用户");
            sysRole.setManageId(sysUser.getOrgId());
            sysRole.setManageCode(sysUser.getOrgCode());
            sysRole.setManageName(sysUser.getOrgName());
            roles.add(sysRole);
        }
        sysUser.setRoles(roles);
        return sysUser;
    }

    /**
     * 通过hash获取用户减项信息
     *
     * @param hash
     * @return
     */
    @Override
    public SysUser getSimpleUserByHash(String hash) {
        List<JSONObject> jsons = HttpClient.textBody(sysProperties.getUrls().getSimpleData() + "/queryUserInfoByHash/").json(Lists.newArrayList(hash)).asBean(new TypeRef<List<JSONObject>>() {
        });

        log.debug("用户查询:{}={}", hash, jsons);
        if (UtilCollection.isEmpty(jsons)) {
            return null;
        }
        JSONObject json = jsons.get(0);
        json.remove("userId");
        json.put("username", json.getString("userName"));
        json.put("realName", json.getString("userName"));
        SysUser sysUser = JsonMapper.toJavaObject(json, SysUser.class);
        return sysUser;
    }
}
