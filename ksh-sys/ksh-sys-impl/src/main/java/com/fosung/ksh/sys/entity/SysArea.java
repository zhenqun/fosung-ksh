package com.fosung.ksh.sys.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.ksh.sys.entity.constant.AreaType;
import lombok.Data;

import javax.persistence.*;

/**
 * 行政区划
 */
@Data
@Entity
@Table(name = "sys_area")
public class SysArea extends AppJpaBaseEntity {


    /**
     * 区域编码
     */

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "area_id")
    private String areaId;


    /**
     * 区域编码
     */
    @Column(name = "area_code")
    private String areaCode;


    /**
     * 区域名称
     */
    @Column(name = "area_name")
    private String areaName;


    /**
     * 区域类型，国、省、市、县、镇、村
     */
    @Column(name = "area_type")
    @Enumerated(EnumType.STRING)
    private AreaType areaType;


    /**
     * 父级城市代码
     */
    @Column(name = "parent_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;


    /**
     * 合并城市名称
     */
    @Column(name = "merger_name")
    private String mergerName;

    /**
     * 父级城市名称
     */
    @Transient
    private String parentName;


    /**
     * 树形深度
     */
    @Column(name = "depth")
    private Integer depth;


    /**
     * 经度
     */
    @Column(name = "longitude")
    private String longitude;


    /**
     * 维度
     */
    @Column(name = "latitude")
    private String latitude;


    /**
     * 是否有子节点（1：是，0：否）
     */
    @Column(name = "has_children")
    private Boolean hasChildren;

    /**
     * 是否启用
     */
    @Column(name = "is_use")
    private Boolean isUse;

}
