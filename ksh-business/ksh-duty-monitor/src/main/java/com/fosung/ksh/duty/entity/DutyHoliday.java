package com.fosung.ksh.duty.entity;


import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.ksh.duty.config.constant.HolidayType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 补充调用第三方接口，日期查询只是每年更新一次
 * 中间修改的日期，无法进行同步，需要再此处进行补充
 * 节假日日期设定
 */
@Entity
@Table(name = "duty_holiday")
@Setter
@Getter
public class DutyHoliday extends AppJpaBaseEntity {

    /**
     * 特殊日期
     */
    @DateTimeFormat(pattern = AppProperties.DATE_PATTERN)
    @Column(name = "holiday_date")
    private Date holidayDate;


    /**
     * 相应数据类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "holiday_type")
    private HolidayType holidayType;

    /**
     * 节假日所属年度
     */
    @Column(name = "holiday_year")
    private Integer holidayYear;
}
