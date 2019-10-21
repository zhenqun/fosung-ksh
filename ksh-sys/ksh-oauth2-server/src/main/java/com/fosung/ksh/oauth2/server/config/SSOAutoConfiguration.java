package com.fosung.ksh.oauth2.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 灯塔web数据获取服务配置
 *
 * @Author : liupeng
 * @Date : 2018-12-07
 * @Modified By
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@Configuration
@EnableConfigurationProperties(SSOProperties.class)
public class SSOAutoConfiguration {

    SSOAutoConfiguration() {

    }

}
