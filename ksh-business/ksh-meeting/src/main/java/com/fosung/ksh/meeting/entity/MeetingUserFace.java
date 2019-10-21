package com.fosung.ksh.meeting.entity;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fosung.framework.common.support.dao.entity.AppJpaIdEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 人脸采集结果
 */
@Data
@Entity
@Table(name = "sync_face")
public class MeetingUserFace extends AppJpaIdEntity {

    /**
     * 身份证号
     */
    @ApiModelProperty("身份证号")
    @Excel(name = "身份证号", orderNum = "0")
    @Column(name = "id_card")
    private String idCard;

    /**
     * 人员姓名
     */
    @ApiModelProperty("人员姓名")
    @Excel(name = "姓名", orderNum = "1")
    @Column(name = "person_name")
    private String personName;

    /**
     * 人员HASH
     */
    @ApiModelProperty("人员HASH")
    @Column(name = "user_hash")
    private String userHash;

    /**
     * 人脸地址URL
     */
    @ApiModelProperty("人脸地址URL")
    @Excel(name = "人脸地址", orderNum = "2")
    @Column(name = "image_url")
    private String imageUrl;


    @ApiModelProperty("上传结果")
    @Excel(name = "上传结果", orderNum = "3")
    @Transient
    private Boolean result;


    @ApiModelProperty("上传结果")
    @Excel(name = "上传结果", orderNum = "4")
    @Transient
    private String message;

}
