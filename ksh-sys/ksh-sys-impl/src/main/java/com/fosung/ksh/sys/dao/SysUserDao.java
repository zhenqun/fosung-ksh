package com.fosung.ksh.sys.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.framework.dao.jpa.annotation.MybatisQuery;
import com.fosung.ksh.sys.entity.SysUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by lizhen on 2018\7\17 0017.
 */
public interface SysUserDao extends AppJPABaseDao<SysUser, Long> {
    @Query("from SysUser where username =:username or telephone=:telephone")
    List<SysUser> findByUserNameOrTelephone(@Param("username") String username, @Param("telephone") String telephone);


    /**
     * 根据权限，查询具备该权限的角色
     * @param permissionName
     * @param manageId
     * @param clientId
     * @return
     */
    @MybatisQuery
    public List<String> queryUserByPermissionList(@org.apache.ibatis.annotations.Param("permissionName") String permissionName,
                                                  @org.apache.ibatis.annotations.Param("manageId") String manageId,
                                                  @org.apache.ibatis.annotations.Param("clientId") String clientId);
}
