package com.fosung.ksh.meeting.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.ksh.meeting.service.MeetingJoinRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Validated
@RestController
@RequestMapping(value = MeetingJoinRecordController.BASE_PATH)
public class MeetingJoinRecordController extends AppIBaseController {

    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/meeting-join-record";

    @Resource
    private MeetingJoinRecordService meetingJoinRecordService;

    @RequestMapping(value = "/leave", method = RequestMethod.POST)
    public ResponseParam leave(@RequestParam Integer hstRoomId,
                               @RequestParam String userHash) {
        meetingJoinRecordService.leave(hstRoomId, userHash, new Date());
        return ResponseParam.success();
    }

}
