package com.fosung.ksh.meeting.control.hst.config;


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
@EnableConfigurationProperties(HstProperties.class)
public class HstAutoConfiguration {
    public HstAutoConfiguration() {
        log.info("HstProperties 加载成功");
    }
}
