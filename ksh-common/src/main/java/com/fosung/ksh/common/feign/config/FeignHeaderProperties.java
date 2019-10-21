package com.fosung.ksh.common.feign.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: fosung-ksh
 * @description: fegin请求客户端消息头拦截器配置
 * @author: LZ
 * @create: 2019-09-02 13:47
 **/
@Data
@ConfigurationProperties(prefix = FeignHeaderProperties.PREFIX)
public class FeignHeaderProperties {
    public final static String PREFIX = "feign.header";
    /**
     * 是否开启fegin消息头的拦截器
     */
    private Boolean enable = Boolean.TRUE;
}
