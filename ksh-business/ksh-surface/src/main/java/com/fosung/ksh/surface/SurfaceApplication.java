package com.fosung.ksh.surface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 项目启动程序
 *
 * @author wangyh
 */
@SpringBootApplication(scanBasePackages = "com.fosung.ksh.surface")
@EnableFeignClients(basePackages = "com.fosung")
@EnableSwagger2
@EnableEurekaClient
public class SurfaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SurfaceApplication.class, args);
    }
}
