package com.fosung.ksh.meeting.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.meeting.entity.MeetingNotice;

public interface MeetingNoticeService  extends AppBaseDataService<MeetingNotice, Long> {
    public MeetingNotice saveMeetNotice(MeetingNotice meetingNotice);
}
