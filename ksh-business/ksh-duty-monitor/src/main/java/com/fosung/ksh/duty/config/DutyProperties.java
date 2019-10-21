package com.fosung.ksh.duty.config;


import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * 值班监控相关配置
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ksh.duty")
public class DutyProperties {

    /**
     * 人脸库
     */
    private String libId;
    /**
     * 人脸库ID
     */
    private Set<String> libIds = Sets.newHashSet();

    /**
     * 同步哪些区域的设备
     */
    private Set<String> cityCodes = Sets.newHashSet();
}
