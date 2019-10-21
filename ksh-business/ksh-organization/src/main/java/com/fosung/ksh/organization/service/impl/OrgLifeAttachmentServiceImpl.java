package com.fosung.ksh.organization.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fosung.ksh.organization.dao.OrgLifeAttachmentDao;
import com.fosung.ksh.organization.entity.OrgLifeAttachment;
import com.fosung.ksh.organization.entity.OrgLifeBranch;
import com.fosung.ksh.organization.service.OrgLifeAttachmentService;
import org.springframework.stereotype.Service;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrgLifeAttachmentServiceImpl extends AppJPABaseDataServiceImpl<OrgLifeAttachment, OrgLifeAttachmentDao>
	implements OrgLifeAttachmentService {
	
	/**
	 * 查询条件表达式
	 */
	private Map<String, String> expressionMap = new LinkedHashMap<String , String>(){
			{
				put("orgLifeId", "orgLifeId:EQ");
                put("attachmentType", "attachmentType:EQ");
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
	public void save(Long orgLifeId, List<OrgLifeAttachment> list) {
		if (list != null) {
			this.entityDao.deleteByOrgLifeId(orgLifeId);
			for (OrgLifeAttachment orgLifeType : list) {
				orgLifeType.setId(null);
				orgLifeType.setOrgLifeId(orgLifeId);
			}
			saveBatch(list);
		}
	}


}
