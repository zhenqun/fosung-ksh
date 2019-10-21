package com.fosung.ksh.sys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 项目启动程序
 *
 * @author wangyh
 */
//@SpringBootApplication(scanBasePackages = "com.fosung")
@SpringBootApplication(scanBasePackages = {"com.fosung.ksh.sys","com.fosung.ksh.common"})
@EnableFeignClients(basePackages = "com.fosung")
@EnableEurekaClient
public class SysWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(SysWebApplication.class, args);
    }
}
