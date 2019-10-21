package com.fosung.ksh.meeting.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.meeting.entity.MeetingJoinRecord;

import java.util.Date;
import java.util.List;

/**
 * @author toquery
 * @version 1
 */
public interface MeetingJoinRecordService extends AppBaseDataService<MeetingJoinRecord, Long> {
    /**
     * 离开会议室记录
     * @param hstRoomId
     * @param userHash
     * @param date
     */
    public void leave(Integer hstRoomId, String userHash, Date date);

    /**
     * 获取最后离场时间
     * @return
     */
    public Date getMaxLeaveDate(Long meetingRoomId);

    public Integer countMeetingRooms (Long meetingRoomId, String userHash);
}
