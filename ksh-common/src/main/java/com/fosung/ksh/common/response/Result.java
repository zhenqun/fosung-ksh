package com.fosung.ksh.common.response;

import com.fosung.ksh.common.util.AppPage;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * rest请求返回实体类
 *
 * @author wangyh
 */
@Data
public class Result {


    public static ResponseEntity success() {
        return new ResponseEntity(HttpStatus.OK);
    }


    public static <T> ResponseEntity success(T obj) {
        if(obj instanceof Page && obj instanceof List){
            Page p = (Page) obj;
            PageImpl page = new PageImpl(p.getContent(),p.getPageable(),p.getTotalElements());
            return new ResponseEntity<T>((T) page, HttpStatus.OK);
        }
        return new ResponseEntity<T>(obj, HttpStatus.OK);
    }

    /**
     * 返回自定义错误码
     *
     * @param obj
     * @param httpStatus
     * @return
     */
    public static <T> ResponseEntity failure(T obj, HttpStatus httpStatus) {
        return new ResponseEntity<T>(obj, httpStatus);
    }


    /**
     * 返回500错误
     *
     * @param obj
     * @return
     */
    public static <T> ResponseEntity failure(T obj) {
        return new ResponseEntity<T>(obj, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 返回不带错误信息的500错误
     *
     * @return
     */
    public static ResponseEntity failure() {
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}