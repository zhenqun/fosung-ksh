package com.fosung.ksh.common.response;

import com.fosung.framework.common.json.JsonMapper;
import lombok.Data;

import java.util.List;

/**
 * rest请求返回实体类
 * @author wangyh
 */
@Data
public class ResponseResult {
    private static final long serialVersionUID = 1L;

    private Boolean success = true;
    private String message;
    private Object data;
    private List datalist;
    private String code;


    /**
     * 请求是否成功
     *
     * @return
     */
    public boolean ok() {
        return success;
    }

    /**
     * 获取返回数据
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T data(Class<T> clazz) {
        String json = JsonMapper.toJSONString(data);
        return JsonMapper.parseObject(json, clazz);
    }

    /**
     * 获取返回数据
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> datalist(Class<T> clazz) {
        String json = JsonMapper.toJSONString(datalist);
        return JsonMapper.parseArray(json, clazz);
    }


    /**
     * 获取返回数据
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> datalist(List list, Class<T> clazz) {
        String json = JsonMapper.toJSONString(list);
        return JsonMapper.parseArray(json, clazz);
    }

}