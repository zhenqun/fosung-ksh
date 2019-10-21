package com.fosung.ksh.organization.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 模版类型
 *
 * @author toquery
 * @version 1
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum OrgLifeStatus implements AppRuntimeDict {
    // 组织生活的召开状态
    GOING("进行中"),
    FINISHED("已结束");
    private String remark;
}
