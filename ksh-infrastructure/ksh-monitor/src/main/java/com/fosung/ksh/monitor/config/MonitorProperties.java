package com.fosung.ksh.monitor.config;


import com.fosung.framework.common.util.UtilString;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 值班监控相关配置
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ksh")
public class MonitorProperties {
    private HikVision hikVision = new HikVision();



    @Getter
    @Setter
    public class HikVision {
        private ArtemisConfig config;
        /**
         * 人脸库ID
         */
        private String listLibId;
        private String ip;
        private String port;





        public String getIp() {
            String[] split = config.getHost().split(":");
            return split[0];
        }

        public String getPort() {
            String[] split = config.getHost().split(":");
            if (UtilString.isNotEmpty(split[1])) {
                return split[1];
            }
            return "443";
        }
    }


}
