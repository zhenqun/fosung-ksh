package com.fosung.ksh.common.feign.interceptor;

import com.fosung.ksh.common.feign.config.FeignHeaderProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Feign 统一 cookie 拦截 器
 *
 * @author wangyh
 */
@Component
public class FeignHeaderInterceptor implements RequestInterceptor {
    @Autowired
    private FeignHeaderProperties feignHeaderProperties;

    /**
     * 将获取到的header的信息向下传递
     *
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (feignHeaderProperties.getEnable()){
            if (null == getHttpServletRequest()) {
                return;
            }
            HttpServletRequest request = getHttpServletRequest();
            Enumeration<?> headerEnum = request.getHeaderNames();
            while (headerEnum.hasMoreElements()) {
                String key = (String) headerEnum.nextElement();
                if (!key.equalsIgnoreCase("Content-Type")) {
                    String value = request.getHeader(key);
                    requestTemplate.header(key, value);
                }
            }
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

