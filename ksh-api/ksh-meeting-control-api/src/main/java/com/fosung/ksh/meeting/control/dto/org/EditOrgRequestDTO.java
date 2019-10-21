package com.fosung.ksh.meeting.control.dto.org;


import lombok.Data;

/**
 *
 */
@Data
public class EditOrgRequestDTO {

    //组织架构ID
    private Integer departId;

    /**
     * 组织架构名称
     */
    private String departName;


    private Integer parentDepartID;

    private String authDepartID;

    private String authParentDepartID;

}
