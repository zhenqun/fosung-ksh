package com.fosung.ksh.common.feign.exception;

import com.fosung.framework.common.exception.AppException;

/**
 * 接口调用异常处理
 *
 * @author wangyh
 */
public class AppFeignException extends AppException {

    private static final long serialVersionUID = 7098315552108290542L;

    public AppFeignException(String message) {
        super(message);
    }

    public AppFeignException(String appModeCode, String errorCode , String message) {
        super(appModeCode, errorCode, message);
    }

}
