package com.fosung.ksh.common.dto;

import com.fosung.framework.common.dto.UtilDTO;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.ksh.common.dto.handler.KshDTOCallbackHandler;

import java.util.List;

public class DtoUtil extends UtilDTO {
    /**
     * 批量转换对象为dto
     *
     * @param objects
     * @param dtoCallbackHandler
     * @param <T>
     * @return
     */
    public static <T> void handler(List<T> objects,
                                   KshDTOCallbackHandler dtoCallbackHandler) {
        if (UtilCollection.isEmpty(objects)) {
            return;
        }

        objects.stream().forEach(obj -> {
            dtoCallbackHandler.doHandler(obj);
        });
    }
}
