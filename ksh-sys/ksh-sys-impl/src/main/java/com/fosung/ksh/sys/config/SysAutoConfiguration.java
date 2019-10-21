package com.fosung.ksh.sys.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 灯塔web数据获取服务配置
 *
 * @Author : liupeng
 * @Date : 2018-12-07
 * @Modified By
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@Configuration
@EnableConfigurationProperties(SysProperties.class)
public class SysAutoConfiguration {
    SysAutoConfiguration() {

    }

//
//    /**
//     * 自定义异步线程池
//     *
//     * @return
//     */
//    @Bean
//    public AsyncTaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setMaxPoolSize(32);
//        //配置核心线程数
//        executor.setCorePoolSize(16);
//        //配置队列大小
//        executor.setQueueCapacity(99999);
//        //配置线程池中的线程的名称前缀
//        executor.setThreadNamePrefix("async-service-");
//        // 使用预定义的异常处理类
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        return executor;
//    }
}
