package com.fosung.ksh.sys.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fosung.framework.common.secure.auth.constant.UserSource;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.google.common.collect.Lists;
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
@Table(name = "sys_user")
public class SysUser extends AppJpaBaseEntity {

    private static final long serialVersionUID = 166454455614338585L;

    /**
     * 灯塔 userId
     */
    @Transient
    private String userId;

    /**
     * 用户hash
     */
    @Column
    @Excel(name = "hash", orderNum = "0")
    private String hash;

    /**
     * 登录用户名
     */
    @Column(name = "user_name")
    @Excel(name = "username", orderNum = "2")
    private String username;

    /**
     * 登录用户名
     */
    @JsonIgnore
    @Column(name = "password")
    private String password;


    /**
     * 登录用户名
     */
    @Column(name = "id_card")
    private String idCard;


    /**
     * 手机号
     */
    @Column
    @Excel(name = "telephone", orderNum = "4")
    private String telephone;

    /**
     * 用户真实姓名
     */
    @Column(name = "real_name")
    @Excel(name = "realName", orderNum = "1")
    private String realName;


    /**
     * 用户所属党组织ID
     */
    @Column(name = "org_id")
    @Excel(name = "orgId", orderNum = "3")
    private String orgId;

    /**
     * 用户所属党组织编码
     */
    @Transient
    private String orgCode;

    /**
     * 用户所属党组织编码
     */
    @Transient
    private String orgName;

    @Transient
    private UserSource userSource;

    @Transient
    private List<SysRole> roles = Lists.newArrayList();

}
