package com.fosung.ksh.oauth2.server.dt.surpport;

import com.fosung.ksh.oauth2.server.dt.dto.DTUser;

public interface SSOUserAdapter {
    /**
     * 用户登录认证
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public TokenVO token(String userName, String password);

    /**
     * 通过token获取用户详细信息
     *
     * @param token
     * @return
     */
    public DTUser getUserByToken(TokenVO token);
}
