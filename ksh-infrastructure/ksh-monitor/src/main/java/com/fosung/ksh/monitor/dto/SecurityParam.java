package com.fosung.ksh.monitor.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SecurityParam implements Serializable {
    private static final long serialVersionUID = -3930717811650965129L;
    /**
     * appkey
     */
    private String appkey;
    private String port;
    private String ip;
    /**
     * 加密后的appSecret
     */
    private String appSecret;
    /**
     * 时间戳
     */
    private String time;
    /**
     * 加密时间戳
     */
    private String timeSecret;
}
