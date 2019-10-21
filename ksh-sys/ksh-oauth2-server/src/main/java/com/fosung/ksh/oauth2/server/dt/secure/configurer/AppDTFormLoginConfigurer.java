package com.fosung.ksh.oauth2.server.dt.secure.configurer;

import com.fosung.ksh.oauth2.server.dt.secure.authentication.AppDTUsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 基于灯塔的用户认证配置类
 * @Author : liupeng
 * @Date : 2019-01-06
 * @Modified By
 */
public class AppDTFormLoginConfigurer<H extends HttpSecurityBuilder<H>> extends
        AbstractAuthenticationFilterConfigurer<H, AppDTFormLoginConfigurer<H>, AppDTUsernamePasswordAuthenticationFilter> {

    public AppDTFormLoginConfigurer() {
        super(new AppDTUsernamePasswordAuthenticationFilter(), null);
        usernameParameter("username");
        passwordParameter("password");
    }

    @Override
    public AppDTFormLoginConfigurer<H> loginPage(String loginPage) {

        return super.loginPage(loginPage);
    }

    public AppDTFormLoginConfigurer<H> usernameParameter(String usernameParameter) {
        getAuthenticationFilter().setUsernameParameter(usernameParameter);
        return this;
    }

    public AppDTFormLoginConfigurer<H> passwordParameter(String passwordParameter) {
        getAuthenticationFilter().setPasswordParameter(passwordParameter);
        return this;
    }

    public AppDTFormLoginConfigurer<H> failureForwardUrl(String forwardUrl) {
        failureHandler(new ForwardAuthenticationFailureHandler(forwardUrl));
        return this;
    }

    public AppDTFormLoginConfigurer<H> successForwardUrl(String forwardUrl) {
        successHandler(new ForwardAuthenticationSuccessHandler(forwardUrl));
        return this;
    }

    @Override
    public void init(H http) throws Exception {
        super.init(http);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    private String getUsernameParameter() {
        return getAuthenticationFilter().getUsernameParameter();
    }

    private String getPasswordParameter() {
        return getAuthenticationFilter().getPasswordParameter();
    }


}
