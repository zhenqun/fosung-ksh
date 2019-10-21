package com.fosung.ksh.meeting.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.ksh.meeting.entity.MeetingNotice;
import com.fosung.ksh.meeting.entity.MeetingNoticePerson;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingNoticePersonDao extends AppJPABaseDao<MeetingNoticePerson, Long> {

    @Query("from MeetingNoticePerson where userHash=?1 and  readInfo='1' order by createDatetime asc")
    public List<MeetingNoticePerson> meetingNoticePeoples(String userHash);
}
