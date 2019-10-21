package com.fosung.ksh.aiface.util;

import com.fosung.ksh.aiface.config.constant.TencentResult;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

public class UtilTencentException {
    /**
     *
     * @param e
     * @return
     */
    public static TencentResult resolve(TencentCloudSDKException e) throws TencentCloudSDKException {
        String message = e.getMessage();
        if(message.contains("-")){
            message = message.split("-")[0];
            if(message.contains(".")){
                message = message.split(".")[1];
                return TencentResult.valueOf(message);
            }
        }
        throw e;
    }
}
