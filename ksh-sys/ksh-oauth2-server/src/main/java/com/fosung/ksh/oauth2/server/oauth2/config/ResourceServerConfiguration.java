package com.fosung.ksh.oauth2.server.oauth2.config;

import com.fosung.ksh.oauth2.server.oauth2.store.AppRedisTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@Configuration // 加入后 所有资源 需要 访问权限
@EnableResourceServer
@Order(3)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    /**
     * 这里主要设置logout处理配置，在websecurity中配置不起作用，需要在此配置
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/oauth/**", "/doc/**", "/swagger-ui/**").permitAll()// js css 不拦截
                .anyRequest().authenticated()
                .and().logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler())
                .and().csrf().disable().headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and().requestMatchers().antMatchers("/user/principal")
                .and().authorizeRequests().antMatchers("/user/principal")
                .authenticated();
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("resourceId")
                .tokenStore(new AppRedisTokenStore(redisConnectionFactory));
    }

    /**
     * 登出处理器，使用头文件中referer中的uri跳转
     *
     * @return
     */
    @Bean
    LogoutSuccessHandler logoutSuccessHandler() {
        SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
        logoutSuccessHandler.setUseReferer(true);
        logoutSuccessHandler.setTargetUrlParameter("redirect_uri");
        return logoutSuccessHandler;
    }

}
