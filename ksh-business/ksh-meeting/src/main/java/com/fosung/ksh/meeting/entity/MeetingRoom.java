package com.fosung.ksh.meeting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.framework.common.support.dao.entity.AppJpaSoftDelEntity;
import com.fosung.ksh.meeting.constant.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 会议室实体对象
 *
 * @author wangyh
 */
@Entity
@Table(name = "meeting_room")
@Data
public class MeetingRoom extends AppJpaBaseEntity implements AppJpaSoftDelEntity {

    /**
     * 好视通会议室ID
     */
    @ApiModelProperty("好视通会议室ID")
    @Column(name = "hst_room_id")
    private Integer hstRoomId;


    /**
     * 会议室状态
     */
    @ApiModelProperty("会议室状态编码，NOTSTART 未开始 ，GOING 进行中 ，FINISHED 已结束")
    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_status")
    private MeetingStatus meetingStatus;

    /**
     * 会议室状态名称
     */
    @ApiModelProperty("会议室状态名称，未开始，进行中，已结束")
    @Transient
    private String meetingStatusDict;


    /**
     * 会议室名称
     */
    @ApiModelProperty("会议室名称，长度为1～150位任意字符")
    @Column(name = "room_name")
    private String roomName;


    /**
     * 最大用户数量
     */
    @ApiModelProperty("最大用户数量，默认为5000")
    @Column(name = "max_user_count")
    private Integer maxUserCount = 5000;

    /**
     * 会议室类型1固定会议室，2预约会议室
     */
    @ApiModelProperty("会议室类型，FIXED 固定会议室 ， HOPE 预约会议室")
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type")
    private RoomType roomType;


    /**
     * 会议室类型，普通会议室与组织生活会议室
     */
    @ApiModelProperty("会议类型，GENERAL 普通会议室 ， organization 组织生活会议室")
    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_type")
    private MeetingType meetingType;

    /**
     * 会议室类型，普通会议室与组织生活会议室
     */
    @ApiModelProperty("会议类型，普通会议室 , 组织生活会议室")
    @Transient
    private String meetingTypeDict;

    /**
     * 每隔X分钟抓拍会场图片
     */
    @ApiModelProperty("每隔X分钟抓拍会场图片，默认15分钟")
    @Column(name = "interval_time")
    private Integer intervalTime = 15;

    /**
     * 登录校验模型1、用户密码验证；2、会议室密码验证；3、匿名登录
     */
    @ApiModelProperty("登录校验模型 UP、用户密码验证；RP、会议室密码验证；NONE、匿名登录")
    @Enumerated(EnumType.STRING)
    @Column(name = "verify_mode")
    private VerifyMode verifyMode = VerifyMode.UP;


    /**
     * 会议室密码
     */
    @ApiModelProperty("会议室密码")
    @Column(name = "password")
    private String password;


    /**
     * 是否允许管理员密码0不启用，1启用
     */
    @ApiModelProperty("是否允许管理员密码 ENABLE 不启用，DISABLE 启用")
    @Enumerated(EnumType.STRING)
    @Column(name = "enable_chair_pwd")
    private EnableChairPwd enableChairPwd = EnableChairPwd.DISABLE;


    /**
     * 管理员密码当有允许管理员密码时，不允许为空。4-32位
     */
    @ApiModelProperty("管理员密码当有允许管理员密码时，不允许为空。4-32位")
    @Column(name = "chair_password")
    private String chairPassword;


    /**
     * 是否启用默认视频参数1 启用默认视频参数，0 不启用默认视频参数
     */
    @ApiModelProperty("是否启用默认视频参数 YES 启用默认视频参数，NO 不启用默认视频参数")
    @Enumerated(EnumType.STRING)
    @Column(name = "use_default_flag")
    private UseDefaultFlag useDefaultFlag = UseDefaultFlag.NO;


    /**
     * 默认视频编码器 3 H.264;2 MPEG4;默认H.264；当启用默认视频参数时，不得为空
     */
    @ApiModelProperty("默认视频编码器 3 H.264;2 MPEG4;默认H.264；当启用默认视频参数时，不得为空")
    @Column(name = "default_video_codec")
    private Integer defaultVideoCodec = 3;


    /**
     * 默认分辨率:参数为：4到8；4 640*480;5 704*576;6 720*576;7 1280*720;8 1920*1080,需要授权文件允许，默认4（640*480）；当启用默认视频参数时，不得为空。
     */
    @ApiModelProperty("默认分辨率:参数为：4到8；4 640*480;5 704*576;6 720*576;7 1280*720;8 1920*1080,需要授权文件允许，默认4（640*480）；当启用默认视频参数时，不得为空。")
    @Column(name = "default_video_wind")
    private Integer defaultVideoWind = 4;


    /**
     * 默认视频码流10-2000（kbps）之间；默认为128；当启用默认视频参数时，不得为空。其余情况，填入0;
     */
    @ApiModelProperty("默认视频码流10-2000（kbps）之间；默认为128；当启用默认视频参数时，不得为空。其余情况，填入0;")
    @Column(name = "default_video_bitrate")
    private Integer defaultVideoBitrate = 128;


    /**
     * 所属组织
     */
    @ApiModelProperty("当前会议室所属党组织ID")
    @Column(name = "org_id")
    private String orgId;

    /**
     * 所属组织
     */
    @ApiModelProperty("当前会议室所属党组织编码")
    @Transient
    private String orgCode;


    /**
     * 所属组织
     */
    @ApiModelProperty("当前会议室所属党组织名称")
    @Transient
    private String orgName;


    /**
     * room_type为2时，预定开始时间 格式：yyyy-MM-dd HH:mm:ss；会议室类型为预约会议室时，不允许为空
     */
    @ApiModelProperty("预定开始时间 格式：yyyy-MM-dd HH:mm:ss；会议室类型为预约会议室时，不允许为空")
    @Column(name = "hope_start_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    private Date hopeStartTime;


    /**
     * room_type为2时，预定结束时间 格式：yyyy-MM-dd HH:mm:ss；会议室类型为预约会议室时，不允许为空
     */
    @ApiModelProperty("预定结束时间 格式：yyyy-MM-dd HH:mm:ss；会议室类型为预约会议室时，不允许为空")
    @Column(name = "hope_end_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    private Date hopeEndTime;

    /**
     * 实际开始时间
     */
    @ApiModelProperty("会议室实际开始时间 格式：yyyy-MM-dd HH:mm:ss；会议室类型为预约会议室时，为预约开始时间，为固定会议室时，为当前创建时间")
    @Column(name = "real_start_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    private Date realStartTime;

    /**
     * 实际结束时间
     */
    @ApiModelProperty("会议室实际结束时间 格式：yyyy-MM-dd HH:mm:ss")
    @Column(name = "real_end_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    private Date realEndTime;


    /**
     * 软删除标志
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "del")
    private Boolean del = false;

    /**
     * 当前在线人数
     */
    @ApiModelProperty("会议室当前在线人数")
    @Transient
    private Integer currentUserNum = 0;

    @Transient
    private Integer allUserNum=0;

    /**
     * 模版ID
     */
    @ApiModelProperty("会议室对应模版ID")
    @Transient
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long meetingTemplateId;

    /**
     * 参会人员列表
     */
    @ApiModelProperty("参会人员列表")
    @Transient
    private List<MeetingRoomPerson> persons;


    @ApiModelProperty("会议是否签到 sign 1 是 2否")
    @Column(name = "sign")
    @Enumerated(EnumType.STRING)
    private Sign sign=Sign.ENABLE;

    @ApiModelProperty("签到时长")
    @Column(name = "sign_duration")
    private Integer signDuration;


    @Column(name = "repair_sign")
    @Enumerated(EnumType.STRING)
    private Sign repairSign=Sign.ENABLE;

    @ApiModelProperty("补签时长")
    @Column(name = "repair_duration")
    private Integer repairDuration;

    //应参加人员数
    @Transient
    private Integer personNum;
    //应参加党组织树
    @Transient
    private Integer OrgNum;

}
