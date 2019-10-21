package com.fosung.ksh.meeting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.ksh.meeting.constant.MeetingStatus;
import com.fosung.ksh.meeting.constant.MeetingType;
import com.fosung.ksh.meeting.constant.RoomType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 会议室实体对象
 *
 * @author wangyh
 */
@Data
public class MeetingRoom implements Serializable {

    private Long id;
    /**
     * 好视通会议室ID
     */
    private Integer hstRoomId;


    /**
     * 会议室状态
     */
    private MeetingStatus meetingStatus;

    /**
     * 会议室状态名称
     */
    private String meetingStatusDict;


    /**
     * 会议室名称
     */
    private String roomName;


    /**
     * 最大用户数量
     */
    private Integer maxUserCount = 5000;

    /**
     * 会议室类型1固定会议室，2预约会议室
     */
    private RoomType roomType;


    /**
     * 会议室类型，普通会议室与组织生活会议室
     */
    private MeetingType meetingType;

    /**
     * 会议室类型，普通会议室与组织生活会议室
     */
    private String meetingTypeDict;

    /**
     * 每隔X分钟抓拍会场图片
     */
    private Integer intervalTime = 15;



    /**
     * 所属组织
     */
    private String orgId;

    /**
     * 所属组织
     */
    private String orgCode;


    /**
     * 所属组织
     */
    private String orgName;


    /**
     * room_type为2时，预定开始时间 格式：yyyy-MM-dd HH:mm:ss；会议室类型为预约会议室时，不允许为空
     */
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    private Date hopeStartTime;


    /**
     * room_type为2时，预定结束时间 格式：yyyy-MM-dd HH:mm:ss；会议室类型为预约会议室时，不允许为空
     */
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    private Date hopeEndTime;

    /**
     * 实际开始时间
     */
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    private Date realStartTime;

    /**
     * 实际结束时间
     */
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    private Date realEndTime;


    /**
     * 当前在线人数
     */
    private Integer currentUserNum = 0;

    /**
     * 模版ID
     */
    private Long meetingTemplateId;

    /**
     * 参会人员列表
     */
    private List<MeetingRoomPerson> persons;

    //是否签到
    //是否签到
    private String  sign;

    //签到时长
    private Integer signDuration;


    //是否补签
    private String repairSign;
    //补签时长
    private Integer repairDuration;
}
