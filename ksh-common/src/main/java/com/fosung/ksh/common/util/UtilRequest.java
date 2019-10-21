package com.fosung.ksh.common.util;

import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.json.JsonMapper;
import feign.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public class UtilRequest {

    /**
     * 获取真正的字符串值,将字符串null置为真正的null
     *
     * @param username
     * @param id
     * @return
     */
    public static Set<String> keys(HttpServletRequest request) throws IOException {
        String bodyStr = readAsChars(request);
        JSONObject json = JsonMapper.parseObject(bodyStr);
        return json.keySet();
    }

    // 字符串读取
    // 方法一
    public static String readAsChars(HttpServletRequest request) throws IOException {
        Reader reader = request.getReader();
        if(reader != null){
            return Util.toString(request.getReader());
        }
        return null;
    }
}
