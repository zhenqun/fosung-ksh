package com.fosung.ksh.sys.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * OAUTH2配置客户端
 *
 * @author wangyihua
 * @date 2019-07-18 17:13
 */
@Data
@Entity
@Table(name = "oauth_client_details")
public class OauthClientDetails {


    @Id
    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "resource_ids")
    private String resourceIds;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "scope")
    private String scope;

    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;

    @Column(name = "web_server_redirect_uri")
    private String webServerRedirectUri;

    @Column(name = "authorities")
    private String authorities;

    @Column(name = "access_token_validity")
    private Long accessTokenValidity;

    @Column(name = "refresh_token_validity")
    private Long refreshTokenValidity;

    @Column(name = "additional_information")
    private String additionalInformation;

    @Column(name = "autoapprove")
    private String autoapprove;
}
