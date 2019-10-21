package com.fosung.ksh.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户角色表
 * Created by lizhen on 2018\7\25 0017.
 */
@Entity
@Table(name = "sys_role_permission")
@Data
public class SysRolePermission extends AppJpaBaseEntity {

    /**
     * 权限ID
     */
    @Column(name = "permission_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long permissionId;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long roleId ;

}
