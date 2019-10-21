package com.fosung.ksh.common.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 自动对方法返回数据进行缓存,默认缓存30分钟
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCacheable {

    /**
     * 生成key的前缀
     *
     * @return
     */
    String prefix() default "";

    /**
     * 缓存过期时间，如果为0的话则永不过期,默认为30
     *
     * @return
     */
    int expired() default 30;


    /**
     * 缓存单位，默认为分钟
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;



}
