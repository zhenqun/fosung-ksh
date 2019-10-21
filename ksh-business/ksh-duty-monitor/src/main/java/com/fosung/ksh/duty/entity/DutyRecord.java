package com.fosung.ksh.duty.entity;


import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.framework.common.support.dao.entity.AppJpaIdEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 签到记录
 */
@Entity
@Table(name = "duty_record")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DutyRecord extends AppJpaIdEntity {// 值班记录实体类

    /**
     * 签到人id
     */
    @Column(name = "duty_people_id")
    private String dutyPeopleId;

    /**
     * 签到时间
     */
    @DateTimeFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    @Column(name = "duty_sign_time")
    private Date dutySignTime;


    /**
     * 签到设备编号
     */
    @Column(name = "index_code")
    private String indexCode;


    /**
     * 海康签到记录ID
     */
    @Column(name = "alarm_id")
    private String alarmId;

    /**
     * 海康签到记录人员ID
     */
    @Column(name = "human_id")
    private String humanId;

}
