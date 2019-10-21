package com.fosung.ksh.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 项目启动程序
 *
 * @author wangyh
 */
@EnableCaching
@SpringBootApplication(scanBasePackages = "com.fosung")
@EnableEurekaClient
public class MonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class, args);
    }
}