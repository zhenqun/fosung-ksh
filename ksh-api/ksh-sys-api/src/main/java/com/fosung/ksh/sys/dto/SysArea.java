package com.fosung.ksh.sys.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.ksh.sys.client.constant.AreaType;
import lombok.Data;

import java.io.Serializable;


/**
 * @author LZ
 * @Description: 行政区划
 * @date 2019-05-09 19:58
 */
@Data
public class SysArea implements Serializable {
    private static final long serialVersionUID = 572958808943206161L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long areaId;

    /**
     * 区域编码
     */
    private String areaCode;


    /**
     * 区域名称
     */
    private String areaName;


    /**
     * 区域类型，国、省、市、县、镇、村
     */
    private AreaType areaType;


    /**
     * 父级城市代码
     */
    private Long parentId;


    /**
     * 合并城市名称
     */
    private String mergerName;

    /**
     * 父级城市名称
     */
    private String parentName;


    /**
     * 树形深度
     */
    private Integer depth;


    /**
     * 经度
     */
    private String longitude;


    /**
     * 维度
     */
    private String latitude;


    /**
     * 是否有子节点（1：是，0：否）
     */
    private Boolean hasChildren;

    /**
     * 是否启用
     */
    private Boolean isUse;


}
