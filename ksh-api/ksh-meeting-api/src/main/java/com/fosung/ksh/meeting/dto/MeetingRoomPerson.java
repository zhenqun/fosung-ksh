package com.fosung.ksh.meeting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.ksh.meeting.constant.SigninType;
import com.fosung.ksh.meeting.constant.UserRight;
import com.fosung.ksh.meeting.constant.UserType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * 参会人员记录
 * @author wangyh
 */
@Data
public class MeetingRoomPerson  implements Serializable {

    private static final long serialVersionUID = -6596685973835785256L;
    /**
     * 会议记录Id
     */
    private Long meetingRoomId;

    /**
     *
     */
    private Integer hstRoomId;

    /**
     * 授权唯一标志
     * 如果为本地用户，则为String类型本地用户ID
     * 如果为党员用户，则为党员用户hash
     */
    private String userHash;

    /**
     * 用户名
     */
    private String personName;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 用户类型 1系统用户 ,2灯塔党员
     */
    private UserType userType;


    /**
     * 会议室角色 1旁听 2 参会人 3 主席；  权限值为0时，表示取消该用户会议室权限；
     * 如果只是单纯签到的话，不具有任何角色
     */
    private UserRight userRight;


    /**
     * 签到方式
     */
    private SigninType signInType;


    /**
     * 签到时间
     */

    private String signInTime;

    /**
     * 用户所属组织
     */
    private String orgId;

    /**
     * 用户所属组织
     */
    private String orgCode;

    /**
     * 用户所属组织
     */
    private String orgName;


    private String sign;
    private String roomName;
}
