package com.fosung.ksh.sys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 项目启动程序
 *
 * @author wangyh
 */
@SpringBootApplication(scanBasePackages = "com.fosung")
@EnableFeignClients(basePackages = "com.fosung")
@EnableEurekaClient
@EnableScheduling
public class SysSyncApplication {
    public static void main(String[] args) {
        SpringApplication.run(SysSyncApplication.class, args);
    }
}
