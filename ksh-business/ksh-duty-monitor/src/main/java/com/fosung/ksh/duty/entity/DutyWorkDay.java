package com.fosung.ksh.duty.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.ksh.duty.config.constant.WeekType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 工作日设置实体对象
 */
@Entity
@Table(name = "duty_work_day")
@Setter
@Getter
public class DutyWorkDay extends AppJpaBaseEntity {
    /**
     * 是否启用
     */
    @Column(name = "is_enable")
    private Boolean isEnable;

    /**
     * 枚举，周一至周天
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "work_day")
    private WeekType workDay;

    /**
     * 名称
     */
    @Transient
    private String workDayName;


    /**
     * 班次id
     */
    @Column(name = "shift_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shiftId;

}
