package com.fosung.ksh.meeting.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.meeting.entity.MeetingNoticePerson;

import java.util.List;

public interface MeetingNoticePersonService extends AppBaseDataService<MeetingNoticePerson, Long> {
    public List<MeetingNoticePerson> meetingNoticePeoples(String userHash);
}
