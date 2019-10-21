package com.fosung.ksh.meeting.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.framework.dao.jpa.annotation.MybatisQuery;
import com.fosung.ksh.meeting.entity.MeetingJoinRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author toquery
 * @version 1
 */
public interface MeetingJoinRecordDao extends AppJPABaseDao<MeetingJoinRecord, Long> {

    @Query("update MeetingJoinRecord set leaveDate = ?3 where hstRoomId = ?1 and userHash = ?2 and leaveDate is null")
    @Modifying
    public void leave(Integer hstRoomId,String userHash,Date date);


    @Query("select max(leaveDate) from MeetingJoinRecord where meetingRoomId = ?1")
    public Date getMaxLeaveDate(Long meetingRoomId);

    @Query("SELECT COUNT(*) FROM  MeetingJoinRecord WHERE meetingRoomId =?1 and userHash = ?2 AND leaveDate is null")
    public Integer countMeetingRooms(Long meetingRoomId,String userHash);
}
