package com.fosung.ksh.meeting.client;

import com.fosung.ksh.meeting.dto.MeetingRoom;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * @author wangyihua
 * @date 2019-05-02 10:29
 */
@FeignClient(value = "ksh-laizhou-meeting", path = "/meeting-room")
public interface MeetingRoomClient {
    /**
     * 获取会议室详情
     *
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.POST)
    public MeetingRoom get(@RequestParam("id") Long id);

    /**
     * 获取会议室详情
     *
     * @return
     */
    @RequestMapping(value = "get-hst-id", method = RequestMethod.POST)
    public MeetingRoom getByHstRoomId(@RequestParam("hstRoomId") Integer hstRoomId);


    /**
     * 保存并召开会议室
     *
     * @param meetingRoom
     * @return
     */
    @RequestMapping(value = "save-and-convene", method = RequestMethod.POST)
    public MeetingRoom saveAndConvene(@RequestBody MeetingRoom meetingRoom);

    /**
     * 结束会议室
     *
     * @param id
     */
    @RequestMapping(value = "close", method = RequestMethod.POST)
    public void close(@RequestParam("id") Long id);

}
