package com.fosung.ksh.duty.entity;


import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 值班班次设定
 */
@Entity
@Table(name = "duty_shift")
@Setter
@Getter
public class DutyShift extends AppJpaBaseEntity {

    /**
     * 班次名称
     */
    @Column(name = "shift_name")
    private String shiftName;

    /**
     * 行政区划id
     */
    @Column(name = "area_id")
    private Long areaId;


    /**
     * 上午签到时间
     */
    @Column(name = "morning_start_time")
    private String morningStartTime = "08:30";


    /**
     * 上午签退时间
     */
    @Column(name = "morning_end_time")
    private String morningEndTime = "11:30";


    /**
     * 下午签到时间
     */
    @Column(name = "afternoon_start_time")
    private String afternoonStartTime = "14:00";


    /**
     * 下午签退时间
     */
    @Column(name = "afternoon_end_time")
    private String afternoonEndTime = "17:30";

    /**
     * 法定节假日是否值班
     */
    @Column(name = "exclude_holiday")
    private Boolean excludeHoliday = false;


    /**
     * 特殊日期设置
     */
    @Transient
    private List<DutySpecialDay> specialDayList = Lists.newArrayList();


    /**
     * 工作日设置
     */
    @Transient
    private List<DutyWorkDay> workDayList = Lists.newArrayList();


}
