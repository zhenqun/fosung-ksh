package com.fosung.ksh.meeting.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.framework.dao.jpa.annotation.MybatisQuery;
import com.fosung.ksh.meeting.entity.MeetingTemplate;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MeetingTemplateDao extends AppJPABaseDao<MeetingTemplate, Long>{

    @Query("delete from MeetingTemplateCycle where meetingTemplateId = ?1")
    @Modifying
    public void deleteByTemplateId(Long meetingTemplateId);
    @MybatisQuery
    public List<Map<String, Object>> queryMeetingTemAll(Map<String, Object> queryMeetingTemplate);
}