package com.fosung.ksh.organization.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.ksh.organization.constant.OrgTemplateType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 模板表实体对象
 * @author wangyh
 */
@Entity
@Table(name = "org_life_template")
@Setter
@Getter
public class OrgLifeTemplate extends AppJpaBaseEntity {

    /**
     * 模板类型1支部模板 2模板规范
     */
    @ApiModelProperty("模板类型1支部模板 2模板规范")
    @Enumerated(EnumType.STRING)
    @Column(name = "template_type")
    private OrgTemplateType templateType;


    /**
     * 模板名称
     */
    @ApiModelProperty("模板名称")
    @Column(name = "title")
    private String title;


    /**
     * 发布时间
     */
    @ApiModelProperty("发布时间")
    @Column(name = "push_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pushTime;


    /**
     * 简介
     */
    @ApiModelProperty("简介")
    @Column(name = "summary")
    private String summary;


    /**
     * 内容
     */
    @Lob
    @ApiModelProperty("内容")
    @Column(columnDefinition="text",name="content")
    private String content;


    /**
     * 组织ID
     */
    @ApiModelProperty("组织ID")
    @Column(name = "org_id")
    private String orgId;


    /**
     * 模板状态 0:未发布 1已发布 支部模板默认已发布
     */
    @ApiModelProperty("模板状态 false:未发布 true已发布")
    @Column(name = "is_push")
    private Boolean isPush = false;

    /**
     * 模板个数
     */
    @Transient
    @ApiModelProperty("模板个数")
    private Integer num;

    @Transient
    @ApiModelProperty("组织名称")
    private String orgName;

    @Transient
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDatetime;

}
