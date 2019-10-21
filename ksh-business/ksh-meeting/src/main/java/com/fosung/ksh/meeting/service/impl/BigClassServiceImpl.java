package com.fosung.ksh.meeting.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fosung.ksh.meeting.dao.BigClassDao;
import com.fosung.ksh.meeting.entity.BigClass;
import com.fosung.ksh.meeting.service.BigClassService;
import org.springframework.stereotype.Service;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;

@Service
public class BigClassServiceImpl extends AppJPABaseDataServiceImpl<BigClass, BigClassDao>
	implements BigClassService {

	/**
	 * 查询条件表达式
	 */
	private Map<String, String> expressionMap = new LinkedHashMap<String , String>(){
			{
				put("title", "title:LIKE");
	       }
	};

	@Override
	public Map<String, String> getQueryExpressions() {
		return expressionMap ;
	}


}
