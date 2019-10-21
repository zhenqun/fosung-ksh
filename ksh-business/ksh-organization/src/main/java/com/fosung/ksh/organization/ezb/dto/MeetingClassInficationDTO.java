package com.fosung.ksh.organization.ezb.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 参会人员
 * @author wangyh
 */
@Data
public class MeetingClassInficationDTO implements Serializable {

    private static final long serialVersionUID = -2386505542711652263L;

    /**
     * 主键ID
     */
    private String unionId;

    /**
     * 组织生活ID
     */
    private String meetingId;

    /**
     * 组织生活类型ID
     */
    @NotBlank
    private String classificationId;

    /**
     * 组织生活类型名称
     */
    @NotBlank
    private String classificationName;

}
