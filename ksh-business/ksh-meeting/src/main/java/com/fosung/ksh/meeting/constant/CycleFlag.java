package com.fosung.ksh.meeting.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import com.fosung.framework.common.support.anno.AppDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 循环模式1每天，2每周，3每月
 * @author wangyh
 */
@AppDict("cycle_flag")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum CycleFlag implements AppRuntimeDict {
    /**
     * 启用
     */
    DAILY("1","每天"),
    /**
     * 启用
     */
    WEEKLY("2","每周"),
    /**
     * 禁用
     */
    MONTHLY("3","每月");

    private String code;
    private String remark;
}