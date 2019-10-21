package com.fosung.ksh.common.util;

import com.fosung.framework.common.util.UtilCollection;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author wangyihua
 * @date 2019-05-11 21:58
 */

public class AppPage<T> extends PageImpl<T> {
    private List<T> allList;

    public AppPage(List<T> content, Pageable pageable, long total) {
        super(getContentList(content, pageable.getPageNumber(), pageable.getPageSize()),
                pageable, total);
        allList = content;
    }

    public List<T> getAllList() {
        return allList;
    }

    private static <T> List<T> getContentList(List<T> list, int pageNumber, int pageSize) {
        if(UtilCollection.isEmpty(list)){
            return list;
        }
        int fromIndex = pageNumber * pageSize;
        int toIndex = (pageNumber + 1) * pageSize;
        toIndex = toIndex > list.size() ? list.size()  : toIndex;
        return list.subList(fromIndex, toIndex);
    }


}
