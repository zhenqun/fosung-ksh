package com.fosung.ksh.meeting.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import com.fosung.framework.common.support.anno.AppDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@AppDict("sign")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum Sign  implements AppRuntimeDict {
    /**
     * 签到
     */
    ENABLE("1","是"),

    /**
     * 不
     */
    DISABLE("0","否");

    private String code;
    private String remark;
}