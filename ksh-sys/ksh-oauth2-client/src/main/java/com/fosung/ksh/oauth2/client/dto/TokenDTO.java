package com.fosung.ksh.oauth2.client.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * sso 平台 访问token 信息
 * <pre>
 * {
 *     "access_token": "a7b9deca-6b4d-42ef-acbb-3237cbe541d8",
 *     "token_type": "bearer",
 *     "refresh_token": "828b67f5-a5b8-4f51-a356-e5021dbb5f8c",
 *     "expires_in": 3599,
 *     "scope": "edu.read openid edu.write"
 * }
 * </pre>
 *
 * @author wangyihua
 */
@Data
public class TokenDTO implements Serializable {
    private static final long serialVersionUID = 2817219411029161044L;

    /**
     * 访问token
     */
    private String access_token;

    /**
     * token类型
     */
    private String token_type;

    /**
     * 刷新token
     */
    private String refresh_token;

    /**
     * 过期时间/s
     */
    private Integer expires_in;


    /**
     * token生成时间
     */
    private Long time = System.currentTimeMillis();


}
