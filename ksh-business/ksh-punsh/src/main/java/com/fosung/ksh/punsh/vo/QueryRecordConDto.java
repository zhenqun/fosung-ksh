package com.fosung.ksh.punsh.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;


/**
 * @date 2019-4-4 13:48
 */
@Data
@ToString
public class QueryRecordConDto {
    
    
    /**
     *村(社区)
     */
    private String community;
    
    /**
     *签到状态
     */
    private String status;
    
    /**
     *值班人
     */
    private String userName;

    /**
     *查询使用日期
     */
    private String dateTime;


    /**
     * 接收的日期
     */
    private Date dateTime2;

    /**
     *行政机构code
     */
    private String orgCode;


    /**
     * 判断是区,镇,村
     * 1.区
     * 2.镇
     * 3.村
     * sql地方使用
     */
    private String flag;




}
