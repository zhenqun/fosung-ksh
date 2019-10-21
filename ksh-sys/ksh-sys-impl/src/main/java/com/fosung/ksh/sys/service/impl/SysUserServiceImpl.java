package com.fosung.ksh.sys.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.id.IdGenerator;
import com.fosung.framework.common.id.snowflake.AppIDGenerator;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.secure.password.AppPasswordEncoderDefault;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.common.cache.RedisCacheable;
import com.fosung.ksh.meeting.control.client.UserClient;
import com.fosung.ksh.meeting.control.dto.user.UserinfoRequestDTO;
import com.fosung.ksh.sys.config.SysProperties;
import com.fosung.ksh.sys.dao.SysUserDao;
import com.fosung.ksh.sys.dto.ImportUser;
import com.fosung.ksh.sys.dto.SysOrg;
import com.fosung.ksh.sys.entity.SysArea;
import com.fosung.ksh.sys.entity.SysRole;
import com.fosung.ksh.sys.entity.SysUser;
import com.fosung.ksh.sys.entity.SysUserRole;
import com.fosung.ksh.sys.entity.constant.ManageType;
import com.fosung.ksh.sys.service.SysAreaService;
import com.fosung.ksh.sys.service.SysOrgService;
import com.fosung.ksh.sys.service.SysRoleService;
import com.fosung.ksh.sys.service.SysUserRoleService;
import com.fosung.ksh.sys.service.SysUserService;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mzlion.easyokhttp.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SysUserServiceImpl extends AppJPABaseDataServiceImpl<SysUser, SysUserDao> implements SysUserService {


    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysOrgService sysOrgService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysProperties sysProperties;

    @Resource
    private SysAreaService sysAreaService;
    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("username", "username:EQ");
            put("hash", "hash:EQ");
            put("hashs", "hash:IN");
            put("orgId", "orgId:EQ");
            put("createDatetime", "createDatetime:GT");
        }
    };

    /**
     * 用户登录验证
     *
     * @return
     */
    @Override
    public SysUser login(String userName, String password) {
        SysUser sysUser = getSysUserByName(userName);
        if (sysUser == null) {
            return null;
        }

        if (!passwordEncoder.matches(password, sysUser.getPassword())) {
            return null;
        }
        SysOrg sysOrg = sysOrgService.getOrgInfo(sysUser.getOrgId());
        List<SysRole> roles = sysUserRoleService.queryRoleList(sysUser.getHash());
        sysUser.setOrgCode(sysOrg.getOrgCode());
        sysUser.setOrgName(sysOrg.getOrgName());
        sysUser.setRoles(roles);
        sysUser.setPassword(null);
        return sysUser;
    }


    @Override
    public SysUser getSysUserByHash(String hash) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("hash", hash);
        List<SysUser> list = queryAll(param);
        if (UtilCollection.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }


    public SysUser getSysUserByName(String username) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("username", username);
        List<SysUser> list = queryAll(param);
        if (UtilCollection.isNotEmpty(list)) {
            if (list.size() > 1) {
                throw new AppException("用户名对应多个用户：" + username);
            }
            return list.get(0);
        }
        return null;
    }


    /**
     * 通过orgId获取该组织下的所有党员
     * todo 后期需要变更为从本地数据库中获取数据
     *
     * @param orgId
     * @return
     */
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public List<SysUser> queryDTUserByOrgId(String orgId) {
        JSONObject json = HttpClient.post(sysProperties.getUrls().getSimpleData() + "/queryUserAllByOrgId/" + orgId).asBean(JSONObject.class);
        JSONArray array = json.getJSONArray("users");
        List<SysUser> users = Lists.newArrayList();
        if (UtilCollection.isNotEmpty(array)) {
            array.stream().forEach(obj -> {
                JSONObject jsonObj = (JSONObject) obj;
                // jsonObj.remove("userId");
                SysUser sysUser = JsonMapper.toJavaObject(jsonObj, SysUser.class);
                sysUser.setUserId(jsonObj.getString("userId"));
                sysUser.setRealName(jsonObj.getString("param1"));
                sysUser.setUsername(jsonObj.getString("userName"));
                users.add(sysUser);
            });
        }

        return users;
    }

    /**
     * 查询全部本地用户
     *
     * @param orgId
     * @return
     */
    @Override
    public List<SysUser> queryLocalUserByOrgId(String orgId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("orgId", orgId);
        List<SysUser> list = queryAll(param);
        return list;
    }

    @SuppressWarnings("all")
    @Transactional
    @Override
    public SysUser saveImportUser(ImportUser importUser) {
        // 保存用户前的准备
        SysUser sysUser = this.importUser2SysUser(importUser);

        IdGenerator<Long> idGenerator = new AppIDGenerator();
        String userHash = idGenerator.getNextId().toString();
        sysUser.setHash(userHash);

        Map<String, Object> map = Maps.newHashMap();
        map.put("username", importUser.getUsername());
        List<SysUser> sysUserList = queryAll(map);
        if (UtilCollection.isNotEmpty(sysUserList)) {
            throw new AppException("用户创建失败,存在相同的用户名");
        }


        String sourcePassword = sysUser.getPassword();
        String encodePassword = this.passwordEncoder.encode(sourcePassword);
        sysUser.setPassword(encodePassword);

//        保存用户
        sysUser = save(sysUser);

        // 保存用户角色 AREA 关系
        if (!Strings.isNullOrEmpty(importUser.getAreaRoleName()) && !Strings.isNullOrEmpty(importUser.getAreaId())) {

            SysArea sysArea = sysAreaService.get(Long.valueOf(importUser.getAreaId()));
            if (sysArea == null) {
                throw new AppException("未找到 " + importUser.getAreaId() + " 对应的行政区域信息 ");
            }

            List<SysRole> sysRoleList = sysRoleService.queryByClientIdAndRoleName("ksh-duty-monitor", importUser.getAreaRoleName());
            SysRole sysRole = sysRoleList.get(0);
            SysUserRole sysUserRole = toSysUserRole(sysRole, sysUser, importUser.getAreaId(), ManageType.AREA);
            sysUserRoleService.save(sysUserRole);
        }

        // 处理 ORGANIZATION 类型数据
        if (!Strings.isNullOrEmpty(importUser.getDtOrgId())) {

            SysOrg sysOrg = sysOrgService.getOrgInfo(importUser.getDtOrgId());
            if (sysOrg == null) {
                throw new AppException("未找到 " + importUser.getDtOrgId() + " 对应的党组织信息 ");
            }

            if (!Strings.isNullOrEmpty(importUser.getOrgRoleName())) {
                List<SysRole> sysRoleList = sysRoleService.queryByClientIdAndRoleName("ksh-organization", importUser.getOrgRoleName());
                SysRole sysRole = sysRoleList.get(0);
                SysUserRole sysUserRole = toSysUserRole(sysRole, sysUser, importUser.getDtOrgId(), ManageType.ORGANIZATION);
                sysUserRoleService.save(sysUserRole);
            }


            if (!Strings.isNullOrEmpty(importUser.getMeetingRoleName())) {
                List<SysRole> sysRoleList = sysRoleService.queryByClientIdAndRoleName("ksh-meeting", importUser.getMeetingRoleName());
                SysRole sysRole = sysRoleList.get(0);
                SysUserRole sysUserRole = toSysUserRole(sysRole, sysUser, importUser.getDtOrgId(), ManageType.ORGANIZATION);
                sysUserRoleService.save(sysUserRole);
            }
        }


        userClient.addUserinfo(sysUser2UserinfoRequestDTO(sysUser));

        return sysUser;

    }

    /**
     * 转换为 系统用户关系
     */
    private SysUserRole toSysUserRole(SysRole sysRole, SysUser sysUser, String manageId, ManageType manageType) {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(sysRole.getId());
        sysUserRole.setUserHash(sysUser.getHash());
        sysUserRole.setManageId(manageId);
        sysUserRole.setManageType(manageType);
        return sysUserRole;
    }

    /**
     * 转换为 系统用户
     */
    private SysUser importUser2SysUser(ImportUser importUser) {
        SysUser sysUser = new SysUser();
        sysUser.setOrgId(importUser.getDtOrgId());
        sysUser.setUsername(importUser.getUsername());
        sysUser.setRealName(importUser.getRealname());
        sysUser.setPassword(importUser.getPassword());
        return sysUser;
    }

    @Transactional
    @Override
    public void updateAndHst(SysUser sysUser) {
        this.update(sysUser, Sets.newHashSet("username", "telephone", "realName", "orgId"));
        userClient.editUser(sysUser2UserinfoRequestDTO(sysUser));
    }

    @Override
    public void resetPassword(SysUser sysUser) {
        String sourcePassword = sysUser.getPassword();
        String encodePassword = this.passwordEncoder.encode(sourcePassword);
        sysUser.setPassword(encodePassword);
        this.update(sysUser, Sets.newHashSet("password"));
    }


    @Override
    public void changePassword(SysUser sysUser, String newPlainPassword) {
        String encodePassword = this.passwordEncoder.encode(newPlainPassword);
        sysUser.setPassword(encodePassword);
        this.update(sysUser, Sets.newHashSet("password"));
    }

    /**
     * 根据权限，查询具备该权限的角色
     *
     * @param permissionName
     * @param manageId
     * @param clientId
     * @return
     */
    @Override
    public List<String> queryUserByPermissionList(String permissionName, String manageId, String clientId) {
        return this.entityDao.queryUserByPermissionList(permissionName, manageId, clientId);
    }

    private UserinfoRequestDTO sysUser2UserinfoRequestDTO(SysUser sysUser) {
        UserinfoRequestDTO userRequest = new UserinfoRequestDTO();
        userRequest.setUserName(sysUser.getHash());
        userRequest.setNickName(sysUser.getRealName());
        userRequest.setOrgId(sysUser.getOrgId());
        userRequest.setMobile(sysUser.getTelephone());
        return userRequest;
    }


    @Resource
    private UserClient userClient;

    @SuppressWarnings("all")
    @Transactional
    @Override
    public SysUser registerUser(SysUser sysUser) {
        if (Strings.isNullOrEmpty(sysUser.getHash())) {
            IdGenerator<Long> idGenerator = new AppIDGenerator();
            sysUser.setHash(idGenerator.getNextId().toString());
        }

        List<SysUser> sysUserList = entityDao.findByUserNameOrTelephone(sysUser.getUsername(), sysUser.getTelephone());
        if (UtilCollection.isNotEmpty(sysUserList)) {
            throw new AppException("用户创建失败,存在相同的用户名或手机号");
        }

        String sourcePassword = sysUser.getPassword();
        String encodePassword = this.passwordEncoder.encode(sourcePassword);
        sysUser.setPassword(encodePassword);
        sysUser = save(sysUser);


        userClient.addUserinfo(sysUser2UserinfoRequestDTO(sysUser));

        return sysUser;
    }

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new AppPasswordEncoderDefault();
        String p = passwordEncoder.encode("dtyhyspxt");
        System.out.println(p);
    }
}
