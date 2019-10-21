package com.fosung.ksh.oauth2.server.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 灯塔配置属性
 *
 * @Author : liupeng
 * @Date : 2018-12-04
 * @Modified By
 */
@ConfigurationProperties(prefix = SSOProperties.PREFIX)
@Setter
@Getter
public class SSOProperties {

    public static final String PREFIX = "ksh.sso";

    /**
     * 互联网端DT-SSO配置
     */
    private SSOConfig dt = new SSOConfig(false, "", "", "https://sso.dtdjzx.gov.cn/sso", "/login/dt", new SSOMethod());

    /**
     * 基于灯塔的用户认证
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class SSOConfig {

        private Boolean enable;

        private String clientId;

        private String clientSecret;

        private String url;

        private String loginUrl;

        private SSOMethod method = new SSOMethod();
    }

    /**
     * SSO方法
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class SSOMethod {

        private String token = "/oauth/token";

        private String principal = "/user/principal";

        private String checkToken = "/oauth/check_token";

    }


}
