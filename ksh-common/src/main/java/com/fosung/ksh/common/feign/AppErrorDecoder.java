package com.fosung.ksh.common.feign;

import com.fosung.framework.common.json.JsonMapper;
import com.fosung.ksh.common.feign.exception.AppFeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 自定义feign解码器
 *
 * @author wangyihua
 * @date 2019/4/17
 */
@Slf4j
public class AppErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("\n{}请求失败,\nURL:{},\nRequest：{}，\nResponse：{}",
                methodKey,
                response.request().url(),
                response.request().toString(),
                response.toString());
        Response.Body body = response.body();
        if (body == null) {
            return new AppFeignException(methodKey);
        } else {
            try {
                String resultStr = Util.toString(body.asReader());
                String message = JsonMapper.parseObject(resultStr).getString("message");
                return new AppFeignException("", new StringBuffer().append(response.status()).toString(), message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new AppFeignException("未知异常");
    }
}
