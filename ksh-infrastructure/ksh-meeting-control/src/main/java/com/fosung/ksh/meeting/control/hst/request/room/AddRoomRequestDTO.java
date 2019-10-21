package com.fosung.ksh.meeting.control.hst.request.room;


import com.fosung.ksh.meeting.control.hst.config.constant.RoomType;
import com.fosung.ksh.meeting.control.hst.request.base.BaseRequestDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.core.annotation.Order;

import java.util.Date;
import java.util.List;

/**
 * @author wangyh
 * @description:新增会议室 （添加新的会议室，可设置基础参数。可调用其它接口修改更多其他会议参数。）
 * @date: 2018/12/4 14:03
 */
@Data
public class AddRoomRequestDTO extends BaseRequestDTO {


    /**
     * 会议室名(支持汉字、英文字母a-zA-Z、数字0-9、英文下划线,长度在1-32位之间)
     */
    @ApiModelProperty("会议室名(任意字符，长度在1-150位之间)")
    @Order(0)
    private String roomName;

    /**
     * 登录校验模型(1、用户密码验证；2、会议室密码验证；3、匿名登录)
     */
    @Order(1)
    @ApiModelProperty("登录校验模型(1、用户密码验证（默认）；2、会议室密码验证；3、匿名登录)")
    private String verifyMode = "1";

    /**
     * 会议室密码(当登录校验模型为会议室密码验证时，不允许为空。4-32位)
     */
    @Order(2)
    @ApiModelProperty("会议室密码(当登录校验模型为会议室密码验证时，不允许为空。4-32位)")
    private String password;

    /**
     * 允许最大用户数
     */
    @Order(3)
    @ApiModelProperty("允许最大用户数，默认为5000")
    private Integer maxUserCount = 5000;

    /**
     * 是否允许管理员密码(0不启用，1启用)
     */
    @Order(4)
    @ApiModelProperty("是否允许管理员密码(0不启用（默认），1启用)")
    private String enableChairPwd = "0";

    /**
     * 管理员密码(当有允许管理员密码时，不允许为空。4-32位)
     */
    @Order(5)
    @ApiModelProperty("管理员密码(当有允许管理员密码时，不允许为空。4-32位)")
    private String chairPassword;

    /**
     * 图片抓拍时间
     */
    @Order(6)
    @ApiModelProperty("图片抓拍时间（分钟），默认30")
    private Integer intervalTime = 30;




    /**
     * 待授权的用户信息
     */
    @ApiModelProperty("待授权的用户列表")
    private List<UserRightByRoomDTO> userRightList;


    /**
     * 会议室类型，固定会议室、预约会议室
     */
    @ApiModelProperty("会议室类型：（FIXED固定会议室、HOPE预约会议室）")
    private RoomType roomType;

    /**
     * 预约开始时间
     */
    @ApiModelProperty("预约会议室开始时间")
    private Date hopeStartTime;


    /**
     * 预约结束时间
     */
    @ApiModelProperty("预约会议室结束时间")
    private Date hopeEndTime;

    /**
     * 会议室ID
     */
    @ApiModelProperty("会议室ID")
    private Integer roomId;
    /**
     * 是否签到
     */
    @Order(7)
    @ApiModelProperty("是否签到")
    private String checkInType="0";
    /**
     * 签到时长
     */
    @Order(8)
    @ApiModelProperty("签到时长")
    private Integer checkInTime=0;
    /**
     * 是否补签
     */
    @Order(9)
    @ApiModelProperty("是否补签")
    private String additionalCheckInType="0";

    /**
     * 补签时长
     */
    @Order(10)
    @ApiModelProperty("补签时长")
    private Integer additionalCheckInTime=0;
    /**
     * 密钥用于访问认证，默认为fswebservice2011，接口中出现的keyCode都是fswebservice2011加密后字符串：3025495AEE146DA3864AB81BAAF79A3E
     */
    @Order(11)
    @ApiModelProperty("密钥用于访问认证，默认为fswebservice2011，接口中出现的keyCode都是fswebservice2011加密后字符串：3025495AEE146DA3864AB81BAAF79A3E")
    private String keyCode = "3025495AEE146DA3864AB81BAAF79A3E";

}
