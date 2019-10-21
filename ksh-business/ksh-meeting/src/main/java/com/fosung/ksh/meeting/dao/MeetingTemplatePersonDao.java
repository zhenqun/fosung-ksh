package com.fosung.ksh.meeting.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.framework.dao.jpa.annotation.MybatisQuery;
import com.fosung.ksh.meeting.entity.MeetingTemplatePerson;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MeetingTemplatePersonDao extends AppJPABaseDao<MeetingTemplatePerson, Long>{
    /**
     * 获取获取应参会人数
     * @return
     */
    @Query("select count(t) from MeetingTemplatePerson t " +
            "where t.meetingTemplateId = ?1 and t.userRight <> 'NOAUTH' and t.orgId in ?2")
    public Integer countAllPerson(Long meetingTemplateId,List<String> orgIds);

    @Query("select count(t) from MeetingTemplatePerson t " +
            "where t.meetingTemplateId = ?1 and t.userRight <> 'NOAUTH' and t.orgId in ?2")
    public List<MeetingTemplatePerson> queryTemAll(Long meetingTemplateId,String userRight);

//    @MybatisQuery
//    int sitesIng(@org.apache.ibatis.annotations.Param("orgIds") List<String> orgIds);

    @MybatisQuery
    List<Map<String,Object>> meetingTemplateOrgNum(@org.apache.ibatis.annotations.Param("searchParam") Map<String, Object> searchParam );
    @MybatisQuery
    Integer  meetingTemplatePersonNum(@org.apache.ibatis.annotations.Param("searchParam") Map<String, Object> searchParam );
}