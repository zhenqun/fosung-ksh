package com.fosung.ksh.organization.ezb;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * AppOrgProperties自动配置
 *
 * @author wangyh
 * @date 2018/11/17 11:42
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@Configuration
@EnableConfigurationProperties(EzbProperties.class)
public class EzbAutoConfiguration {
    public EzbAutoConfiguration() {
        log.info("EzbProperties 加载成功");
    }
}
