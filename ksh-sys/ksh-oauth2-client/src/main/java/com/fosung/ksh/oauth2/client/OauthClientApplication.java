package com.fosung.ksh.oauth2.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 项目启动程序
 *
 * @author wangyh
 */
@SpringBootApplication(scanBasePackages = "com.fosung")
@EnableFeignClients(basePackages = "com.fosung")
@EnableEurekaClient
public class OauthClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(OauthClientApplication.class, args);
    }
}
