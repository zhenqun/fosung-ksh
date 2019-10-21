package com.fosung.ksh.meeting.task;

import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.task.AppTaskCluster;
import com.fosung.ksh.common.util.UtilDateTime;
import com.fosung.ksh.meeting.constant.CycleFlag;
import com.fosung.ksh.meeting.constant.RoomType;
import com.fosung.ksh.meeting.constant.TemplateType;
import com.fosung.ksh.meeting.entity.MeetingTemplate;
import com.fosung.ksh.meeting.entity.MeetingTemplateCycle;
import com.fosung.ksh.meeting.service.MeetingTemplateCycleService;
import com.fosung.ksh.meeting.service.MeetingTemplateService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 循环会议室模版定时创建会议室
 */
public class MeetingCreateTask {
    @Resource
    private AppTaskCluster appTaskCluster;
    @Autowired
    private MeetingTemplateService meetingTemplateService;

    @Autowired
    private MeetingTemplateCycleService meetingTemplateCycleService;


    /**
     * 每天0点30分，定时创建
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws ParseException
     */
    @Scheduled(cron = "0 30 0 * * ?")
    public void dateCreatMeetingRoom() throws InvocationTargetException, IllegalAccessException, ParseException {
        if (appTaskCluster.needRunTask()) {
            Map<String, Object> queryMeetingTemplate = Maps.newHashMap();
            queryMeetingTemplate.put("templateType", TemplateType.CYCLE);
            List<MeetingTemplate> meetingTemplateList = meetingTemplateService.queryAll(queryMeetingTemplate);

            for (MeetingTemplate template : meetingTemplateList) {
                MeetingTemplateCycle cycle = meetingTemplateCycleService.getByTemplateId(template.getId());
                // 此处执行每天的数据
                Date date = null;

                if (cycle.getCycleFlag() == CycleFlag.DAILY) {
                    date = new Date();
                }
                // 每周第一天执行
                if (cycle.getCycleFlag() == CycleFlag.WEEKLY && UtilDateTime.getWeekDays(new Date()) == 2) {
                    date = UtilDateTime.plusDays(new Date(), cycle.getWeeks() - 2);
                }

                // 每月第一天的
                if (cycle.getCycleFlag() == CycleFlag.MONTHLY && UtilDateTime.getMonthDay(new Date()) == 1) {
                    date = UtilDateTime.plusDays(new Date(), cycle.getDateEveyMonth() - 1);
                }
                String startStr = UtilDateTime.getDateFormatStr(date, AppProperties.DATE_PATTERN) + " " + cycle.getWeekStartTime();
                String endStr = UtilDateTime.getDateFormatStr(date, AppProperties.DATE_PATTERN) + " " + cycle.getWeekEndTime();
                Date start = UtilDateTime.parse(startStr);
                Date end = UtilDateTime.parse(endStr);

                template.setHopeStartTime(start);
                template.setHopeEndTime(end);
                template.setRoomType(RoomType.HOPE);
                meetingTemplateService.convene(template);
            }
        }
    }
}
