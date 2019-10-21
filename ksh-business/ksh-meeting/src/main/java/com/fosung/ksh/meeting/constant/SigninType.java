package com.fosung.ksh.meeting.constant;


import com.fosung.framework.common.support.AppRuntimeDict;
import com.fosung.framework.common.support.anno.AppDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 签到方式
 *
 * @author wangyh
 * @date 2018/12/01
 */
@AppDict("signin_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum SigninType implements AppRuntimeDict {
    /**
     * 取消授权
     */
    FACE(2, "人脸识别"),
    /**
     * 旁听
     */
    LOGIN(1, "终端登录"),
    /**
     * 参会
     */
    ADMININPUT(0, "管理员录入");

    int code;

    String remark;

}
