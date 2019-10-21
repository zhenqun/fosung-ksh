package com.fosung.ksh.meeting.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.ksh.meeting.constant.UserRight;
import com.fosung.ksh.meeting.dao.MeetingTemplatePersonDao;
import com.fosung.ksh.meeting.entity.MeetingRoomPerson;
import com.fosung.ksh.meeting.entity.MeetingTemplatePerson;
import com.fosung.ksh.meeting.service.MeetingTemplatePersonService;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.client.SysUserClient;
import com.fosung.ksh.sys.dto.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;

@Service
public class MeetingTemplatePersonServiceImpl extends AppJPABaseDataServiceImpl<MeetingTemplatePerson, MeetingTemplatePersonDao>
	implements MeetingTemplatePersonService {

	@Autowired
	private SysOrgClient sysOrgClient;

	@Autowired
	private SysUserClient sysUserClient;
	/**
	 * 查询条件表达式
	 */
	private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
		{
			put("meetingTemplateId", "meetingTemplateId:EQ");
			put("userHash", "userHash:EQ");
			put("signInType", "signInType:ISNOTNULL");
			put("orgIds", "orgId:IN");
			put("userType", "userType:EQ");
			put("userRight", "userRight:EQ");
			put("notUserRight", "userRight:NEQ");
			put("orgId", "orgId:EQ");
		}
	};
	@Override
	public Map<String, String> getQueryExpressions() {
		return expressionMap;
	}

	@Override
	public List<SysUser> queryNotRightDTList(Long meetingTemplateId, String orgId) {
		List<SysUser> users = sysUserClient.queryDTUserByOrgId(orgId);
		if (UtilCollection.isNotEmpty(users)) {
			users = users.stream().filter(user -> {
				MeetingTemplatePerson person = get(meetingTemplateId, user.getHash());
				return person == null || person.getUserRight() == UserRight.NOAUTH;
			}).collect(Collectors.toList());
		}
		return users;
	}

	@Override
	public MeetingTemplatePerson get(Long meetingTemplateId, String userHash) {
		Map<String, Object> searchParam = new HashMap<String, Object>() {{
			put("meetingTemplateId", meetingTemplateId);
			put("userHash", userHash);
		}};
		List<MeetingTemplatePerson> list = queryAll(searchParam);

		//如果数据已存在，则进行修改
		if (UtilCollection.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<SysUser> queryNotRightLocalList(Long meetingTemplateId, String orgId) {
		List<SysUser> users = sysUserClient.queryLocalUserByOrgId(orgId);
		if (UtilCollection.isNotEmpty(users)) {
			users = users.stream().filter(user -> {
				MeetingTemplatePerson person = get(meetingTemplateId, user.getHash());
				return person == null || person.getUserRight() == UserRight.NOAUTH;
			}).collect(Collectors.toList());
		}
		return users;
	}
	public List<MeetingTemplatePerson> queryTemAll(Long meetingTemplateId,UserRight userRight){
		return  this.entityDao.queryTemAll(meetingTemplateId, userRight.getCode());
	}
	@Override
	public void batchUpdate(List<MeetingTemplatePerson> personList) {
		Set<String> updateFields = new HashSet<String>() {{
			add("userRight");
		}};

		personList.stream().forEach(person -> {
			MeetingTemplatePerson p = get(person.getMeetingTemplateId(), person.getUserHash());
			//如果当前用户已经存在，则修改授权信息
			if (p != null) {
				p.setUserRight(person.getUserRight());
				update(p, updateFields);
			} else {
				save(person);
			}
		});
	}

	@Override
	public void batchUpdate(List<MeetingTemplatePerson> personList, Long meetingTemplateId) {

	}

	@Override
	public List<Map<String, Object>> meetingTemplateOrgNum (Map<String, Object> searchParam) {
		return   entityDao.meetingTemplateOrgNum(searchParam);
	}

	@Override
	public Integer meetingTemplatePersonNum(Map<String, Object> searchParam) {
		return  entityDao.meetingTemplatePersonNum(searchParam);
	}
}
