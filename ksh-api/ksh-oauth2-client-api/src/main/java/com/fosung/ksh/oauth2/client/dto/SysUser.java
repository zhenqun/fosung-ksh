package com.fosung.ksh.oauth2.client.dto;

import com.fosung.framework.common.secure.auth.constant.UserSource;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 */
@Data
public class SysUser implements Serializable {

    private static final long serialVersionUID = 166454455614338585L;

    /**
     * 用户id
     */
    private String id;

    /**
     * 用户hash
     */
    private String hash;

    /**
     * 登录用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;


    /**
     * 登录用户名
     */
    private String idCard;


    /**
     * 手机号
     */
    private String telephone;

    /**
     * 用户真实姓名
     */
    private String realName;


    /**
     * 用户所属党组织ID
     */
    private String orgId;

    /**
     * 用户所属党组织编码
     */
    private String orgCode;

    /**
     * 用户所属党组织编码
     */
    private String orgName;


    /**
     * 用户来源
     */
    private UserSource userSource;


    private List<SysRole> roles = Lists.newArrayList();

}
