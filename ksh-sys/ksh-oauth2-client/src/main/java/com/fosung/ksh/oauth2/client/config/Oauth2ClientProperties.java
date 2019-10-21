package com.fosung.ksh.oauth2.client.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 灯塔配置属性
 *
 * @Author : liupeng
 * @Date : 2018-12-04
 * @Modified By
 */
@ConfigurationProperties(prefix = Oauth2ClientProperties.PREFIX)
@Setter
@Getter
public class Oauth2ClientProperties {

    public static final String PREFIX = "ksh.auth2.client";


    private String clientId;

    private String clientSecret;

    private String url;


    private SSOMethod method = new SSOMethod();

    /**
     * SSO方法
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class SSOMethod {

        private String authorizeCode = "/oauth/authorize";

        private String token = "/oauth/token";

        private String principal = "/user/principal";

        private String checkToken = "/oauth/check_token";

        private String logout = "/logout";

    }


}
