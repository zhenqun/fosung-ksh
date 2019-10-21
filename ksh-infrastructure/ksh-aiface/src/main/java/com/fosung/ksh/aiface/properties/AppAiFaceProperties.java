package com.fosung.ksh.aiface.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * 腾讯云 - 福生账号 290989609@qq.com （杨东兴）
 *
 * appid: 1258629608
 * SecretId: AKIDBGNFepBaExLUEWhUYVR2d1EE63oIfsBJ
 * SecretKey: NUovma8RaaKzPy7KTIItNd6Nq5xfhU0G
 *
 * 腾讯云 - 好视通账号
 *
 * appid:
 * SecretId: AKIDMyUpEH8yb0KXZNdMimeMJcjhfQyGk9j9
 * SecretKey: kGxgCh15uToV0GPJxPlxuiM2HJ6ey5wY
 *
 * @author toquery
 * @version 1
 */
@Getter
@Setter
@ConfigurationProperties(prefix = AppAiFaceProperties.PREFIX)
public class AppAiFaceProperties {

    public static final String PREFIX = "visual.ai-face";

    private String secretId = "AKIDBGNFepBaExLUEWhUYVR2d1EE63oIfsBJ";
    private String secretKey = "NUovma8RaaKzPy7KTIItNd6Nq5xfhU0G";
    private String region = "ap-guangzhou";

    // 腾讯云face 组ID
    private String groupId = "YanTaiLaiZhouPartyTest";

    private String groupName = "烟台莱州可视化党建";
}
