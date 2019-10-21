package com.fosung.ksh.meeting.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import com.fosung.framework.common.support.anno.AppDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 会议类型
 *
 * @author toquery
 * @version 1
 */
@AppDict("meeting_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum MeetingType implements AppRuntimeDict {
    GENERAL("普通会议"), ORGANIZATION("组织生活会议") , LIVE("直播会议");
    private String remark;
}


