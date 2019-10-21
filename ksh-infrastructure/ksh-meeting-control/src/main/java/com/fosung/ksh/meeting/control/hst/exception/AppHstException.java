package com.fosung.ksh.meeting.control.hst.exception;

import com.fosung.framework.common.exception.AppException;
import com.fosung.ksh.meeting.control.hst.config.constant.ResultCode;

/**
 * 好视通接口调用异常处理
 *
 * @author wangyh
 */
public class AppHstException extends AppException {

    private static final long serialVersionUID = 7098315552108290542L;

    private ResultCode resultCode;

    public AppHstException(String message) {
        super(message);
    }

    public AppHstException(String appModeCode, String errorCode, ResultCode resultCode, String message) {
        super(appModeCode, errorCode, message);
        this.resultCode = resultCode;
    }

    public ResultCode getHstCodeEnum() {
        return resultCode;
    }
}
