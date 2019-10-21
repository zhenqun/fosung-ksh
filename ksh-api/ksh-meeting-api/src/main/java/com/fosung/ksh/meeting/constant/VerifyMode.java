package com.fosung.ksh.meeting.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import com.fosung.framework.common.support.anno.AppDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 登录校验模型1、用户密码验证；2、会议室密码验证；3、匿名登录
 *
 * @author toquery
 * @version 1
 */
@AppDict("verify_mode")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum VerifyMode implements AppRuntimeDict {


    UP("1", "用户名密码验证"), RP("2", "会议室密码验证"), NONE("3", "匿名登录");

    private String code;
    private String remark;

}
