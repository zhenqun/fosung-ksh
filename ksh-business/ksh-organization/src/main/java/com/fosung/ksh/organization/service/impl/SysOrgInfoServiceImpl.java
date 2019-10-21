package com.fosung.ksh.organization.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fosung.ksh.organization.dao.SysOrgInfoDao;
import com.fosung.ksh.organization.entity.SysOrgInfo;
import com.fosung.ksh.organization.service.SysOrgInfoService;
import org.springframework.stereotype.Service;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;

@Service
public class SysOrgInfoServiceImpl extends AppJPABaseDataServiceImpl<SysOrgInfo, SysOrgInfoDao>
	implements SysOrgInfoService {
	
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
