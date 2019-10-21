package com.fosung.ksh.meeting.client;

import com.fosung.ksh.common.response.ResponseResult;
import com.fosung.ksh.meeting.dto.MeetingRoomPerson;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihua
 * @date 2019-05-02 10:29
 */
@FeignClient(value = "ksh-laizhou-meeting", path = "/meeting-join-record")
public interface MeetingJoinRecordClient {
    @RequestMapping(value = "/leave", method = RequestMethod.POST)
    public MeetingRoomPerson leave(@RequestParam("hstRoomId") Integer hstRoomId,
                                   @RequestParam("userHash") String userHash);


}
