package com.fosung.ksh.meeting.control.hst.request.org;


import com.fosung.ksh.meeting.control.hst.request.base.BaseRequestDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.core.annotation.Order;

/**
 *
 */
@Data
public class AddOrgRequestDTO extends BaseRequestDTO {

    /**
     * 组织架构名称
     */
    @ApiModelProperty("组织架构名称")
    @Order(0)
    private String departName;


    @ApiModelProperty("父ID")
    @Order(1)
    private Integer parentDepartID;

    @ApiModelProperty("灯塔组织ID")
    @Order(2)
    private String authDepartID;

    @ApiModelProperty("灯塔组织父ID")
    @Order(3)
    private String authParentDepartID;

    @ApiModelProperty("keyCode")
    @Order(4)
    private String keyCode;
}
