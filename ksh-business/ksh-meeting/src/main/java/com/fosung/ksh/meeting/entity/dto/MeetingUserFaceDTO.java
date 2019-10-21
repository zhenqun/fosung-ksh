package com.fosung.ksh.meeting.entity.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.ksh.sys.dto.SysUser;
import lombok.Data;

/**
 * 人脸采集
 *
 * @author wangyihua
 * @date 2019-06-18 14:42
 */
@Data
public class MeetingUserFaceDTO extends SysUser {
    private String imageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long meetingUserFaceId;
}
