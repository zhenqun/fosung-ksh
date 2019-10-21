package com.fosung.ksh.sys.entity;

import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 权限
 */
@Data
@Entity
@Table(name = "sys_permission")
public class SysPermission extends AppJpaBaseEntity {


    /**
     * permissionId : 34
     * permissionName : FIRSTSECRETARY_OPERATE
     * permissionDescription : 可操作第一书记
     * clientId : party_firstsecretary_ui
     */

    @Column(name = "permission_id")
    private String permissionId;

    @Column(name = "permission_name")
    private String permissionName;

    @Column(name = "permission_description")
    private String permissionDescription;


    @Transient
    private List<SysRole> roles;
}
