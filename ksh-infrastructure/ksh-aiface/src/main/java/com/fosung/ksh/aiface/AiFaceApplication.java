package com.fosung.ksh.aiface;

import com.fosung.ksh.aiface.config.AiFaceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * 项目启动程序
 *
 * @author wangyh
 */
@Import(AiFaceProperties.class)
@SpringBootApplication(scanBasePackages = "com.fosung")
@EnableFeignClients
@EnableEurekaClient
public class AiFaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiFaceApplication.class, args);
    }
}
