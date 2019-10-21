package com.fosung.ksh.meeting.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.ksh.meeting.entity.MeetingTemplateCycle;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MeetingTemplateCycleDao extends AppJPABaseDao<MeetingTemplateCycle, Long>{
    @Query("delete from MeetingTemplateCycle where meetingTemplateId = ?1")
    @Modifying
    public void deleteByTemplateId(Long meetingTemplateId);
}