package com.fosung.ksh.meeting.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.ksh.meeting.constant.UserRight;
import com.fosung.ksh.meeting.entity.MeetingRoomPerson;
import com.fosung.ksh.meeting.entity.MeetingTemplatePerson;
import com.fosung.ksh.sys.dto.SysUser;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface MeetingTemplatePersonService extends AppBaseDataService<MeetingTemplatePerson,Long> {
    /**
     * 查询未授权的灯塔用户信息
     *
     * @param meetingTemplateId
     * @param orgId
     * @return
     */
    public List<SysUser> queryNotRightDTList(Long meetingTemplateId, String orgId);

    /**
     * 根据用户Hash和会议室Id,获取授权的用户
     *
     * @param meetingTemplateId
     * @param userHash
     * @return
     */
    public MeetingTemplatePerson get(Long meetingTemplateId, String userHash);
    /**
     * 查询未授权的本地用户信息
     *
     * @param meetingTemplateId
     * @param orgId
     * @return
     */
    public List<SysUser> queryNotRightLocalList(Long meetingTemplateId, String orgId);


    /**
     * 授权用户批量新增删除
     *
     * @param personList
     */
    public void batchUpdate(List<MeetingTemplatePerson> personList);

    /**
     * 授权用户批量新增删除
     *
     * @param personList
     */
    public void batchUpdate(List<MeetingTemplatePerson> personList,Long meetingTemplateId);

    //获取会议室应参加党组织数
    List<Map<String,Object>> meetingTemplateOrgNum(Map<String, Object> searchParam);
    //获取会议室应参加人数
    Integer meetingTemplatePersonNum(Map<String, Object> searchParam);
}
