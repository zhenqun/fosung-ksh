package com.fosung.ksh.oauth2.server.dt.secure.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

public class AppDTAuthenticationProvider implements AuthenticationProvider, InitializingBean {

    @Setter
    @Getter
    private AppDTAuthenticationService appDTAuthenticationService ;

    @Setter
    @Getter
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public AppDTAuthenticationProvider() {
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (AppDTUsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        AppDTUsernamePasswordAuthenticationToken appDTUsernamePasswordAuthenticationToken = (AppDTUsernamePasswordAuthenticationToken)authentication ;

        UserDetails loadedUser = null ;

        try {
            loadedUser = appDTAuthenticationService.loadUserByUsername( appDTUsernamePasswordAuthenticationToken.getPrincipal().toString() ,
                    authentication.getCredentials().toString() );

            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "appDTAuthenticationService returned null, which is an interface contract violation");
            }

            return createSuccessAuthentication( loadedUser , authentication , loadedUser );
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    protected Authentication createSuccessAuthentication(Object principal,
                                                         Authentication authentication, UserDetails user) {

        AppDTUsernamePasswordAuthenticationToken result = new AppDTUsernamePasswordAuthenticationToken(
                principal, authentication.getCredentials() ,
                authoritiesMapper.mapAuthorities(user.getAuthorities())) ;

        result.setDetails(authentication.getDetails());

        return result;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.appDTAuthenticationService, "A appDTAuthenticationService must be set");
    }
}
