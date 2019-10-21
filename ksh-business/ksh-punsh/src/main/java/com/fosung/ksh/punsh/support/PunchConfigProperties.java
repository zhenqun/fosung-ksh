package com.fosung.ksh.punsh.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lqyu
 * @date 2019-3-29 11:30
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "punch.info")
public class PunchConfigProperties {

    private String attlogStamp;

    private String operlogStamp;

    private String attphotoStamp;

    private String errorDelay;

    private String delay;

    private String transTimes;

    private String transInterval;

    private String transFlag;

    private String timeZone;

    private String realtime;

    private String encrypt;
}
