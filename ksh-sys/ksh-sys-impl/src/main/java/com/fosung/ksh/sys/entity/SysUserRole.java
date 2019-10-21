package com.fosung.ksh.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.ksh.sys.dto.SysOrg;
import com.fosung.ksh.sys.entity.constant.ManageType;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 用户角色表
 * Created by lizhen on 2018\7\25 0017.
 */
@Entity
@Table(name = "sys_user_role")
@Data
public class SysUserRole extends AppJpaBaseEntity {

    /**
     * 用户id
     */
    @NotNull
    @Column(name = "user_hash")
    private String userHash;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long roleId;


    /**
     * 角色管理的党组织Id
     */
    @Column(name = "manage_id")
    private String manageId;


    @Enumerated(EnumType.STRING)
    @Column(name = "manage_type")
    private ManageType manageType;


    @Transient
    private SysUser sysUser;

    @Transient
    private SysOrg sysOrg;

    @Transient
    private SysArea sysArea;

}
