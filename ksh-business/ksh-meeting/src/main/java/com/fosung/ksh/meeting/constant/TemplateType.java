package com.fosung.ksh.meeting.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import com.fosung.framework.common.support.anno.AppDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 会议室模版类型
 *
 * @author toquery
 * @version 1
 */
@AppDict("template_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum TemplateType implements AppRuntimeDict {

    GENERAL("普通会议室"), CYCLE("循环会议室");
    private String remark;
}
