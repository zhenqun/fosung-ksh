package com.fosung.ksh.meeting.control.dto.room;


import lombok.Data;

/**
 * @author wangyh
 * @description:修改会议室基本参数 （修改会议室的默认参数，校验模式为会议室密码或匿名登录时，允许游客登录；其中用会议室密码验证的用户权限较高，为参会人权限。管理员密码允许参会人用户在会议室中临时获得管理员权限。）
 * @date: 2018/12/4 14:03
 */
@Data
public class EditRoomRequestDTO {
    private static final long serialVersionUID = -1228032683418980378L;

    /**
     * 会议室ID
     */
    private Integer roomId;

    /**
     * 会议室名(支持汉字、英文字母a-zA-Z、数字0-9、英文下划线,长度在1-32位之间)
     */
    private String roomName;

    /**
     * 登录校验模型(1、用户密码验证；2、会议室密码验证；3、匿名登录)
     */
    private String verifyMode = "1";

    /**
     * 会议室密码(当登录校验模型为会议室密码验证时，不允许为空。4-32位)
     */
    private String password;

    /**
     * 允许最大用户数
     */
    private Integer maxUserCount = 5000;

    /**
     * 是否允许管理员密码(0不启用，1启用)
     */
    private String enableChairPwd = "0";

    /**
     * 管理员密码(当有允许管理员密码时，不允许为空。4-32位)
     */
    private String chairPassword;

    /**
     * 图片抓拍时间
     */
    private Integer intervalTime = 30;

    /**
     * 密钥用于访问认证，默认为fswebservice2011，接口中出现的keyCode都是fswebservice2011加密后字符串：3025495AEE146DA3864AB81BAAF79A3E
     */
    private String keyCode = "3025495AEE146DA3864AB81BAAF79A3E";
    /**
     * 是否签到
     */

    private String checkInType="0";
    /**
     * 签到时长
     */

    private Integer checkInTime=0;
    /**
     * 是否补签
     */

    private String additionalCheckInType="0";

    /**
     * 补签时长
     */

    private Integer additionalCheckInTime=0;
}
