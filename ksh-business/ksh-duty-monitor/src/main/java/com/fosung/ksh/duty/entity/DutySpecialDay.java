package com.fosung.ksh.duty.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 特殊日期设置实体对象
 */
@Entity
@Table(name = "duty_special_day")
@Setter
@Getter
public class DutySpecialDay extends AppJpaBaseEntity {

    /**
     * 特殊日期
     */
    @DateTimeFormat(pattern = AppProperties.DATE_PATTERN)
    @Column(name = "special_date")
    private Date specialDate;


    /**
     * 是否考勤
     */
    @Column(name = "is_duty")
    private Boolean isDuty;


    /**
     * 班次id
     */
    @Column(name = "shift_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shiftId;

}
