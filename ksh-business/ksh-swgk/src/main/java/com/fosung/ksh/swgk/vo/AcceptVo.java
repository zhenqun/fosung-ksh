package com.fosung.ksh.swgk.vo;

import lombok.Data;

import java.util.List;

/**
 * @program: fosung-ksh
 * @description: 接受数据
 * @author: LZ
 * @create: 2019-08-20 08:54
 **/
@Data
public class AcceptVo {

    /**
     * 分类集合
     */
    private List<Cate> cateList;

    /**
     * 分类id
     */
    private String  cateId;
    /**
     * 三务公开文章
     */
    private List<ArticleDetails> articleList;
    /**
     * 和上面的属性相同只是接口返回的两个不同属性名称不同
     */
    private List<ArticleDetails> artList;
    /**
     * 便民信息
     */
    private List<Convenience> convenienceList;
    /**
     * 目前是第几页
     */
    private String nowPage;
    /**
     * 总页数
     */
    private String totalCount;
}
