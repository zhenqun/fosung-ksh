package com.fosung.ksh.organization;

import com.fosung.ksh.organization.ezb.EzbProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 项目启动程序
 *
 * @author wangyh
 */
//@Import(EzbProperties.class)
//@SpringBootApplication(scanBasePackages = "com.fosung")
@SpringBootApplication(scanBasePackages = {"com.fosung.ksh.organization","com.fosung.ksh.common"})
@EnableFeignClients(basePackages = "com.fosung.ksh")
@EnableEurekaClient
@EnableAsync
@EnableScheduling
@EntityScan(basePackages = "com.fosung.ksh.organization.entity")
public class OrganizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrganizationApplication.class, args);
    }
}
