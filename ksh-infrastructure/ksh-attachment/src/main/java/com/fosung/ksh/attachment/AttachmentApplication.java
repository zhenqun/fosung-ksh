package com.fosung.ksh.attachment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 项目启动程序
 *
 * @author wangyh
 */

@SpringBootApplication(scanBasePackages = {"com.fosung.ksh"})
//@SpringBootApplication(scanBasePackages = {"com.fosung"})
@EnableEurekaClient
@EnableAsync
@EnableScheduling
public class AttachmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttachmentApplication.class, args);
    }
}
