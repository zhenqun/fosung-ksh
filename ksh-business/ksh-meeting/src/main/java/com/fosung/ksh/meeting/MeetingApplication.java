package com.fosung.ksh.meeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 项目启动程序
 *
 * @author wangyh
 */
@SpringBootApplication(scanBasePackages = {"com.fosung.ksh.meeting","com.fosung.ksh.common"})
@EnableFeignClients(basePackages = {"com.fosung"})
@EnableEurekaClient
@EnableScheduling
@EntityScan(basePackages = "com.fosung.ksh.meeting.entity")
public class MeetingApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeetingApplication.class, args);
    }
}
