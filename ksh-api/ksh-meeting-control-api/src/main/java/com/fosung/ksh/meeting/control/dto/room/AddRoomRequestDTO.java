package com.fosung.ksh.meeting.control.dto.room;


import com.fosung.ksh.meeting.control.constant.RoomType;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author wangyh
 * @description:新增会议室 （添加新的会议室，可设置基础参数。可调用其它接口修改更多其他会议参数。）
 * @date: 2018/12/4 14:03
 */
@Data
public class AddRoomRequestDTO {


    /**
     * 会议室id
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
     * 待授权的用户信息
     */
    private List<UserRightByRoomDTO> userRightList;


    /**
     * 会议室类型，固定会议室、预约会议室
     */
    private RoomType roomType;

    /**
     * 预约开始时间
     */
    private Date hopeStartTime;


    /**
     * 预约结束时间
     */
    private Date hopeEndTime;

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
