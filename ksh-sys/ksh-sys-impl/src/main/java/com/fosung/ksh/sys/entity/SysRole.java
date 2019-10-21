package com.fosung.ksh.sys.entity;

import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 用户信息
 */
@Data
@Entity
@Table(name = "sys_role")
public class SysRole extends AppJpaBaseEntity {

    private static final long serialVersionUID = -5838798718600275067L;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private String roleId;


    /**
     * 角色所属clientId
     */
    @Column(name = "client_id")
    private String clientId;

    /**
     * 角色所属clientId
     */
    @Column(name = "client_name")
    private String clientName;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_description")
    private String roleDescription;

    /**
     * 角色管理的党组织Id
     */
    @Transient
    private String manageId;

    /**
     * 角色管理的党组织编码
     */
    @Transient
    private String manageCode;

    /**
     * 角色管理的党组织编码
     */
    @Transient
    private String manageName;

    @Transient
    private List<SysPermission> permissions;


}
