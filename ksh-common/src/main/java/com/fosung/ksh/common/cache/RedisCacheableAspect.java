package com.fosung.ksh.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class RedisCacheableAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 缓存切面，
     *
     * @param point
     * @param redisCacheable
     * @return
     * @throws Throwable
     */
    @Around("@annotation(redisCacheable)")
    public Object restoreDataSource(ProceedingJoinPoint point, RedisCacheable redisCacheable) throws Throwable {
        String prefix = redisCacheable.prefix();
        Object args[] = point.getArgs();
        StringBuffer key = new StringBuffer(prefix);
        key.append("^").append(point.getTarget().getClass())
                .append("@").append(point.getSignature().getName());
        if (args != null) {
            for (Object arg : args) {
                key.append("&").append(arg);
            }
        }
        Object obj = redisTemplate.opsForValue().get(key);
        log.debug("缓存命中：key={},value={}",key,obj);
        if (obj == null) {
            obj = point.proceed(args);
            if (obj != null) {
                redisTemplate.opsForValue().set(key, obj, redisCacheable.expired(), redisCacheable.timeUnit());
            }
        }
        return obj;
    }
}
