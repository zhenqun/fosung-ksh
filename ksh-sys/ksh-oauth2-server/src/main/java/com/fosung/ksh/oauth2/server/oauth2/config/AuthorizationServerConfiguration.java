package com.fosung.ksh.oauth2.server.oauth2.config;

import com.fosung.ksh.oauth2.server.dt.secure.authentication.AppDTAuthenticationProvider;
import com.fosung.ksh.oauth2.server.oauth2.provider.AppResourceOwnerPasswordTokenGranter;
import com.fosung.ksh.oauth2.server.oauth2.store.AppRedisTokenStore;
import com.fosung.ksh.oauth2.server.support.AppSysUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @author: 594781919@qq.com
 * @Date: 2018/4/8 10:48
 * @version: 1.0
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    @Lazy
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    AppSysUserDetailsService userDetailsService;

    @Autowired
    AppDTAuthenticationProvider dtAuthenticationProvider;


    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        // 配置token获取和验证时的策略
        security.
                tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        List<TokenGranter> list = getDefaultTokenGranters(endpoints);
        // 配置tokenStore，保存到redis缓存中
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(new AppRedisTokenStore(redisConnectionFactory))
                .tokenGranter(tokenGranter(list))
                .userDetailsService(userDetailsService);
        ;
    }

    private List<TokenGranter> getDefaultTokenGranters(AuthorizationServerEndpointsConfigurer endpoints) {
        ClientDetailsService clientDetails = endpoints.getClientDetailsService();
        AuthorizationServerTokenServices tokenServices = endpoints.getTokenServices();
        AuthorizationCodeServices authorizationCodeServices = endpoints.getAuthorizationCodeServices();
        OAuth2RequestFactory requestFactory = endpoints.getOAuth2RequestFactory();

        List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetails,
                requestFactory));
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetails, requestFactory));
        ImplicitTokenGranter implicit = new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory);
        tokenGranters.add(implicit);
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory));
        if (authenticationManager != null) {
            tokenGranters.add(new AppResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,
                    clientDetails, requestFactory, dtAuthenticationProvider));
        }
        return tokenGranters;
    }

    private TokenGranter tokenGranter(List<TokenGranter> list) {

        TokenGranter tokenGranter = new TokenGranter() {
            private CompositeTokenGranter delegate;

            @Override
            public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
                if (delegate == null) {
                    delegate = new CompositeTokenGranter(list);
                }
                return delegate.grant(grantType, tokenRequest);
            }
        };

        return tokenGranter;
    }


}
