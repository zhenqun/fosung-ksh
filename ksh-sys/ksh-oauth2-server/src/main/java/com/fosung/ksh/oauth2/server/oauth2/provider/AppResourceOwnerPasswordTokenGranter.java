package com.fosung.ksh.oauth2.server.oauth2.provider;

import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.secure.auth.constant.UserSource;
import com.fosung.framework.common.util.UtilString;
import com.fosung.ksh.oauth2.server.dt.secure.authentication.AppDTAuthenticationProvider;
import com.fosung.ksh.oauth2.server.dt.secure.authentication.AppDTUsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义password认证方式，支持本地用户登录，灯塔用户登录两种方式
 *
 * @author Wang yihua
 * @Date 2019-06-27
 */
public class AppResourceOwnerPasswordTokenGranter extends AbstractTokenGranter {

    private AppDTAuthenticationProvider appDTAuthenticationProvider;

    private static final String GRANT_TYPE = "password";

    private final AuthenticationManager authenticationManager;

    public AppResourceOwnerPasswordTokenGranter(AuthenticationManager authenticationManager,
                                                AuthorizationServerTokenServices tokenServices,
                                                ClientDetailsService clientDetailsService,
                                                OAuth2RequestFactory requestFactory,
                                                AppDTAuthenticationProvider appDTAuthenticationProvider) {
        this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE, appDTAuthenticationProvider);
    }

    protected AppResourceOwnerPasswordTokenGranter(AuthenticationManager authenticationManager,
                                                   AuthorizationServerTokenServices tokenServices,
                                                   ClientDetailsService clientDetailsService,
                                                   OAuth2RequestFactory requestFactory,
                                                   String grantType,
                                                   AppDTAuthenticationProvider appDTAuthenticationProvider) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
        this.appDTAuthenticationProvider = appDTAuthenticationProvider;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String username = parameters.get("username");
        String password = parameters.get("password");
        String user_source = parameters.get("user_source");
        // Protect from downstream leaks of password
        parameters.remove("password");

        Assert.isTrue(UtilString.isNotBlank(user_source), "user_source is null");
        Authentication userAuth = null;
        if (UserSource.LOCAL.name().equals(user_source)) {
            try {
                userAuth = new UsernamePasswordAuthenticationToken(username, password);
                ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
                userAuth = authenticationManager.authenticate(userAuth);
            } catch (AccountStatusException ase) {
                //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
                throw new InvalidGrantException(ase.getMessage());
            } catch (BadCredentialsException e) {
                // If the username/password are wrong the spec says we should send 400/invalid grant
                throw new InvalidGrantException(e.getMessage());
            }
            if (userAuth == null || !userAuth.isAuthenticated()) {
                throw new InvalidGrantException("Could not authenticate user: " + username);
            }


        } else if (UserSource.DT.name().equals(user_source)) {
            userAuth = new AppDTUsernamePasswordAuthenticationToken(username, password);
            userAuth = appDTAuthenticationProvider.authenticate(userAuth);
        } else {
            throw new AppException("unknown user_source");
        }
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
