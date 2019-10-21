package com.fosung.ksh.surface.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangyihua
 * @date 2019-05-07 14:15
 */
@Data
public class SysUserDTO implements Serializable {
    private static final long serialVersionUID = -7881507097676018026L;

    @ApiModelProperty("用户唯一标志")
    private String userHash;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("用户所属党组织ID")
    private String orgId;

    @ApiModelProperty("用户所属党组织编码")
    private String orgCode;

    @ApiModelProperty("用户所属党组织名称")
    private String orgName;

}
