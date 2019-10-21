package com.fosung.ksh.sys.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户信息
 */
@Data
public class SysRole {

    /**
     * 角色ID
     */
    private Object roleId;


    /**
     * 角色所属clientId
     */
    private Object clientId;

    /**
     * 角色名称
     */
    private Object roleName;

    /**
     * 角色管理的党组织Id
     */
    private Object manageId;

    /**
     * 角色管理的党组织编码
     */
    private Object manageCode;

    /**
     * 角色管理的党组织编码
     */
    private Object manageName;
    private List<SysPermission> permissions;

}
