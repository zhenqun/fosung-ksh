package com.fosung.ksh.meeting.client;

import com.fosung.ksh.common.response.ResponseResult;
import com.fosung.ksh.meeting.dto.MeetingRoom;
import com.fosung.ksh.meeting.dto.MeetingRoomPerson;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
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
@FeignClient(value = "ksh-laizhou-meeting", path = "/meeting-room-person")
public interface MeetingRoomPersonClient {
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public MeetingRoomPerson get(@RequestParam("meetingRoomId") Long meetingRoomId, @RequestParam("userHash") String userHash);


    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    public ResponseResult sign(@RequestBody MeetingRoomPerson meetingRoomPerson);


    /**
     * 签到结果
     * @param meetingRoomId
     * @param orgId
     * @return
     */
    @RequestMapping(value = "/sign/num", method = RequestMethod.POST)
    public Map<String, Object> signInNum(
            @RequestParam("meetingRoomId") Long meetingRoomId,
            @RequestParam("orgId") String orgId);

    /**
     * 签到结果
     * @param meetingRoomId
     * @param orgId
     * @return
     */
    @RequestMapping(value = "/sign/list", method = RequestMethod.POST)
    public List<MeetingRoomPerson> signInList(
            @RequestParam("meetingRoomId") Long meetingRoomId,
            @RequestParam("orgId") String orgId,
            @RequestParam(required = false) Boolean sign
    );
    @RequestMapping(value = "/shouldJoinOrg", method = RequestMethod.POST)
    public List<MeetingRoomPerson> shouldJoinOrg(
            @RequestParam("meetingRoomId") Long meetingRoomId,
            @RequestParam("orgId") String orgId
    );
    @RequestMapping(value = "/needJoinOrg", method = RequestMethod.POST)
    public List<MeetingRoomPerson> needJoinOrg(
            @RequestParam("meetingRoomId") Long meetingRoomId,
            @RequestParam("orgId") String orgId
    );

}
