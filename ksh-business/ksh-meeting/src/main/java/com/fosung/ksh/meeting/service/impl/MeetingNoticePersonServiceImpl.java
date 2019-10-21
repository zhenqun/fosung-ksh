package com.fosung.ksh.meeting.service.impl;

import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.meeting.dao.MeetingNoticePersonDao;
import com.fosung.ksh.meeting.entity.MeetingNoticePerson;
import com.fosung.ksh.meeting.service.MeetingNoticePersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MeetingNoticePersonServiceImpl extends AppJPABaseDataServiceImpl<MeetingNoticePerson, MeetingNoticePersonDao>
        implements MeetingNoticePersonService {

    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("userHash", "userHash:EQ");
            put("meetingNoticeId", "meetingNoticeId:EQ");
        }
    };
    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }

    public List<MeetingNoticePerson> meetingNoticePeoples(String userHash){
        List<MeetingNoticePerson>  meetingNoticePeoples=this.entityDao.meetingNoticePeoples(userHash);
        return meetingNoticePeoples;
    }
}
