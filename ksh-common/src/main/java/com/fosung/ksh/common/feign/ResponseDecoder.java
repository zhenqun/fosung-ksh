package com.fosung.ksh.common.feign;

import com.alibaba.fastjson.JSONObject;
import com.fosung.ksh.common.feign.exception.AppFeignException;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * 自定义feign解码器
 *
 * @author wangyihua
 * @date 2019/4/17
 */
@Slf4j
public class ResponseDecoder implements Decoder {

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        Response.Body body = response.body();
        if (body == null) {
            return null;
        } else {
            String resultStr = Util.toString(body.asReader());
            return JSONObject.parseObject(resultStr, type);

        }
    }


    /**
     * 获取真实的返回值类型，适应用于范型处理
     *
     * @param type
     * @return
     */
    private Class getRealClass(Type type) {
        //返回值类型
        Class resultClass = null;
        try {
            //如果带范型
            if (type instanceof ParameterizedType) {
                // 第一步，获取真实的返回类型
                ParameterizedType ptType = (ParameterizedType) type;
                resultClass = Class.forName(ptType.getRawType().getTypeName());

                //判定是否是集合，如果是集合，从datalist获取数据
                if (Collection.class.isAssignableFrom(resultClass)) {
                    Type realType = ptType.getActualTypeArguments()[0];
                    return getRealClass(realType);
                } else {
                    resultClass = Class.forName(ptType.getRawType().getTypeName());
                }
            } else {
                resultClass = Class.forName(type.getTypeName());
            }


        } catch (ClassNotFoundException e) {
            throw new AppFeignException("ResponseDecoder", "500", e.getMessage());
        }

        return resultClass;
    }

    /**
     * 判定是否是集合
     *
     * @param type
     * @return
     */
    private boolean isCollection(Type type) {
        //判定是否是集合，如果是集合，从datalist获取数据
        try {
            if (type instanceof ParameterizedType) {
                // 第一步，获取真实的返回类型
                ParameterizedType ptType = (ParameterizedType) type;
                return Collection.class.isAssignableFrom(Class.forName(ptType.getRawType().getTypeName()));

            }
        } catch (ClassNotFoundException e) {
            throw new AppFeignException("ResponseDecoder", "500", e.getMessage());
        }
        return false;
    }
}
