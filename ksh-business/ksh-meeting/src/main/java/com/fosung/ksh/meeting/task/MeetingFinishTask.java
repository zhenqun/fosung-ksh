package com.fosung.ksh.meeting.task;

import com.fosung.ksh.meeting.constant.MeetingStatus;
import com.fosung.ksh.meeting.constant.RoomType;
import com.fosung.ksh.meeting.entity.MeetingRoom;
import com.fosung.ksh.meeting.service.MeetingJoinRecordService;
import com.fosung.ksh.meeting.service.MeetingRoomService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangyihua
 * @date 2019-05-01 21:05
 */
@Slf4j
@Component
public class MeetingFinishTask {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private MeetingJoinRecordService joinRecordService;


    /**
     * 持续1小时无人，会议室自动结束
     */
    private static final long TIME = 1000L * 60 * 60;

    /**
     * 持续1小时无人，会议室自动结束
     */
    private static final long FINISH_TIME = 1000L * 60 * 60 * 24;


    @Scheduled(cron = "0 0/1 * * * ?")
    public void autoStartTask() throws Exception {
        int i = meetingRoomService.start();
        log.debug("共启动{}个会议室。", i);
    }

    /**
     * 自动结束会议室
     */
    @Scheduled(fixedDelay = 1000L * 60 * 1)
    public void autoFinishTask() {
        Date now = new Date();
        int k = 0;
        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("meetingStatus", MeetingStatus.GOING);
        searchParam.put("roomType", RoomType.FIXED);

        List<MeetingRoom> meetingRoomPage = meetingRoomService.queryAll(searchParam);
        //获取当前在线人数
        for (MeetingRoom meetingRoom : meetingRoomPage) {
            try {
                searchParam = Maps.newHashMap();
                searchParam.put("leaveDate", now);
                searchParam.put("meetingRoomId", meetingRoom.getId());

                long currentUserNum = joinRecordService.count(searchParam);
                // 如果会议室中没有人
                if (currentUserNum == 0L) {
                    Date leaveDate = joinRecordService.getMaxLeaveDate(meetingRoom.getId());

                    // 如果没有人加入过会议室
                    if (leaveDate == null) {
                        if (now.getTime() - meetingRoom.getRealStartTime().getTime() > FINISH_TIME) {
                            meetingRoomService.close(meetingRoom.getId());
                        }
                    } else {
                        if (now.getTime() - leaveDate.getTime() > TIME) {
                            meetingRoomService.close(meetingRoom.getId());
                        }
                    }
                    k++;
                }
            } catch (Exception e) {
                log.error("会议室定时结束失败：hstRoomId={},错误信息：{}", meetingRoom.getHstRoomId(), ExceptionUtils.getStackTrace(e));
            }
        }

        int i = meetingRoomService.finish();
        log.debug("共关闭{}个会议室。", (i + k));
    }
}
