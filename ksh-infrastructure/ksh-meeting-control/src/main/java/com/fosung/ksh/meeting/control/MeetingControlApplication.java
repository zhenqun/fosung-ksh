package com.fosung.ksh.meeting.control;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 项目启动程序
 *
 * @author wangyh
 */
@SpringBootApplication(scanBasePackages = "com.fosung")
//@EnableEurekaClient
//@EnableSwagger2
public class MeetingControlApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeetingControlApplication.class, args);
    }
}
