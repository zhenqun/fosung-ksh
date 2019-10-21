package com.fosung.ksh.duty.config.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 值班类型
 */

@AllArgsConstructor
@Getter
public enum DutyType {

    OVERTIME("加班"),
    DUTY("正常值班");
    private String remark;
}
