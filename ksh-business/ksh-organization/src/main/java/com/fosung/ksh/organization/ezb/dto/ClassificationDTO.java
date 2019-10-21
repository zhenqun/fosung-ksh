package com.fosung.ksh.organization.ezb.dto;

import lombok.Data;

/**
 * 组织类型
 * @author wangyh
 */
@Data
public class ClassificationDTO {

    /**
     * 组织类型ID
     */
    private String classificationId;

    /**
     * 组织类型名称
     */
    private String classificationName;
}
