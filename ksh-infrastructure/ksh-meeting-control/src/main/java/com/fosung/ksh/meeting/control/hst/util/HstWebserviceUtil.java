package com.fosung.ksh.meeting.control.hst.util;

import com.alibaba.fastjson.JSONObject;
import com.fosung.ksh.common.response.ResponseResult;
import com.fosung.ksh.common.util.UtilWebservice;
import com.fosung.ksh.meeting.control.hst.config.constant.ResultCode;
import com.fosung.ksh.meeting.control.hst.exception.AppHstException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;

/**
 * 调用webservice工具类
 *
 * @author wangyihua
 * @
 */
@Slf4j
public class HstWebserviceUtil {


    /**
     * 调用Webservice接口，并返回调用结果
     *
     * @param wsUrl  ws服务地址
     * @param method 调用的方法
     * @param args   传递的参数
     * @return 返回调用结果
     */
    public static ResponseResult execute(String wsUrl, String method, Object[] args) throws Exception {
        JSON result = UtilWebservice.execute(wsUrl, method, args);
        return parse(result);
    }


    /**
     * 解析返回值
     *
     * @param result
     * @return
     */
    private static ResponseResult parse(JSON result) {
        String jsonStr = result.toString();
        ResponseResult res = JSONObject.parseObject(jsonStr, ResponseResult.class);
        if (!ResultCode.SUCCESS.getCode().equals(res.getCode())) {
            ResultCode hstCodeEnum = ResultCode.valueOf("ERROR_" + res.getCode());
            throw new AppHstException("Hst", res.getCode(), hstCodeEnum, hstCodeEnum.getRemark());
        }
        return res;
    }
}
