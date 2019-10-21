package com.fosung.ksh.meeting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "meeting_notice_person")
public class MeetingNoticePerson  extends AppJpaBaseEntity {

    @ApiModelProperty("通知ID")
    @Column(name = "meeting_notice_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long meetingNoticeId;

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

    @ApiModelProperty("是否已读 1表示未读取 2表示读取")
    @Column(name = "read_info")
    private String readInfo;

}
