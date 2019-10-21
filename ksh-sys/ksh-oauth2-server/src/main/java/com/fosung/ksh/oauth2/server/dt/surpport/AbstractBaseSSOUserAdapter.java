package com.fosung.ksh.oauth2.server.dt.surpport;

import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.ksh.oauth2.server.config.SSOProperties;
import com.fosung.ksh.oauth2.server.dt.dto.DTUser;
import com.mzlion.easyokhttp.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import java.util.Base64;

@Slf4j
public abstract class AbstractBaseSSOUserAdapter implements SSOUserAdapter {


    /**
     * 用户登录认证
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @Override
    public TokenVO token(String userName, String password) {
        TokenVO token = null;
        token = HttpClient.post(getSSOConfig().getUrl() + "/oauth/token")
                .header(HttpHeaders.AUTHORIZATION, buildAccessTokenAuthorization())
                .param("grant_type", "password")
                .param("username", userName)
                .param("password", password)
                .asBean(TokenVO.class);
        log.debug("灯塔 accessToken 获取完成: userName={}  , token={}", userName, token);
        return token;
    }


    /**
     * 通过token获取用户详细信息
     *
     * @param token
     * @return
     */
    @Override
    public DTUser getUserByToken(TokenVO token) {
        DTUser dtUser = null;

        JSONObject json = HttpClient.post(getSSOConfig().getUrl() + getSSOConfig().getMethod().getPrincipal())
                .header(HttpHeaders.AUTHORIZATION, buildAccessTokenAuthorization(token.getAccess_token()))
                .asBean(JSONObject.class);
        log.debug("获取用户信息: token={} ,result={}", token, json);

        if (json.containsKey("error")) {
            throw new InternalAuthenticationServiceException("登录用户构建异常");
        }

        dtUser = getSysUser(json);

        return dtUser;
    }

    /**
     * 对SYSUSER
     *
     * @param json
     * @return
     */
    private DTUser getSysUser(JSONObject json) {
        DTUser dtUser;
        dtUser = JsonMapper.toJavaObject(json, DTUser.class);
        return dtUser;
    }


    public abstract SSOProperties.SSOConfig getSSOConfig();


    /**
     * 如果已经获取到token，则根据token获取请求头
     *
     * @param accessToken
     * @return
     */
    public String buildAccessTokenAuthorization(String accessToken) {
        String authorization = "Bearer " + accessToken;
        return authorization;
    }

    /**
     * 构建accessToken认证请求头
     *
     * @return
     */
    public String buildAccessTokenAuthorization() {

        String oauth2WebToken = getSSOConfig().getClientId() + ":" + getSSOConfig().getClientSecret();

        String authorization = Base64.getEncoder().encodeToString(oauth2WebToken.getBytes());

        return "Basic " + authorization;
    }

}
