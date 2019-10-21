package com.fosung.ksh.oauth2.client.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 */
@Data
public class SysRole implements Serializable {

    /**
     * 角色ID
     */
    private String roleId;


    /**
     * 角色所属clientId
     */
    private String clientId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDescription;


    /**
     * 角色管理的党组织Id
     */
    private String manageId;

    /**
     * 角色管理的党组织编码
     */
    private String manageCode;

    /**
     * 角色管理的党组织编码
     */
    private String manageName;


    private List<SysPermission> permissions;

}
