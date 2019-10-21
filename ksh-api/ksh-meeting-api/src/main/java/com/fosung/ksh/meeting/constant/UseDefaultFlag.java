package com.fosung.ksh.meeting.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import com.fosung.framework.common.support.anno.AppDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 是否启用默认视频配置
 * @author wangyh
 */
@AppDict("use_default_flag")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum UseDefaultFlag implements AppRuntimeDict {
    /**
     * 启用
     */
    YES("1","启用"),

    /**
     * 禁用
     */
    NO("0","禁用");

    private String code;
    private String remark;
}
