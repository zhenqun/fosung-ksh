package com.fosung.ksh.meeting.control.hst.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangyh
 */
@Data
public class UserResponseDTO implements Serializable {
    private static final long serialVersionUID = 325028881121137743L;

    /**
     * 后台权限 0普通用户 1所在组织的管理员
     */
    private String adminRole;
    /**
     * 所属组织的组织id
     */
    private Integer departId;
    /**
     * 用户昵称
     */
    private String displayName;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 用户加密后的密码
     */
    private String password;
    /**
     * 用户性别 0男性 1女性
     */
    private String sex;
    /**
     * 用户座机
     */
    private String telephone;
    /**
     * 用户名（帐号）
     */
    private String userName;
    /*
     * status
     */
    private String status;
    /*
     * userlevel
     */
    private String userLevel;
    /*
     * 用户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    /*
     * 用户在会中的权限 1旁听 2 出席 3 主席
     */
    private String userRight;
    /*
     * 用户类型 0游客（临时用户） 1实名用户（有相应帐号）
     */
    private String userType;
    /*
     * 是否为主讲 0不是 1是
     */
    private String isPresenter;
    /*
     * 用户ip地址
     */
    private String userIp;

}
