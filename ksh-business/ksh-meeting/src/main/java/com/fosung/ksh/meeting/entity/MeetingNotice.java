package com.fosung.ksh.meeting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "meeting_notice")
public class MeetingNotice  extends AppJpaBaseEntity {
    @ApiModelProperty("会议室ID")
    @Column(name = "meeting_room_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long meetingRoomId;


    @ApiModelProperty("发布信息")
    @Column(name = "info")
    private String info;
    @ApiModelProperty("发给的人员")
    @Column(name = "user_hashs")
    private String userHashs;
    @Column(
            name = "create_user_id",
            length = 32,
            updatable = false,
            nullable = true
    )
    private String createUserId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(
            name = "create_time",
            updatable = false,
            nullable = true
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createDatetime;


    @Transient
    private String roomName;
    @Transient
    private String createTime;

    @Transient
    private MeetingRoom meetingRoom;

    @Transient
    private List<MeetingNoticePerson> peoples;
}
