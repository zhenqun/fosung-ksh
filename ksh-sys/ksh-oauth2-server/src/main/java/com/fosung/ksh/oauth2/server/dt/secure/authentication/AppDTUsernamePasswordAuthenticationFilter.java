package com.fosung.ksh.oauth2.server.dt.secure.authentication;

import com.fosung.framework.web.util.UtilWeb;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义登录页面的灯塔用户登录认证
 * @Author : liupeng
 * @Date : 2019-01-06
 * @Modified By
 */
public class AppDTUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if ( !UtilWeb.isPostRequest( request )) {
            throw new AuthenticationServiceException(
                    "DT Authentication method not supported: " + request.getMethod());
        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        username = username.trim();

        AppDTUsernamePasswordAuthenticationToken authRequest = new AppDTUsernamePasswordAuthenticationToken(
                username, password);

        // 设置详情数据
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request,
                              AppDTUsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails( authenticationDetailsSource.buildDetails(request) ) ;
    }

}
