package com.fosung.ksh.sys.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 灯塔配置属性
 *
 * @Author : liupeng
 * @Date : 2018-12-04
 * @Modified By
 */
@ConfigurationProperties(prefix = SysProperties.PREFIX)
@Setter
@Getter
public class SysProperties {

    public static final String PREFIX = "ksh.sys";


    private String rootOrgId = "030b9e46-b8ea-47ec-9feb-fb8c3eead801";

    private Urls urls = new Urls();

    /**
     * 灯塔url配置
     */
    @Getter
    @Setter
    public static class Urls {

        // 简项库地址
        private String simpleData = "http://10.254.23.41:8962/simpledata";

    }
}
