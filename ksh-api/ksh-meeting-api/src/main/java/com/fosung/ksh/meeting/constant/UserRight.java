package com.fosung.ksh.meeting.constant;


import com.fosung.framework.common.support.AppRuntimeDict;
import com.fosung.framework.common.support.anno.AppDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 好视通返回码
 *
 * @author wangyh
 * @date 2018/12/01
 */
@AppDict("user_right")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum UserRight implements AppRuntimeDict {
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
    CHAIRMAN("3","主席"),

    /**
     * 未授权，说明该人员是通过人脸识别进行参会的
     */
    UNAUTHORIZED("9", "未授权");

    String code;
    String remark;

}
