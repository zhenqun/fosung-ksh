package com.fosung.ksh.punsh.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.*;

/**
 * @date 2019-4-2 13:53
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PunchRecord {


    /**
     * 打卡记录id
     */
    private Long id;

    @Excel(name = "序号", orderNum = "1")
    private Integer index;

    /**
     * 街道
     */
    @Excel(name = "街道（镇）", orderNum = "2", width = 25)
    private String street;
    /**
     * 村
     */
    @Excel(name = "村（社区）", orderNum = "3", width = 25)
    private String community;

    /**
     * 签到状态
     */
    @Excel(name = "签到状态", orderNum = "4", width = 15)
    private String status;

    /**
     * 签到时间
     */
    @Excel(name = "签到时间", orderNum = "5", width = 30)
    private String time;

    /**
     * 值班人
     */
    @Excel(name = "值班人", orderNum = "6", width = 15)
    private String name;

    /**
     * 验证方式
     */
    @Excel(name = "签到方式", orderNum = "7", width = 15)
    private String verification;

    /**
     * 照片记录
     */
    private String photoRecord;

    /**
     * 序列号
     */
    //@Excel(name = "考勤机",orderNum ="8")
    private String sn;

    /**
     * 工号
     */
    @Excel(name = "工号", orderNum = "8", width = 25)
    private String pin;

    private String sMinTime;

    private String xMinTime;

    private String sMaxTime;

    private String xMaxTime;

}
