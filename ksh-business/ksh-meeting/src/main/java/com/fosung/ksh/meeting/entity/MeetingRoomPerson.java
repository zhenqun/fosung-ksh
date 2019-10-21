package com.fosung.ksh.meeting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.ksh.meeting.constant.SigninType;
import com.fosung.ksh.meeting.constant.UserRight;
import com.fosung.ksh.meeting.constant.UserType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 参会人员记录
 * @author wangyh
 */
@Entity
@Table(name = "meeting_room_person")
@Data
public class MeetingRoomPerson extends AppJpaBaseEntity {

    /**
     * 会议记录Id
     */
    @ApiModelProperty("会议室ID")
    @Column(name = "meeting_room_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long meetingRoomId;



    /**
     * 授权唯一标志
     * 如果为本地用户，则为String类型本地用户ID
     * 如果为党员用户，则为党员用户hash
     */
    @ApiModelProperty("用户唯一标志")
    @Column(name = "user_hash")
    private String userHash;

    /**
     * 用户名
     */
    @ApiModelProperty("用户姓名")
    @Column(name = "person_name")
    private String personName;

    /**
     * 手机号
     */
    @ApiModelProperty("用户手机号")
    @Column(name = "telephone")
    private String telephone;

    /**
     * 用户类型 1系统用户 ,2灯塔党员
     */
    @ApiModelProperty("用户类型 LOCAL 系统用户 ,DT 灯塔党员")
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @ApiModelProperty("用户类型 LOCAL 系统用户 ,DT 灯塔党员")
    @Transient
    private String userTypeDict;
    /**
     * 会议室角色 1旁听 2 参会人 3 主席；  权限值为0时，表示取消该用户会议室权限；
     * 如果只是单纯签到的话，不具有任何角色
     */
    @ApiModelProperty(" 会议室角色 NOAUTH 取消授权 HEARER 旁听  ATTENDEE 参会人 CHAIRMAN 主席。")
    @Enumerated(EnumType.STRING)
    @Column(name = "user_right")
    private UserRight userRight;

    @ApiModelProperty(" 会议室角色 NOAUTH 取消授权 HEARER 旁听  ATTENDEE 参会人 CHAIRMAN 主席。")
    @Transient
    private String userRightDict;

    /**
     * 签到方式
     */
    @ApiModelProperty(" 签到方式 FACE 人脸识别 LOGIN 终端登录  ADMININPUT 管理员录入 ")
    @Enumerated(EnumType.STRING)
    @Column(name = "sign_in_type")
    private SigninType signInType;

    @ApiModelProperty(" 签到方式 FACE 人脸识别 LOGIN 终端登录  ADMININPUT 管理员录入 ")
    @Transient
    private String signInTypeDict;

    /**
     * 签到时间
     */
    @ApiModelProperty(" 签到时间，格式yyyy-MM-dd HH:mm:ss")
    @Column(name="sign_in_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    private Date signInTime;

    /**
     * 用户所属组织
     */
    @ApiModelProperty("用户所属党组织")
    @Column(name = "org_id")
    private String orgId;

    /**
     * 用户所属组织
     */
    @ApiModelProperty("用户所属党组织编号")
    @Transient
    private String orgCode;

    /**
     * 用户所属组织
     */
    @ApiModelProperty("用户所属党组织名称")
    @Transient
    private String orgName;

    /**
     * 用户所属组织
     */
    @ApiModelProperty("好视通会议室id")
    @Transient
    private Integer hstRoomId;

    @ApiModelProperty("设备是否接入")
    @Transient
    private Integer joinRecordsNum;

    @ApiModelProperty("参会人数")
    @Transient
    private Integer attendanceNum;
}
