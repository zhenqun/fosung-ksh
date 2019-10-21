package com.fosung.ksh.meeting.control.dto.user;

import lombok.Data;

/**
 * 新增修改用户请求数据
 *
 * @author wangyh
 */
@Data
public class UserinfoRequestDTO {
    private static final long serialVersionUID = -1228032683418980378L;

    /**
     * 新增用户信息
     */

    /**
     * 用户名（支持汉字、英文字母a-zA-Z、数字0-9、英文下划线,长度为1-32位）
     */
    private String userName;

    /**
     * 用户密码（支持数字、字母、特殊符号；不应小于8位。由数字、字母等混合组成）
     */
    private String passpwd;

    /**
     * 昵称（支持空格（纯空格）、中文字符（全角），数字、字母、特殊字符;昵称为空时，则昵称与用户名相同）
     */
    private String nickName;
    /**
     * 组织架构（组织架构为0时，查询出根组织在根组织上创建用户，如不指定组织，填0即可）
     */
    private Integer departID;
    /**
     * 角色（基本角色值为2,默认为基本角色）
     */
    private String adminRole;

    /**
     * 性别（默认为男性，值为0；女性为1）
     */
    private String sex;
    /**
     * 手机号码（6-32位纯数字）
     */
    private String mobile;
    /**
     * 固定电话（6-32位纯数字，可以0开头）
     */
    private String telePhone;
    /**
     * 电子邮箱（邮箱正则验证，4到32位）
     */
    private String email;
    /**
     * 密钥（密钥用于访问认证，默认为fswebservice2011，接口中出现的keyCode都是fswebservice2011加密后字符串：3025495AEE146DA3864AB81BAAF79A3E）
     */
    private String keyCode;
    /**
     * 密码类型（是否MD5加密）（默认为0，为0时传入普通密码，为1时传入MD5加密后的密码。注：登录都是用未加密的密码登录。）
     */
    private Integer passwordType;

    private String orgId;

}
