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
@AppDict("org_template_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum OrgTemplateType implements AppRuntimeDict {

    BRANCH("支部模版"), SPECIFICATION("模版规范");
    private String remark;
}
