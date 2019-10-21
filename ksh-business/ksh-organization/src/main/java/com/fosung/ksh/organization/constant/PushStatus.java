package com.fosung.ksh.organization.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import com.fosung.framework.common.support.anno.AppDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 模版类型
 *
 * @author toquery
 * @version 1
 */
@AppDict("push_status")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum PushStatus implements AppRuntimeDict {
    PUSH("已发布"), NOTPUSH("未发布");
    private String remark;
}
