package com.fosung.ksh.organization.ezb.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 参会人员
 * @author wangyh
 */
@Data
public class MeetingPersonDTO implements Serializable {

    private static final long serialVersionUID = -5100227110908792715L;

    /**
     * 主键ID
     */
    private String unionId;

    /**
     * 组织生活主键
     */
    private String meetingId;

    /**
     * 1: 参加党员 2:缺席党员
     */
    @NotBlank
    private String personnelType;

    /**
     * 党员ID
     */
    @NotBlank
    private String personnelId;

    /**
     * 党员hash
     */
    @NotBlank
    private String personnelHash;

    /**
     * 党员名称
     */
    @NotBlank
    private String personnelName;

    /**
     * 互联网名称
     */
    @NotBlank
    private String interName;






}
