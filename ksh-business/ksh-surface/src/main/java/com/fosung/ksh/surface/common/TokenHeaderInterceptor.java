package com.fosung.ksh.surface.common;

import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.util.UtilString;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Feign 统一 cookie 拦截 器
 *
 * @author wangyh
 */
@Slf4j
@Component
public class TokenHeaderInterceptor implements RequestInterceptor {
    private static final String COOKIE_KEY = "_cs";

    @Autowired
    AppProperties appProperties;

    /**
     * 将获取到的header的信息向下传递
     *
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (null == getHttpServletRequest()) {
            return;
        }
        HttpServletRequest request = getHttpServletRequest();
        String token = null;
        Cookie[] cookies = getHttpServletRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_KEY.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        log.debug("获取到的token:" + token);
        if (UtilString.isNotBlank(token)) {
            requestTemplate.header("X-Token", token);
        }

    }

    /**
     * 获取request
     *
     * @return
     */
    private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }
}

