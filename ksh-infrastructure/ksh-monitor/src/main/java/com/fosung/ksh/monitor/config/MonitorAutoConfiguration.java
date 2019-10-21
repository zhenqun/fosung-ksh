package com.fosung.ksh.monitor.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@EnableConfigurationProperties({MonitorProperties.class})
@Configuration
public class MonitorAutoConfiguration {
    public MonitorAutoConfiguration(){
        log.info("初始化海康配置");
    }
}