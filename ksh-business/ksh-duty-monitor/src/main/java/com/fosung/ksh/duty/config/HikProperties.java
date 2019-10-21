package com.fosung.ksh.duty.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "hik")
public class HikProperties {
    private  String host;
    private  String appKey;
    private  String appSecrete;
}
