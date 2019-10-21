package com.fosung.ksh.duty.config.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 节假日类型
 */

@AllArgsConstructor
@Getter
public enum HolidayType  {

    WORKINGDAY(0,"工作日"),LEGALHOLIDAYS(1,"法定节假日"),
    HOLIDAYSHIFT(2,"节假日调休补班"),RESTDAY(3,"休息日");


    private Integer index;
    private String remark;
}
