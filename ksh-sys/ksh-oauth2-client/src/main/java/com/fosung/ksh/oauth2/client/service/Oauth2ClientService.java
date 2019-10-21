package com.fosung.ksh.oauth2.client.service;

import com.fosung.framework.common.secure.auth.constant.UserSource;
import com.fosung.ksh.oauth2.client.dto.SysUser;
import com.fosung.ksh.oauth2.client.dto.TokenDTO;

import java.io.UnsupportedEncodingException;

public interface Oauth2ClientService {


    /**
     * 通过用户名密码进行用户登录
     *
     * @return
     */
    public TokenDTO login(String userName, String password, UserSource userSource);
    /**
     * 获取code认证地址
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    String getSSOLoginUrl(String callback) throws UnsupportedEncodingException;

    /**
     * 获取sso平台访问token
     *
     * @param authorizeCode  用户从sso平台登录成功后带回来的code
     * @param callbackUrl    sso回调地址，需要与获取code时回调地址一致
     * @return token信息
     */
    TokenDTO getSsoAccessToken(String authorizeCode,
                               String callbackUrl);

    /**
     * 通过token获取用户详细信息
     *
     * @param token
     * @return
     */
    SysUser getUserByToken(TokenDTO token);

    /**
     * 获取退出登录地址
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    String getSSOLogoutUrl(String callback) throws UnsupportedEncodingException;

}
