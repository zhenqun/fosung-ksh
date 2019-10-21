package com.fosung.ksh.organization.task;

import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.task.AppTaskCluster;
import com.fosung.ksh.meeting.client.MeetingRoomClient;
import com.fosung.ksh.meeting.constant.MeetingStatus;
import com.fosung.ksh.meeting.dto.MeetingRoom;
import com.fosung.ksh.organization.constant.OrgLifeStatus;
import com.fosung.ksh.organization.entity.OrgLife;
import com.fosung.ksh.organization.service.OrgLifeService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangyihua
 * @date 2019-06-30 18:00
 */
@Slf4j
@Component
public class OrgLifeFinishTask {

    @Autowired
    OrgLifeService orgLifeService;

    @Autowired
    MeetingRoomClient roomClient;


    @Resource
    private AppTaskCluster appTaskCluster;

    @Scheduled(fixedDelay = 1000L * 60 * 2)
    public void execute() {
        if (appTaskCluster.needRunTask()) {
            Map<String, Object> searchParam = Maps.newHashMap();
            searchParam.put("orgLifeStatus", OrgLifeStatus.GOING);
            List<OrgLife> list = orgLifeService.queryAll(searchParam);
            list = list.stream().filter(orgLife -> orgLife.getMeetingRoomId() != null).collect(Collectors.toList());
            for (OrgLife orgLife : list) {
                try {
                    MeetingRoom meetingRoom = roomClient.get(orgLife.getMeetingRoomId());
                    if (meetingRoom != null && meetingRoom.getMeetingStatus() == MeetingStatus.FINISHED) {
                        orgLife.setOrgLifeStatus(OrgLifeStatus.FINISHED);
                        orgLife.setEndDate(new Date());
                        orgLifeService.update(orgLife, Sets.newHashSet("orgLifeStatus", "endDate"));
                        log.debug("组织生活已结束。\n{}", JsonMapper.toJSONString(orgLife, true));
                    }
                } catch (Exception e) {
                    log.error("组织生活状态同步失败：{}", ExceptionUtils.getStackTrace(e));
                }
            }
        }

    }
}
