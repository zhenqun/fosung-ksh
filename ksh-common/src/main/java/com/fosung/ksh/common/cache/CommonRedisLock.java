package com.fosung.ksh.common.cache;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * redis锁工具类
 *
 * @author wangyihua
 * @date 2019-06-07 13:57
 */
@Component
public class CommonRedisLock {

    private static final Long LOCK_EXPIRE = 1000L * 60; // 默认锁时间 1分钟

    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 分布式锁，默认锁定1分钟
     *
     * @param key key值
     * @return 是否获取到
     */
    public boolean lock(String key) {
        // 利用lambda表达式
        return lock(key, LOCK_EXPIRE);
    }


    /**
     * redis分布式锁
     *
     * @param key
     * @param lockExpire
     * @return
     */
    public boolean lock(String key, Long lockExpire) {
        String lock = key;
        // 利用lambda表达式
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {

            long expireAt = System.currentTimeMillis() + lockExpire + 1;
            Boolean acquire = connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());
            if (acquire) {
                return true;
            } else {

                byte[] value = connection.get(lock.getBytes());
                if (Objects.nonNull(value) && value.length > 0) {
                    long expireTime = Long.parseLong(new String(value));
                    if (expireTime < System.currentTimeMillis()) {
                        // 如果锁已经过期
                        byte[] oldValue = connection.getSet(lock.getBytes(), String.valueOf(System.currentTimeMillis() + lockExpire + 1).getBytes());
                        // 防止死锁
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    /**
     * 删除锁
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

}
