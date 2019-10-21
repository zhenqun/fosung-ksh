package com.fosung.ksh.common.feign.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @program: fosung-ksh
 * @description: fegin拦截消息头的配置文件
 * @author: LZ
 * @create: 2019-09-02 13:56
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties(value = {FeignHeaderProperties.class})
public class FeginHeaderConfiguration {
    public FeginHeaderConfiguration(){
        log.info("FeignHeaderProperties ...加载。。。");
    }
}
