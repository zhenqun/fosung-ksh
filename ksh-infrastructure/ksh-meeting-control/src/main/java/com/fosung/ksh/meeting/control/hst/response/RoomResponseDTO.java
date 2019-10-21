package com.fosung.ksh.meeting.control.hst.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 新增会议室返回
 * @auther: xingxl
 * @date: 2018/12/4 14:11
 */
@Data
public class RoomResponseDTO implements Serializable {
    private static final long serialVersionUID = 325028881121137743L;

    /*
     * 会议室id
     */
    private Integer roomId;
    /*
     * 是否允许管理员密码 1允许 0不允许
     */
    private String enableChairPwd;
    /*
     * 房间最大访问人数
     */
    private Integer maxUserCount;
    /*
     * 房间名称
     */
    private String roomName;

    /*
     * 预定结束时间
     */
    private String hopeendTime;
    /*
     * 预定开始时间
     */
    private String hopeStartTime;
    /*
     * 周例循环模式(1每天，2每周，3每月)
     */
    private String cycleFlag;
    /*
     * 每月的几号(大于零，小于等于31的数值（cycleFlag为3时不空）)
     */
    private String dateEveyMonth;
    /*
     * 预定结束时间(格式：HH:mm:ss；会议室类型为周例会议室时，不允许为空)
     */
    private String weekEndTime;
    /*
     * 预定开始时间(格式：HH:mm:ss；会议室类型为周例会议室时，不允许为空)
     */
    private String weekStartTime;
    /*
     * 每个星期几(6周日，0周一，1周二，2周三，3周四，4周五，5周六（cycleFlag为2时不空）)
     */
    private String weeks;
    /*
     * 默认视频码流(10-2000（kbps）之间；默认为128；当启用默认视频参数时，不得为空。其余情况，填入0;)
     */
    private Integer defaultVideoBitrate;
    /*
     * 默认视频编码器(3 H.264;2 MPEG4;默认H.264；当启用默认视频参数时，不得为空)
     */
    private String defaultVideoCodec;
    /*
     * 默认分辨率(参数为：4到8；4 640*480;5 704*576;6 720*576;7 1280*720;8 1920*1080,需要授权文件允许，默认4（640*480）；当启用默认视频参数时，不得为空。)
     */
    private String defaultVideoWind;
    /*
     * 是否启用默认视频参数 1启用 0不启用
     */
    private String useDefaultFlag;
    /*
     * 管理员密码(当有允许管理员密码时，不允许为空。4-32位)
     */
    private String chairPassword;
    /*
     * 会议室密码(当登录校验模型为会议室密码验证时，不允许为空。4-32位)
     */
    private String password;
    /*
     * 登录校验模型(1、用户密码验证；2、会议室密码验证；3、匿名登录)
     */
    private String verifyMode;
    /*
     * creatorid
     */
    private Integer creatorId;
    /*
     * curusercount
     */
    private Integer curuserCount;
    /*
     * currentserviceid
     */
    private Integer currentServiceId;
    /*
     * defaultmode
     */
    private String defaultMode;
    /*
     * defaultvideoqos
     */
    private String defaultVideoQos;
    /*
     * defaultvideoquality
     */
    private String defaultVideoQuality;
    /*
     * departid
     */
    private Integer departId;
    /*
     * enablerecord
     */
    private String enableRecord;
    /*
     * roomappid
     */
    private Integer roomAppid;
    /*
     * roomdesc
     */
    private String roomDesc;
    /*
     * roomtype
     */
    private String roomType;
    /*
     * status
     */
    private String status;



}




