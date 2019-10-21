package com.fosung.ksh.attachment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 附件持久化存储对象
 *
 * @Author : liupeng
 * @Date : 2018-11-05
 * @Modified By
 */
@Entity
@Table(name = "sys_attachment")
@Data
public class SysAttachment extends AppJpaBaseEntity {
    /**
     * 业务id
     */
    @ApiModelProperty("业务ID")
    @Column(name = "business_id")
    private String businessId;

    /**
     * 业务属性名称
     */
    @ApiModelProperty("业务名称")
    @Column(name = "business_name")
    private String businessName;

    /**
     * 业务类型
     */
    @ApiModelProperty("业务类型")
    @Column(name = "type")
    private String type;

    /**
     * 原名称
     */
    @ApiModelProperty("文件名称")
    @Column(name = "origin_name")
    private String originName;

    /**
     * 存储名称
     */
    @ApiModelProperty("转换后存储的名称")
    @JsonIgnore
    @Column(name = "storage_name")
    private String storageName;

    /**
     * 存储路径
     */
    @ApiModelProperty("存储路径")
    @JsonIgnore
    @Column(name = "storage_path")
    private String storagePath;

    /**
     * 大小
     */
    @ApiModelProperty("文件大小，单位B")
    @Column(name = "size")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long size;

    /**
     * 文件后缀
     */
    @ApiModelProperty("文件扩展名")
    @Column(name = "extension")
    private String extension;

    /**
     * 下载路径
     */
    @ApiModelProperty("下载地址")
    @Column(name = "download_path")
    private String downloadPath;

    /**
     * 排序号
     */
    @ApiModelProperty("排序")
    @Column(name = "sort_number")
    private int sortNumber;



    /**
     * 文件后缀
     */
    @ApiModelProperty("扩展字段，应付后期业务扩充")
    @Column(name = "extension_field1")
    private String extensionField1;

    /**
     * 文件后缀
     */
    @ApiModelProperty("扩展字段，应付后期业务扩充")
    @Column(name = "extension_field2")
    private String extensionField2;

    /**
     * 文件后缀
     */
    @ApiModelProperty("扩展字段，应付后期业务扩充")
    @Column(name = "extension_field3")
    private String extensionField3;

    /**
     * 文件后缀
     */
    @ApiModelProperty("扩展字段，应付后期业务扩充")
    @Column(name = "extension_field4")
    private String extensionField4;

//    /**
//     * 文件上传结果
//     */
//    @ApiModelProperty("文件上传结果")
//    private Boolean result = true;
//
//
//    /**
//     * 文件上传返回信息
//     */
//    @ApiModelProperty("文件上传返回信息")
//    private String resultMessage;

}

