package com.fosung.ksh.aiface.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = AiFaceProperties.PREFIX)
@Data
@Slf4j
public class AiFaceProperties {

    /**
     * 配置信息的前缀
     */
    public static final String PREFIX = "ksh.aiface";

    /**
     * 腾讯云id
     */
    private String secretId = "AKIDBGNFepBaExLUEWhUYVR2d1EE63oIfsBJ";

    /**
     * 腾讯云KEY
     */
    private String secretKey = "NUovma8RaaKzPy7KTIItNd6Nq5xfhU0G";

    /**
     *
     */
    private String region = "ap-guangzhou";

    /**
     * 人脸组
     */
    private String groupId = "YanTaiLaiZhouPartyTest";

    /**
     * 人脸组名称
     */
    private String groupName = "烟台莱州可视化党建";

}
