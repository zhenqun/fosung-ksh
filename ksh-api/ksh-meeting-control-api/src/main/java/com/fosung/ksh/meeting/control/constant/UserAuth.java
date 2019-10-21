package com.fosung.ksh.meeting.control.constant;


import com.fosung.framework.common.support.AppRuntimeDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 好视通用户权限
 *
 * @author wangyh
 * @date 2018/12/01
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum UserAuth implements AppRuntimeDict {

    /**
     * 取消授权
     */
    NOAUTH("0","取消授权"),

    /**
     * 旁听
     */
    HEARER("1","旁听"),

    /**
     * 参会
     */
    ATTENDEE("2","参会"),

    /**
     * 主席
     */
    CHAIRMAN("3","主席");

    String code;
    String remark;

}
