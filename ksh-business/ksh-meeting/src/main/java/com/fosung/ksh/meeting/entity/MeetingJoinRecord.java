package com.fosung.ksh.meeting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *
 * @author toquery
 * @version 1
 */
@Entity
@Table(name="meeting_join_record")
@Setter
@Getter
public class MeetingJoinRecord extends AppJpaBaseEntity {

    /**
     * 会议记录id
     */
    @NotNull
    @Column(name="meeting_room_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long meetingRoomId ;

    /**
     * 好视通会议室ID
     */
    @NotNull
    @Column(name = "hst_room_id")
    private Integer hstRoomId;

    /**
     * 登录时间
     */
    @Column(name = "join_date")
    private Date joinDate = new Date();

    /**
     * 离开时间
     */
    @Column(name = "leave_date")
    private Date leaveDate;

    /**
     * 授权唯一标志
     * 如果为本地用户，则为String类型本地用户ID
     * 如果为党员用户，则为党员用户hash
     */
    @NotBlank
    @Column(name = "user_hash")
    private String userHash;
}
