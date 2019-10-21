package com.fosung.ksh.meeting.service.impl;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.meeting.constant.UserRight;
import com.fosung.ksh.meeting.dao.MeetingNoticeDao;
import com.fosung.ksh.meeting.entity.MeetingNotice;
import com.fosung.ksh.meeting.entity.MeetingNoticePerson;
import com.fosung.ksh.meeting.entity.MeetingRoom;
import com.fosung.ksh.meeting.entity.MeetingRoomPerson;
import com.fosung.ksh.meeting.service.MeetingNoticePersonService;
import com.fosung.ksh.meeting.service.MeetingNoticeService;
import com.fosung.ksh.meeting.service.MeetingRoomPersonService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Slf4j
@Service
public class MeetingNoticeServiceImpl extends AppJPABaseDataServiceImpl<MeetingNotice, MeetingNoticeDao>
        implements MeetingNoticeService {

    @Autowired
    public MeetingRoomPersonService meetingRoomPersonService;
    @Autowired
    public MeetingNoticePersonService meetingNoticePersonService;

    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("createUserId", "createUserId:EQ");
            put("userHashs", "userHashs:LIKE");
            put("info", "info:LIKE");
        }
    };
    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }

    public MeetingNotice saveMeetNotice(MeetingNotice meetingNotice)  {
        long meetingRoomId = meetingNotice.getMeetingRoomId();
        save(meetingNotice);
        Map<String, Object> param = Maps.newHashMap();
        param.put("meetingRoomId", meetingRoomId);
        param.put("userRights" , UserRight.ATTENDEE + "," + UserRight.CHAIRMAN + "," + UserRight.HEARER);
        List<MeetingRoomPerson> list = meetingRoomPersonService.queryAll(param);
        String userHashs="";
        for (int i=0; i<list.size();i++){
            MeetingRoomPerson person=list.get(i);
            MeetingNoticePerson noticePerson=new MeetingNoticePerson();
            noticePerson.setMeetingNoticeId(meetingNotice.getId());
            noticePerson.setPersonName(person.getPersonName());
            noticePerson.setReadInfo("1");
            noticePerson.setTelephone(person.getTelephone());
            noticePerson.setUserHash(person.getUserHash());
            userHashs+=person.getUserHash()+",";
            meetingNoticePersonService.save(noticePerson);

        }
        meetingNotice.setUserHashs(userHashs);
        update(meetingNotice, Sets.newHashSet("userHashs"));
        return meetingNotice;
    }
}
