package com.fosung.ksh.meeting.task;

import com.fosung.framework.task.AppTaskCluster;
import com.fosung.ksh.meeting.control.client.RoomClient;
import com.fosung.ksh.meeting.entity.MeetingJoinRecord;
import com.fosung.ksh.meeting.service.MeetingJoinRecordService;
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
 * @date 2019-06-28 16:37
 */
@Slf4j
@Component
public class JoinLeaveTask {

    private static final long TIME = 1000l * 60 * 60;

    @Autowired
    MeetingJoinRecordService joinRecordService;

    @Autowired
    RoomClient roomClient;

    @Resource
    private AppTaskCluster appTaskCluster;

    @Scheduled(fixedDelay = 1000L * 60 * 20)
    public void execute() {
        if (appTaskCluster.needRunTask()) {
            Date date = new Date();
            Map<String, Object> searchParam = Maps.newHashMap();
            searchParam.put("leaveDate", date);
            List<MeetingJoinRecord> list = joinRecordService.queryAll(searchParam);
            list = list.stream()
                    .filter(record -> date.getTime() - record.getJoinDate().getTime() > TIME).collect(Collectors.toList());
            for (MeetingJoinRecord meetingJoinRecord : list) {
                try {
                    if (!roomClient.userInRoom(meetingJoinRecord.getHstRoomId(), meetingJoinRecord.getUserHash())) {
                        meetingJoinRecord.setLeaveDate(new Date());
                        joinRecordService.update(meetingJoinRecord, Sets.newHashSet("leaveDate"));
                        log.debug("用户{}离开会议室{}。", meetingJoinRecord.getUserHash(), meetingJoinRecord.getHstRoomId());
                    }
                } catch (Exception e) {
                    log.error(ExceptionUtils.getStackTrace(e));
                }
            }
        }
    }


}
