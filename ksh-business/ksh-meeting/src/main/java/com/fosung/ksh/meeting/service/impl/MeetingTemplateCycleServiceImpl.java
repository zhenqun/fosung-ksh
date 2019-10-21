package com.fosung.ksh.meeting.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fosung.ksh.meeting.dao.MeetingTemplateCycleDao;
import com.fosung.ksh.meeting.entity.MeetingTemplateCycle;
import com.fosung.ksh.meeting.service.MeetingTemplateCycleService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;

@Service
public class MeetingTemplateCycleServiceImpl extends AppJPABaseDataServiceImpl<MeetingTemplateCycle, MeetingTemplateCycleDao>
	implements MeetingTemplateCycleService {
	
	/**
	 * 查询条件表达式
	 */
	private Map<String, String> expressionMap = new LinkedHashMap<String , String>(){
			{
				put("meetingTemplateId", "meetingTemplateId:EQ");
	       }
	};
	
	@Override
	public Map<String, String> getQueryExpressions() {
		return expressionMap ;
	}

	@Override
	public void add(MeetingTemplateCycle meetingTemplateCycle) throws Exception {
		deleteByTemplateId(meetingTemplateCycle.getMeetingTemplateId());
		save(meetingTemplateCycle);
	}

	@Override
	public void deleteByTemplateId(Long meetingTemplateId){

		this.entityDao.deleteByTemplateId(meetingTemplateId);
	}

	@Override
	public MeetingTemplateCycle getByTemplateId(Long meetingTemplateId) {
		Map<String, Object> queryParams = Maps.newHashMap();
		queryParams.put("meetingTemplateId", meetingTemplateId);
		List<MeetingTemplateCycle> persons = this.queryAll(queryParams,null);
		if(persons.size()>0) {
			return persons.get(0);
		}else{
			return  null;
		}
	}


}
