package com.fosung.ksh.meeting.service.impl;


import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.meeting.constant.MeetingStatus;
import com.fosung.ksh.meeting.constant.RoomType;
import com.fosung.ksh.meeting.constant.TemplateType;
import com.fosung.ksh.meeting.control.dto.room.AddRoomRequestDTO;
import com.fosung.ksh.meeting.dao.MeetingTemplateDao;
import com.fosung.ksh.meeting.entity.*;
import com.fosung.ksh.meeting.service.*;
import com.fosung.ksh.oauth2.client.dto.SysUser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class MeetingTemplateServiceImpl extends AppJPABaseDataServiceImpl<MeetingTemplate, MeetingTemplateDao>
        implements MeetingTemplateService {
    @Autowired
    private MeetingTemplateCycleService meetingTemplateCycleService;
    @Autowired
    private MeetingTemplatePersonService meetingTemplatePersonService ;
    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private MeetingRoomPersonService meetingRoomPersonService;
    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("meetingTemplateId", "meetingTemplateId:EQ");
            put("templateType", "templateType:EQ");
            put("userHash", "userHash:EQ");
            put("orgIds", "orgId:IN");
            put("userType", "userType:EQ");
//            put("orgId", "orgId:EQ");
            put("roomName", "roomName:LIKE");
            put("userRight", "userRight:EQ");
            put("notUserRight", "userRight:NEQ");

        }
    };


    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }

    /**
     * 保存视频会议信息，并同步到好视通接口
     */
    @Override
    public Long add(MeetingTemplate template) throws Exception {
        save(template);
        if (template.getTemplateType() == TemplateType.CYCLE) {
            MeetingTemplateCycle cycle = template.getCycle();
            cycle.setMeetingTemplateId(template.getId());
            meetingTemplateCycleService.add(cycle);
        }
        return template.getId();
    }

    /**
     * 修改会议室模版
     *
     * @param template
     * @param updateFieldsTemp
     * @throws Exception
     */
    @Override
    public void edit(MeetingTemplate template, Set<String> updateFieldsTemp) throws Exception {
        update(template, updateFieldsTemp);
        if (TemplateType.CYCLE == template.getTemplateType()) {
            MeetingTemplateCycle cycle = template.getCycle();
            cycle.setMeetingTemplateId(template.getId());
            meetingTemplateCycleService.add(cycle);
        }
    }

    @Override
    public MeetingTemplate saveTemplate(MeetingTemplate template, Set<String> updateFieldsTemp) throws Exception {
        Long templateId=template.getId();
        if(templateId==null){
            save(template);
            if (template.getTemplateType() == TemplateType.CYCLE) {
                MeetingTemplateCycle cycle = template.getCycle();
                cycle.setMeetingTemplateId(template.getId());
                meetingTemplateCycleService.add(cycle);
            }
        }else{
            update(template, updateFieldsTemp);
            if (template.getTemplateType() == TemplateType.CYCLE) {
                MeetingTemplateCycle cycle = template.getCycle();
                cycle.setMeetingTemplateId(template.getId());
                meetingTemplateCycleService.add(cycle);
            }
        }
        return template;
    }

    @Override
    public List<Map<String, Object>> queryMeetingTemAll(Map<String, Object> queryMeetingTemplate) {
        List<Map<String, Object>> meetingTemplateList= this.entityDao.queryMeetingTemAll(queryMeetingTemplate);
        return meetingTemplateList;
    }

    public MeetingRoom convene(MeetingTemplate meetingTemplate) throws InvocationTargetException, IllegalAccessException {
        MeetingTemplate  meetingTemplate2=get(meetingTemplate.getId());
        meetingTemplate2.setRoomType(meetingTemplate.getRoomType());
        meetingTemplate2.setMeetingType(meetingTemplate.getMeetingType());
        meetingTemplate2.setHopeStartTime(meetingTemplate.getHopeStartTime());
        meetingTemplate2.setHopeEndTime(meetingTemplate.getHopeEndTime());
        MeetingRoom room=new MeetingRoom();
        BeanUtils.copyProperties(meetingTemplate2, room);
        room.setRoomName(meetingTemplate2.getRoomName());
        room.setId(null);
        room.setMeetingTemplateId(meetingTemplate2.getId());
        //固定会议室则设置当前时间为预计开始时间
        if (room.getRoomType() == RoomType.FIXED) {
            Date date = new Date();
            room.setMeetingStatus(MeetingStatus.GOING);
            room.setHopeStartTime(date);
            room.setRealStartTime(date);
        } else if (room.getRoomType() == RoomType.HOPE) {
            room.setMeetingStatus(MeetingStatus.NOTSTART);
            room.setRealStartTime(room.getHopeStartTime());
        }
        meetingRoomService.save(room);
        room.setPersons(userRightByTemplate(room));
        meetingRoomService.create(room.getId());
        return room;
    }



    /**
     * 通过模版添加用户授权信息
     *
     * @param room
     */

    public List<MeetingRoomPerson> userRightByTemplate(MeetingRoom room) {
        //获取模板中的授权用户信息
        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("meetingTemplateId", room.getMeetingTemplateId());
        List<MeetingTemplatePerson> templatePermissionList = meetingTemplatePersonService.queryAll(searchParam);

        //初始化授权用户
        List<MeetingRoomPerson> roomPersonList = Lists.newArrayList();
        templatePermissionList.stream().forEach(permission -> {
            MeetingRoomPerson person = new MeetingRoomPerson();
            BeanUtils.copyProperties(permission, person);
            person.setId(null);


            roomPersonList.add(person);
        });

        //当前用户默认为主席
        List<MeetingRoomPerson> roomPersons=meetingRoomService.addChairman(roomPersonList);

        for (int i=0;i<roomPersons.size();i++){
            MeetingRoomPerson person=roomPersons.get(i);
            person.setMeetingRoomId(room.getId());
            meetingRoomPersonService.save(person);
        }
        return roomPersons;
    }
}
