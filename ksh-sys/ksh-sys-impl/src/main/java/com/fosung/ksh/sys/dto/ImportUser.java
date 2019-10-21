package com.fosung.ksh.sys.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author toquery
 * @version 1
 */
@Data
public class ImportUser {
    // 非必填
    @Excel(name = "党组织名称")
    private String orgName;
    @Excel(name = "行政区域名称")
    private String areaName;

    // 必填
    @Excel(name = "行政区ID")
    private String areaId;
    @Excel(name = "党组织ID")
    private String dtOrgId;

    @Excel(name = "账号")
    private String username;
    @Excel(name = "昵称")
    private String realname;
    @Excel(name = "密码")
    private String password;

    @Excel(name = "行政区域角色")
    private String areaRoleName;
    @Excel(name = "视频会议角色")
    private String meetingRoleName;
    @Excel(name = "组织生活角色")
    private String orgRoleName;

}
