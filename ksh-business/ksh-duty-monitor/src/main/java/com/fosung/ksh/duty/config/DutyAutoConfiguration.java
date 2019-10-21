package com.fosung.ksh.duty.config;


import com.fosung.ksh.duty.client.HkiMQClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@EnableConfigurationProperties({DutyProperties.class,HikProperties.class,HkiMQClient.class})
@Configuration
public class DutyAutoConfiguration {
    public DutyAutoConfiguration(){
        log.info("初始化人脸配置");
    }
}