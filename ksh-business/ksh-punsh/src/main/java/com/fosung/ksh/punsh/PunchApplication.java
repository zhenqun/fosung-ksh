package com.fosung.ksh.punsh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 项目启动程序
 *
 * @author lzqun
 */
@SpringBootApplication(scanBasePackages = "com.fosung")
@EnableEurekaClient
public class PunchApplication {

    public static void main(String[] args) {
        SpringApplication.run(PunchApplication.class,args);
    }
}
