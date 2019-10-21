package com.fosung.ksh.duty.config.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 星期一至周日的类型
 */
@AllArgsConstructor
@Getter
public enum WeekType implements AppRuntimeDict {
        MON("周一"),
        TUE("周二"),
        WED("周三"),
        THU("周四"),
        FRI("周五"),
        SAT("周六"),
        SUN("周日");
    private String remark;
}
