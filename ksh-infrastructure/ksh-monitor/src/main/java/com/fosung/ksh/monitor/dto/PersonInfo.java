package com.fosung.ksh.monitor.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 人员库
 * @author wangyihua
 * @date 2019-05-08 15:52
 */
@ToString
@Data
public class PersonInfo implements Serializable {

    private static final long serialVersionUID = 2327063871690250451L;
    /**
     * birthday : 2011-06-01
     * humanId : c62193a9473d44a8a62f4b6d19cd424a
     * credentialsNum : 330881199011221234
     * facePicUrl : https://open8200.hikvision.com:443/ngx/proxy?i=aHR0cDovLzEwLjMzLjQyLjE2MTo2MTIwL3BpYz89ZDI9aTNmOXoyMzRkczI4OS02NTUwMDgtLTE2MDQzZTFlZjkwYWFpNmI1Kj02ZDdzNyo9N2RwaSo9MWRwaSptMmkxdD0xZTE2MzQtMDBpNDAwKmU1Yjg2ZDE=
     * sex : 1
     * credentialsType : 1
     * listLibId : 2
     * humanName : zhangsan
     */

    private String birthday;
    private String humanId;
    private String credentialsNum;
    private String facePicUrl;
    private Integer sex;
    private Integer credentialsType;
    private String listLibId;
    private String humanName;
    private String picBase64;

}


