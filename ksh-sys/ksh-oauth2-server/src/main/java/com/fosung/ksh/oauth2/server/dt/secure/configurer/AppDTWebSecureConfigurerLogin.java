package com.fosung.ksh.oauth2.server.dt.secure.configurer;

import com.fosung.framework.common.config.AppSecureProperties;
import com.fosung.framework.web.mvc.config.secure.configurer.AppWebSecureConfigurerAdaptor;
import com.fosung.framework.web.mvc.config.secure.configurer.authentication.details.AppAuthenticationDetailsSource;
import com.fosung.framework.web.mvc.config.secure.handler.AppAuthenticationHandlerDelegate;
import com.fosung.ksh.oauth2.server.config.SSOProperties;
import com.fosung.ksh.oauth2.server.dt.secure.authentication.AppDTAuthenticationProvider;
import com.fosung.ksh.oauth2.server.dt.secure.authentication.AppDTAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 灯塔用户登录认证策略
 *
 * @Author : liupeng
 * @Date : 2019-01-06
 * @Modified By
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppDTWebSecureConfigurerLogin extends AppWebSecureConfigurerAdaptor {

    @Autowired
    private AppDTAuthenticationService appDTAuthenticationService;

    @Autowired
    private SSOProperties ssoProperties;

    /**
     * 使用启用登录配置
     *
     * @return
     */
    @Override
    public boolean isEnable() {
        boolean flag = ssoProperties.getDt().getEnable();

        log.info("{}使用灯塔用户名和密码的登录认证", flag ? "" : "不");

        return flag;
    }

    /**
     * 配置认证构造器
     *
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Override
    public void doConfigure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        AppDTAuthenticationProvider dtAuthenticationProvider = new AppDTAuthenticationProvider();

        dtAuthenticationProvider.setAppDTAuthenticationService(appDTAuthenticationService);
        ;

        authenticationManagerBuilder.authenticationProvider(dtAuthenticationProvider);

        log.info("添加灯塔库的认证");
    }

    /**
     * web安全控制
     *
     * @param httpSecurity
     */
    @Override
    public void doConfigure(HttpSecurity httpSecurity) throws Exception {
        AppSecureProperties.AuthConfig authConfig = getAppSecureProperties().getAuth();

        log.info("{} 配置安全策略", getClass().getSimpleName());

        AppAuthenticationHandlerDelegate appAuthenticationHandlerDelegate =
                httpSecurity.getSharedObject(AppAuthenticationHandlerDelegate.class);

        // 登录配置
        AppDTFormLoginConfigurer<HttpSecurity> appDTFormLoginConfigurer = new AppDTFormLoginConfigurer();
        // 应用灯塔登录配置
        httpSecurity.apply(appDTFormLoginConfigurer);

//        log.info("登录灯塔使用自定义的url映射: {}", ssoProperties.getInternet().getLoginUrl());
//        appDTFormLoginConfigurer.loginPage(ssoProperties.getInternet().getLoginUrl());

        appDTFormLoginConfigurer.usernameParameter(authConfig.getLoginUsernameParam())
                .passwordParameter(authConfig.getLoginPasswordParam())
                .loginProcessingUrl(ssoProperties.getDt().getLoginUrl())
                // 设置认证源的构造参数
                .authenticationDetailsSource(new AppAuthenticationDetailsSource())
                .successHandler(appAuthenticationHandlerDelegate)
                .failureHandler(appAuthenticationHandlerDelegate)
                .permitAll();

    }

}
