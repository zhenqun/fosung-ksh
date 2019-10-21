package com.fosung.ksh.common.feign;

import feign.Logger;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign相关配置
 */
@Configuration
public class FeignConfig {

    /**
     * 日志级别
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

//    @Bean
    public Decoder feignDecoder() {
        return new ResponseDecoder();
    }


    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new AppErrorDecoder();
    }
}
