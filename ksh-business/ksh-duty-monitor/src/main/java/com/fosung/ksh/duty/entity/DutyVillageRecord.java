package com.fosung.ksh.duty.entity;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.support.dao.entity.AppJpaIdEntity;
import com.fosung.ksh.duty.config.constant.DutyType;
import lombok.Data;
import org.assertj.core.util.Lists;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 村级值班记录
 */
@Entity
@Table(name = "duty_village_record")
@Data
public class DutyVillageRecord extends AppJpaIdEntity {// 值班记录实体类

    /**
     * 上午签到人id,现在取当天上午第一个签到人的id
     */
    @Column(name = "duty_people_id")
    private String dutyPeopleId;

    /**
     * 下午签到人id,现在取当天下午第一个签到人的id
     */
    @Column(name = "duty_pm_people_id")
    private String dutyPmPeopleId;

    /**
     * 签到的村
     */
    @Column(name = "village_id")
    private Long villageId;


    /**
     * 上午应签到时间
     */
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    @Column(name = "hope_am_sign_time")
    private Date hopeAmSignTime;

    /**
     * 上午应签退时间
     */
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    @Column(name = "hope_am_sign_off_time")
    private Date hopeAmSignOffTime;

    /**
     * 下午应签到时间
     */
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    @Column(name = "hope_pm_sign_time")
    private Date hopePmSignTime;

    /**
     * 下午应签退时间
     */
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    @Column(name = "hope_pm_sign_off_time")
    private Date hopePmSignOffTime;

    /**
     * 上午签到时间
     */
    @Excel(name = "上午签到时间", orderNum = "3",exportFormat=AppProperties.DATE_TIME_PATTERN,width=20)
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    @Column(name = "am_sign_time")
    private Date amSignTime;
    /**
     * 上午签退时间
     */
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    @Column(name = "am_sign_off_time")
    private Date amSignOffTime;

    /**
     * 下午签到时间
     */
    @Excel(name = "下午签到时间", orderNum = "5",exportFormat=AppProperties.DATE_TIME_PATTERN,width=20)
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    @Column(name = "pm_sign_time")
    private Date pmSignTime;

    /**
     * 下午签退时间
     */
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    @Column(name = "pm_sign_off_time")
    private Date pmSignOffTime;

    /**
     * 签到设备编号
     */
    @Column(name = "index_code")
    private String indexCode;


    /**
     * 值班类型
     */
    @Column(name = "duty_type")
    @Enumerated(EnumType.STRING)
    private DutyType dutyType;


    /**
     * 签到人名称
     */
    @Excel(name = "上午签到人", orderNum = "4")
    @Transient
    private String peopleName;

    /**
     * 下午签到人名称
     */
    @Excel(name = "下午签到人", orderNum = "6")
    @Transient
    private String pmPeopleName;

    /**
     * 所属村级名称
     */
    @Excel(name = "村级名称", orderNum = "1",width=15)
    @Transient
    private String cityName;

    /**
     * 所属镇编码
     */
    @Transient
    private String townCode;

    /**
     * 所属镇名称
     */
    @Excel(name = "所属镇/街道", orderNum = "2",width=12)
    @Transient
    private String townName;

    /**
     * 月度排名
     */
    @Excel(name = "月度排名", orderNum = "10")
    @Transient
    private Integer monthRank = 0;

    /**
     * 在岗时长
     */
    @Excel(name = "在岗时长", orderNum = "9")
    @Transient
    private String onSignTime = "0时0分";
    /**
     * 所有签到人
     */
    @Excel(name = "所有签到人", orderNum = "8",exportFormat=AppProperties.DATE_TIME_PATTERN,width=30)
    @Transient
    private List<String> allSignPeople = Lists.newArrayList();

    /**
     * 值班人
     */
    @Transient
    @Excel(name = "值班人", orderNum = "7",exportFormat=AppProperties.DATE_TIME_PATTERN,width = 25)
    private List<String> schedulePeople = Lists.newArrayList();

}
