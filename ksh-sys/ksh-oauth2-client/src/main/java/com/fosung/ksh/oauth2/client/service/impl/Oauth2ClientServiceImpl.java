package com.fosung.ksh.oauth2.client.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.secure.auth.constant.UserSource;
import com.fosung.ksh.oauth2.client.config.Oauth2ClientProperties;
import com.fosung.ksh.oauth2.client.dto.SysUser;
import com.fosung.ksh.oauth2.client.dto.TokenDTO;
//import com.mzlion.easyokhttp.HttpClient;
import com.google.gson.JsonObject;
import com.mzlion.easyokhttp.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * @author wangyihua
 * @date 2019-06-03 11:10
 */
@Slf4j
@Service
public class Oauth2ClientServiceImpl implements com.fosung.ksh.oauth2.client.service.Oauth2ClientService {

    @Autowired
    private Oauth2ClientProperties clientProperties;


    @Autowired
    private RedisTemplate redisTemplate;

    private static final String TOKEN_PREFIX = "KSH_TOKEN_";


    /**
     * token缓存超时时间，默认30天
     */
    private static final Integer TOKEN_EXP = 30;


    /**
     * user缓存超时时间，默认30分钟
     */
    private static final Integer USER_EXP = 30;

    /**
     * 通过用户名密码进行用户登录
     *
     * @return
     */
    public TokenDTO login(String userName, String password, UserSource userSource) {
        TokenDTO token = null;
        try {
            token = HttpClient.post(clientProperties.getUrl() + "/oauth/token")
                    .header(org.springframework.http.HttpHeaders.AUTHORIZATION, buildAccessTokenAuthorization())
                    .param("grant_type", "password")
                    .param("user_source", userSource.name())
                    .param("username", userName)
                    .param("password", password)
                    .asBean(TokenDTO.class);
            log.debug("accessToken 获取完成:  token={}", token);
            redisTemplate.opsForValue().set(getTokenKey(token), token, TOKEN_EXP, TimeUnit.DAYS);
        } catch (Exception e) {
            log.error("用户登录失败，错误信息：{}", ExceptionUtils.getStackTrace(e));
        }

        return token;
    }

    /**
     * 获取code认证地址
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    @Override
    public String getSSOLoginUrl(String callback) throws UnsupportedEncodingException {
        String redirectUri = URLEncoder.encode(callback, "UTF-8");
        String loginUrl = clientProperties.getUrl()
                + getMethod().getAuthorizeCode()
                + "?response_type=code&redirect_uri="
                + redirectUri + "&client_id="
                + clientProperties.getClientId();
        return loginUrl;
    }

    /**
     * 获取退出登录地址
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    @Override
    public String getSSOLogoutUrl(String callback) throws UnsupportedEncodingException {
        String redirectUri = URLEncoder.encode(callback, "UTF-8");
        String loginUrl = clientProperties.getUrl()
                + getMethod().getLogout()
                + "?redirect_uri="
                + redirectUri;
        return loginUrl;
    }


    /**
     * 获取sso平台访问token
     *
     * @param authorizeCode 用户从sso平台登录成功后带回来的code
     * @param callbackUrl   sso回调地址，需要与获取code时回调地址一致
     * @return token信息
     */
    @Override
    public TokenDTO getSsoAccessToken(String authorizeCode,
                                      String callbackUrl) {
        TokenDTO token = null;
        RestTemplate restTemplate=new RestTemplate();
        String url=clientProperties.getUrl() + "/oauth/token";
        HttpHeaders headers=new HttpHeaders();
        headers.set(org.springframework.http.HttpHeaders.AUTHORIZATION, buildAccessTokenAuthorization());
        MultiValueMap<String,String> map=new LinkedMultiValueMap<>();
        map.add("grant_type","authorization_code");
        map.add("client_id",clientProperties.getClientId());
        map.add("client_secret",clientProperties.getClientSecret());
        map.add("code",authorizeCode);
        map.add("redirect_uri",callbackUrl);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<TokenDTO> response=restTemplate.postForEntity(url,request,TokenDTO.class);
        token=response.getBody();
        log.debug("accessToken 获取完成: code={}  , token={}", authorizeCode, token);
        redisTemplate.opsForValue().set(getTokenKey(token), token, TOKEN_EXP, TimeUnit.DAYS);
        return token;
    }


    /**
     * 通过token获取用户详细信息
     *
     * @param token
     * @return
     */
    @Override
    public SysUser getUserByToken(TokenDTO token) {
        SysUser sysUser = null;
        token = checkToken(token);
        RestTemplate restTemplate=new RestTemplate();
        String url=clientProperties.getUrl() + clientProperties.getMethod().getPrincipal();
        HttpHeaders headers=new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION,buildAccessTokenAuthorization(token.getAccess_token()));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null,headers);
        ResponseEntity<SysUser> response=restTemplate.postForEntity(url,request,SysUser.class);
        sysUser =response.getBody();
        System.out.print(sysUser);
        log.debug("获取用户信息: token={} ,result={}", token, sysUser);
        if (response.getStatusCodeValue()!=200) {
            throw new AppException("", HttpStatus.UNAUTHORIZED.value() + "", "获取用户失败");
        }
//        sysUser = JsonMapper.toJavaObject(json, SysUser.class);
        sysUser.setToken(token.getAccess_token());
        return sysUser;
    }


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

        String oauth2WebToken = clientProperties.getClientId() + ":" + clientProperties.getClientSecret();

        String authorization = Base64.getEncoder().encodeToString(oauth2WebToken.getBytes());

        return "Basic " + authorization;
    }

    private Oauth2ClientProperties.SSOMethod getMethod() {
        return clientProperties.getMethod();
    }


    /**
     * 检查并获取最新的ssoToken
     *
     * @return
     */
    private TokenDTO checkToken(TokenDTO token) {
        token = getTokenFromRedis(token);
        Long nowTime = System.currentTimeMillis();
        Long cacheTime = token.getTime();
        Integer expires = token.getExpires_in();

        //第一步：先验证token本地是否超时
        log.debug("Token本地验证: token={} , nowTime={} ", token, nowTime);

        // 如果距离当前过期时间小于1分钟，则刷新token
        if (expires - (nowTime - cacheTime) / 1000 < 60) {
            return refreshToken(token);
        }
        //        去SSO检验token是否过期
        RestTemplate restTemplate=new RestTemplate();
        String url=clientProperties.getUrl()
                + clientProperties.getMethod().getCheckToken()
                + "?token=" + token.getAccess_token();
        HttpHeaders headers=new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, buildAccessTokenAuthorization(token.getAccess_token()));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null,headers);
        JSONObject json = restTemplate.postForObject(url,request, JSONObject.class);

        log.debug("Token服务器端验证: token={} , nowTime={} , result={}", token, nowTime, json);

        if (json.containsKey("error")) {
            return refreshToken(token);
        }
        expires = json.getInteger("exp");
        //如果TOKEN在服务器端过期时间小雨5分钟，则刷新
        //如果距离当前过期时间小于5分钟，则刷新token
        if (expires < 60) {
            return refreshToken(token);
        }
        return token;
    }

    /**
     * 刷新token令牌,并更新token缓存
     *
     * @param token
     * @return
     */
    private TokenDTO refreshToken(TokenDTO token) {
        String oldKey = getTokenKey(token);
        token = getTokenFromRedis(token);
        String refreshToken = token.getRefresh_token();

//        token = HttpClient.post(clientProperties.getUrl() + clientProperties.getMethod().getToken())
//                .header(HttpHeaders.AUTHORIZATION, buildAccessTokenAuthorization())
//                .param("grant_type", "refresh_token")
//                .param("refresh_token", refreshToken)
//                .asBean(TokenDTO.class);

        RestTemplate restTemplate=new RestTemplate();
        String url=clientProperties.getUrl()
                + clientProperties.getMethod().getCheckToken()
                + "?token=" + token.getAccess_token();
        HttpHeaders headers=new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, buildAccessTokenAuthorization(token.getAccess_token()));
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", refreshToken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,headers);
        token = restTemplate.postForObject(url,request,TokenDTO.class);



        log.debug("TokenVO 已刷新: old_token={} , refresh_token={}", oldKey, token);
        redisTemplate.opsForValue().set(oldKey, token, TOKEN_EXP, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(getTokenKey(token), token, TOKEN_EXP, TimeUnit.DAYS);
        return token;
    }


    /**
     * 从redis获取token信息
     *
     * @param token
     * @return
     */
    private TokenDTO getTokenFromRedis(TokenDTO token) {
        String tokenKey = getTokenKey(token);
        TokenDTO tokenVO = (TokenDTO) redisTemplate.opsForValue().get(tokenKey);
        Assert.notNull(tokenVO, "当前登录已失效，请重新获取");
        return tokenVO;
    }

    /**
     * 获取token缓存key
     *
     * @param tokenVO
     * @return
     */
    public String getTokenKey(TokenDTO tokenVO) {
        return TOKEN_PREFIX + tokenVO.getAccess_token();
    }

}
