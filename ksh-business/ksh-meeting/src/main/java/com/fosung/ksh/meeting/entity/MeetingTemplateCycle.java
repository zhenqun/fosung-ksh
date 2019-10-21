package com.fosung.ksh.meeting.entity;

import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.ksh.meeting.constant.CycleFlag;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "meeting_template_cycle")
@Data
public class MeetingTemplateCycle extends AppJpaBaseEntity {

    /**
     * 会议室模版Id
     */
    @Column(name = "meeting_template_id")
    private Long meetingTemplateId;


    /**
     * room_type为3时，周例会开始时间 格式：HH:mm:ss；会议室类型为周例会议室时，不允许为空
     */
    @Column(name = "week_start_time")
    private String weekStartTime;


    /**
     * room_type为3时，周例会结束时间 格式：HH:mm:ss；会议室类型为周例会议室时，不允许为空
     */
    @Column(name = "week_end_time")
    private String weekEndTime;


    /**
     * room_type为3时，循环模式1每天，2每周，3每月
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "cycle_flag")
    private CycleFlag cycleFlag;


    /**
     * room_type为3时，每个星期几 6周日，0周一，1周二，2周三，3周四，4周五，5周六（cycleFlag为2时不空）
     */
    @Column(name = "weeks")
    private Integer weeks;


    /**
     * room_type为3时，每月的几号大于零，小于等于31的数值（cycleFlag为3时不空）
     */
    @Column(name = "date_evey_month")
    private Integer dateEveyMonth;
}