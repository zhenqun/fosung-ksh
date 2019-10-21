package com.fosung.ksh.common.util;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;

import java.util.List;

/**
 * @author wangyihua
 * @date 2019-05-10 17:23
 */
public class UtilPage {

    /**
     * 对list进行分页
     *
     * @param list
     * @param pageNumber
     * @param pageSize
     * @param <T>
     * @return
     */
    public static <T> Page<T> page(List<T> list, int pageNumber, int pageSize) {

        PageRequest pageAble = PageRequest.of(pageNumber, pageSize);
        AppPage<T> page = new AppPage<T>(list, pageAble, list.size());
        return page;
    }
}
