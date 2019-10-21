package com.fosung.ksh.meeting.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import com.fosung.framework.common.support.anno.AppDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 会议室类型1固定会议室，2预约会议室，3循环会议室
 *
 * @author toquery
 * @version 1
 */
@AppDict("room_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum RoomType implements AppRuntimeDict {

    FIXED("固定会议室"), HOPE("预约议室"), CYCLE("循环会议室");
    private String remark;
}
