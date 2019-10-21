package com.fosung.ksh.common.dto.handler;

import java.util.Map;

public interface KshDTOCallbackHandler<T> {
    /**
     * 对dto对象的处理
     * 自定义DTO处理，不返回map
     *
     */
    void doHandler(T dto);
}
