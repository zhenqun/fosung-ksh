package com.fosung.ksh.swgk.vo;

import lombok.Data;

/**
 * 三务公开简要信息（用于左边栏的展示）
 */
@Data
public class ArticleSimple {

    private ArticleDetails article;//当前公开信息详情

    private String cateName;//所属目录名称

    private String cateId;//所属目录id

    private String cateParentId;//所属目录上级目录id

    private String swType;//当前公开信息类型 党务、村务、财务

}
