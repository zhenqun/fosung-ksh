package com.fosung.ksh.organization.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 组织生活报备状态，不按照OrgLifeStatus 区分，OrgLifeStatus 正在召开，和结束召开，根据时间转换为BEF，AFT，NORMAL
 *
 * @author toquery
 * @version 1
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum OrgLifeReportStatus implements AppRuntimeDict {


    BEF("之前"), AFT("之后"), NORMAL("正常召开"), UNDO("未召开");
    private String remark;
}
