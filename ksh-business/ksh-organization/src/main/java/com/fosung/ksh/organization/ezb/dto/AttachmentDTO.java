package com.fosung.ksh.organization.ezb.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 附件
 * @author wangyh
 */
@Data
public class AttachmentDTO implements Serializable {

    private static final long serialVersionUID = 8559092624405999119L;

    /**
     * 主键ID
     */
    private String attachmentId;

    /**
     * 附件地址
     */
    @NotBlank
    private String attachmentAddr;

    /**
     * 类型主键ID
     */
    private String belongTypeId;

    /**
     * 附件类型
     */
    private String attachmentType;

    /**
     * 文件名称
     */
    @NotBlank
    private String attachmentRelName;


    /**
     * 是否已经上传至好视通文件服务器
     */
    private Boolean sync = false;
}
