package com.fosung.ksh.oauth2.server.dt.secure.configurer;

import com.fosung.ksh.oauth2.server.dt.secure.authentication.AppDTAuthenticationProvider;
import com.fosung.ksh.oauth2.server.dt.secure.authentication.AppDTAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 灯塔web安全自动配置
 * @Author : liupeng
 * @Date : 2018-12-05
 * @Modified By
 */
@Slf4j
@Configuration
@Order(90)
public class AppDTWebSecureAutoConfiguration {

    /**
     * 灯塔用户认证服务
     * @return
     */
    @ConditionalOnMissingBean(AppDTAuthenticationService.class)
    @Bean
    public AppDTAuthenticationService appDTAuthenticationService(){
        return new AppDTAuthenticationService() ;
    }


    @ConditionalOnMissingBean(AppDTAuthenticationProvider.class)
    @Bean
    public AppDTAuthenticationProvider dtAuthenticationProvider(AppDTAuthenticationService appDTAuthenticationService) throws Exception {
        AppDTAuthenticationProvider dtAuthenticationProvider = new AppDTAuthenticationProvider();

        dtAuthenticationProvider.setAppDTAuthenticationService(appDTAuthenticationService);
        ;


        return dtAuthenticationProvider;
    }

    @Bean
    public AppDTWebSecureConfigurerLogin appDTWebSecureConfigurerLogin(){
        return new AppDTWebSecureConfigurerLogin() ;
    }


}
