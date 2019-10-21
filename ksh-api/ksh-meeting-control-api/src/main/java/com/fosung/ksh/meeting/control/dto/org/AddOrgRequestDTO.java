package com.fosung.ksh.meeting.control.dto.org;


import lombok.Data;

/**
 *
 */
@Data
public class AddOrgRequestDTO {

    /**
     * 组织架构名称
     */
    private String departName;


    private Integer parentDepartID;

    private String authDepartID;

    private String authParentDepartID;

}
