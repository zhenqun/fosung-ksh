package com.fosung.ksh.oauth2.server.dt.secure.authentication;

import com.fosung.framework.common.config.AppSecureProperties;
import com.fosung.framework.common.secure.auth.AppUserDetails;
import com.fosung.framework.common.secure.auth.AppUserDetailsDefault;
import com.fosung.framework.common.util.UtilString;
import com.fosung.ksh.oauth2.server.dt.dto.DTUser;
import com.fosung.ksh.oauth2.server.dt.surpport.SSOUserAdapter;
import com.fosung.ksh.oauth2.server.dt.surpport.TokenVO;
import com.google.common.collect.Sets;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

/**
 * 灯塔用户加载服务
 *
 * @Author : liupeng
 * @Date : 2018-12-04
 * @Modified By
 */
@Slf4j
public class AppDTAuthenticationService implements InitializingBean {

    @Setter
    @Autowired
    private AppSecureProperties appSecureProperties;

    @Setter
    @Autowired
    private SSOUserAdapter ssoUserAdapter;


    /**
     * 查询用户详情
     *
     * @param username
     * @param password
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String username, String password) throws UsernameNotFoundException {
        log.debug("登录用户信息->{}:{}", username, password);

        // 获取 accessToken
        TokenVO accessToken = null;
        try {
            accessToken = ssoUserAdapter.token(username, password);
        } catch (Exception e) {
            throw new BadCredentialsException(UtilString.isBlank(e.getMessage()) ? "用户名或密码错误" : e.getMessage());
        }

        // 根据灯塔用户构建当前系统登录用户详情
        AppUserDetails appUserDetails = null;
        try {
            DTUser dtUser = ssoUserAdapter.getUserByToken(accessToken);
            appUserDetails = buildAppUserDetails(dtUser);
            if (appUserDetails == null) {
                throw new InternalAuthenticationServiceException("登录用户构建异常");
            }
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(UtilString.isBlank(e.getMessage()) ? "登录用户构建异常" : e.getMessage());
        }

        return appUserDetails;
    }


    /**
     * 构建登录用户详情
     *
     * @param dtUser
     * @return
     */
    public AppUserDetails buildAppUserDetails(DTUser dtUser) {
        if (dtUser == null) {
            return null;
        }
        // 创建系统session用户
        AppUserDetailsDefault appUserDetailsDefault = new AppUserDetailsDefault(dtUser.getUserName());
        // 用户id直接使用 返回的用户 id hash 值，不是idCardHash的值
        appUserDetailsDefault.setUserId(dtUser.getUserId());
        appUserDetailsDefault.setHash(dtUser.getHash());
//        appUserDetailsDefault.setUserSource(UserSource.DT);

        // 默认用户角色编码
        Set<String> defaultUserRoles = Sets.newHashSet(appSecureProperties.getAuth().getDefaultUserRoles());
        return appUserDetailsDefault;
    }


    /**
     * 加载用户角色信息
     *
     * @param appUserDetailsDefault
     * @param userRoles
     */
    public void loadUserRoles(AppUserDetailsDefault appUserDetailsDefault, Set<String> userRoles) {

    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
