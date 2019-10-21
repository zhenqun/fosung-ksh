package com.fosung.ksh.oauth2.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 项目启动程序
 *
 * @author wangyh
 */
@EnableFeignClients(basePackages = "com.fosung")
@SpringBootApplication(scanBasePackages = "com.fosung")
@EnableEurekaClient
public class Oauth2ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApplication.class, args);
    }
}
