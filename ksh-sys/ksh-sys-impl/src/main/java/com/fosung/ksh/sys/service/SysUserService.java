package com.fosung.ksh.sys.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.sys.dto.ImportUser;
import com.fosung.ksh.sys.entity.SysUser;

import java.util.List;

public interface SysUserService extends AppBaseDataService<SysUser, Long> {
    /**
     * 用户登录，调用SSO进行验证
     *
     * @return
     */
    SysUser login(String userName, String password);


    /**
     * 通过hash获取用户详细信息
     *
     * @param hash
     * @return
     */
    SysUser getSysUserByHash(String hash);

    /**
     * 通过用户名获取用户信息
     *
     * @param username
     * @return
     */
    public SysUser getSysUserByName(String username);

    /**
     * 通过orgId获取该组织下的所有用户
     *
     * @param orgId
     * @return
     */
    public List<SysUser> queryDTUserByOrgId(String orgId);

    /**
     * 通过orgId获取该组织下的所有用户
     *
     * @param orgId
     * @return
     */
    public List<SysUser> queryLocalUserByOrgId(String orgId);

    /**
     * 创建、注册新用户。同步到好视通
     */
    SysUser registerUser(SysUser sysUser);

    /**
     * 保存导入用户
     */
    SysUser saveImportUser(ImportUser importUser);

    /**
     * 修改用户同步到好视通
     */
    void updateAndHst(SysUser sysUser);

    /**
     * 修改用户密码
     */
    void resetPassword(SysUser sysUser);

    /**
     * 修改用户密码
     */
    void changePassword(SysUser sysUser, String newPlainPassword);



    /**
     * 根据权限，查询具备该权限的角色
     * @param permissionName
     * @param manageId
     * @param clientId
     * @return
     */
    List<String> queryUserByPermissionList(String permissionName,
                                           String manageId,
                                           String clientId);
}
