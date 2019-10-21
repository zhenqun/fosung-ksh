package com.fosung.ksh.organization.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fosung.ksh.organization.dao.OrgLifeBranchDao;
import com.fosung.ksh.organization.entity.OrgLifeBranch;
import com.fosung.ksh.organization.entity.OrgLifeType;
import com.fosung.ksh.organization.service.OrgLifeBranchService;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.dto.SysOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrgLifeBranchServiceImpl extends AppJPABaseDataServiceImpl<OrgLifeBranch, OrgLifeBranchDao>
	implements OrgLifeBranchService {


	/**
	 * 查询条件表达式
	 */
	private Map<String, String> expressionMap = new LinkedHashMap<String , String>(){
			{
				put("orgLifeId", "orgLifeId:EQ");
				put("orgLifeIdIN", "orgLifeId:IN");
				put("branchIdIN", "branchId:IN");
	       }
	};
	
	@Override
	public Map<String, String> getQueryExpressions() {
		return expressionMap ;
	}


	/**
	 * 批量保存数据
	 *
	 * @param orgLifeId
	 * @param list
	 */
	@Override
	@Transactional
	public void save(Long orgLifeId, List<OrgLifeBranch> list) {
		if (list != null) {
			this.entityDao.deleteByOrgLifeId(orgLifeId);
			for (OrgLifeBranch orgLifeType : list) {
				orgLifeType.setId(null);
				orgLifeType.setOrgLifeId(orgLifeId);
			}
			saveBatch(list);
		}
	}

}
