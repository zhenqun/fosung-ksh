package com.fosung.ksh.duty.service.impl;

import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.duty.dao.DutyHolidayDao;
import com.fosung.ksh.duty.entity.DutyHoliday;
import com.fosung.ksh.duty.service.DutyHolidayService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DutyHolidayServiceImpl extends AppJPABaseDataServiceImpl<DutyHoliday, DutyHolidayDao>
	implements DutyHolidayService {
	
	/**
	 * 查询条件表达式
	 */
	private Map<String, String> expressionMap = new LinkedHashMap<String , String>(){
			{
	       }
	};
	
	@Override
	public Map<String, String> getQueryExpressions() {
		return expressionMap ;
	}
	

}
