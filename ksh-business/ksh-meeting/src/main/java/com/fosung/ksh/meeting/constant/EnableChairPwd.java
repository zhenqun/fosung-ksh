package com.fosung.ksh.meeting.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import com.fosung.framework.common.support.anno.AppDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 是否启用管理员密码
 * @author wangyh
 */

@AppDict("enable_chair_pwd")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum EnableChairPwd implements AppRuntimeDict {
    /**
     * 启用
     */
    ENABLE("1","启用"),

    /**
     * 禁用
     */
    DISABLE("0","不启用");

    private String code;
    private String remark;
}