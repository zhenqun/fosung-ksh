package com.fosung.ksh.monitor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.config.AppProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *  获取具体签到人员信息列表请求实体类
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonRecordInfo implements Serializable {

     /**
      * age : 35
      * alarmId : 60736
      * alarmTime : 2018-06-22 10:19:34
      * bkgPicUrl : /ngx/proxy?i=aHR0cDovLzEwLjY3LjE4Ny4zNzo2NTAxL3BpYz89ZDYzaTk2MWUqN2UwaTAyYi02MTNiMGEtOTQwOThhNmMyNWUxNGFpNGIzKj0xZDlzMSo9NGRwaSo9MWQ4aTV0MnBlKm01aTE2PS0xMjUwMzAtMzB6NTcycz04ZDA4ZDk=
      * cameraName : 深眸全局80
      * controlId : 15
      * ethnic : 1
      * facePicUrl : /ngx/proxy?i=aHR0cDovLzEwLjY3LjE4Ny4zNzo2NTAxL3BpYz89ZDQ9aTk2MXowN2UwczA1Yi02MzYxMDVtMmVwPXQ1aTdkKj0qMnBkaT0qMXM5aTE9NDNiMWlhZDFlKjJjNGE4NDA0NS1hNmIzOTkyLTE1MzEqMC1kMGkxNzJkNWU4NA==
      * glass : 1
      * humans : [{"address":"杭州市","age":0,"credentialsType":0,"ethnic":0,"facePicUrl":"/ngx/proxy?i=aHR0cDovLzEwLjY3LjE4Ny4zNzo2NTAxL3BpYz8xZGQyNDRpOGQtZSo1NzUxMzYxMDNiMmEtLTQwOThhNmMyNWUxNGFpNGIzKj0zZDhzNCo9MGRwaSo9MWQ2aTB0MnBlKm01aTYtPTkyMDBlMzN6YjY5cz03aTcyPQ==","humanId":"1fc25748a3004caa9bff717890349e8d","humanName":"而非人","listLibId":"ff80808163ce9af20163d2ddb7c40004","listLibName":"hypbuild4test","nation":0,"sex":0,"similarity":0.04634600132703781,"smile":0,"credentialsNum":"1231232"}]
      * indexCode : 32010100001310991622
      * sex : 2
      * smile : 2
      */
     private Integer age;
     private String alarmId;

     @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
     private Date alarmTime;

     private String bkgPicUrl;
     private String cameraName;
     private Integer controlId;
     private Integer ethnic;
     private String facePicUrl;
     private Integer glass;
     private String indexCode;
     private Integer sex;
     private Integer smile;
     private List<PersonInfo> humans;


}
