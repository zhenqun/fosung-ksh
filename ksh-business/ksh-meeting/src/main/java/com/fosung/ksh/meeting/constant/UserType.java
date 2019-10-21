package com.fosung.ksh.meeting.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用户类型枚举
 *
 * @author wangyh
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum UserType implements AppRuntimeDict {
    /**
     * 本地用户
     */
    LOCAL("本地用户"),

    /**
     * 灯塔用户
     */
    DT("灯塔用户");
    private String remark;
}
