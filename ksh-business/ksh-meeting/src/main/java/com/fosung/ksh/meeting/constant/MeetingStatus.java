package com.fosung.ksh.meeting.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import com.fosung.framework.common.support.anno.AppDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 好视通会议室状态枚举
 * @author wangyh
 */
@AppDict("meeting_status")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum MeetingStatus implements AppRuntimeDict {

    NOTSTART("1","未开始"),

    GOING("2","进行中"),

    FINISHED("3","已结束");

    private String code;
    private String remark;
}
