package com.fosung.ksh.meeting.control.hst.config.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 会议室状态
 *
 * @author wangyh
 * @date 2018/12/01
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum SerachType implements AppRuntimeDict {
    LR("0","前后模糊搜索"),
    ALL("1","全匹配");

    String code;
    String remark;

}