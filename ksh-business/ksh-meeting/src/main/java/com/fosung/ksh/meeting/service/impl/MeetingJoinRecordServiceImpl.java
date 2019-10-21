package com.fosung.ksh.meeting.service.impl;

import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.meeting.dao.MeetingJoinRecordDao;
import com.fosung.ksh.meeting.entity.MeetingJoinRecord;
import com.fosung.ksh.meeting.service.MeetingJoinRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Service
public class MeetingJoinRecordServiceImpl
        extends AppJPABaseDataServiceImpl<MeetingJoinRecord, MeetingJoinRecordDao> implements MeetingJoinRecordService {


    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("meetingRoomId", "meetingRoomId:EQ");
            put("userHash", "userHash:EQ");
            put("leaveDate","leaveDate:ISNULL");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }

    /**
     * 离开会议室记录
     * @param hstRoomId
     * @param userHash
     * @param date
     */
    public void leave(Integer hstRoomId, String userHash, Date date) {
        this.entityDao.leave(hstRoomId, userHash, date);
    }

    /**
     * 获取最后离场时间
     * @return
     */
    public Date getMaxLeaveDate(Long meetingRoomId){
        return this.entityDao.getMaxLeaveDate(meetingRoomId);
    }

    @Override
    public Integer countMeetingRooms(Long meetingRoomId, String userHash) {
        return this.entityDao.countMeetingRooms(meetingRoomId,userHash);
    }
}
