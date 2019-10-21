package com.fosung.ksh.sys.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysOrg implements Serializable {

    /**
     * 灯塔党组织ID
     */
    private String orgId;

    /**
     * 党组织名称
     */
    private String orgName;


    /**
     * 党组织编码，主要用于递归查询下级
     */
    private String orgCode;

    /**
     * 父级节点
     */
    private String parentId;


    /**
     * 排序字段
     */
    private Integer orderNumber;


    /**
     * 是否有子节点（1：是，0：否）
     */
    private Boolean hasChildren;


    /**
     * 层级
     */
    private Integer level;

}
