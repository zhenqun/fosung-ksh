package com.fosung.ksh.meeting.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.meeting.entity.MeetingTemplateCycle;

public interface MeetingTemplateCycleService extends AppBaseDataService<MeetingTemplateCycle,Long> {
    public void add(MeetingTemplateCycle meetingTemplateCycle) throws Exception;
    public void deleteByTemplateId(Long meetingTemplateId);
    public MeetingTemplateCycle getByTemplateId(Long meetingTemplateId);
}
