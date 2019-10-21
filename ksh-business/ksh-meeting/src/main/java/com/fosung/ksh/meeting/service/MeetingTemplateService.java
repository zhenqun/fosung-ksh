package com.fosung.ksh.meeting.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.meeting.entity.MeetingRoom;
import com.fosung.ksh.meeting.entity.MeetingTemplate;
import org.apache.ibatis.annotations.Param;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MeetingTemplateService extends AppBaseDataService<MeetingTemplate,Long> {
    public Long add(MeetingTemplate template)throws Exception;
    public void edit(MeetingTemplate template, Set<String> updateFieldsTemp) throws Exception;
    public MeetingRoom convene(MeetingTemplate meetingTemplate) throws InvocationTargetException, IllegalAccessException;
    public MeetingTemplate saveTemplate(MeetingTemplate template, Set<String> updateFieldsTemp) throws Exception;
    public List<Map<String, Object>> queryMeetingTemAll(Map<String, Object> queryMeetingTemplate);
}
