package com.fosung.ksh.meeting.control.client;

import com.fosung.ksh.meeting.control.constant.RoomStatus;
import com.fosung.ksh.meeting.control.dto.room.AddRoomRequestDTO;
import com.fosung.ksh.meeting.control.dto.room.EditRoomRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 好视通视频会议会议室模块接口
 *
 * @author wangyh
 */
@FeignClient(name = "ksh-meeting-control", path = "/meeting-room" )
public interface RoomClient {

    /**
     * 新增会议室
     *
     * @param requestDto
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Integer create(@RequestBody AddRoomRequestDTO requestDto);


    /**
     * 修改视频会议基本参数
     *
     * @param roominfoRequestDto
     * @throws Exception
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public void editRoominfo(@RequestBody EditRoomRequestDTO roominfoRequestDto);

    /**
     * 可以让会议室一次授权多个用户的访问权限。有获得授权的用户才能参加会议。
     *
     * @throws Exception
     */
    @RequestMapping(value = "/do-user-right", method = RequestMethod.POST)
    public void doUserRightByRoomid(@RequestBody AddRoomRequestDTO requestDto);


    /**
     * 根据会议室ID删除会议室
     *
     * @param roomId
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delRoominfo(@RequestParam("roomId") Integer roomId);

    /**
     * 获取会议室当前在线人数
     *
     * @param roomId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "cur-user-count", method = RequestMethod.POST)
    public Integer getCurUserCount(@RequestParam(value = "roomId") Integer roomId);

    /**
     * 获取加入会议室地址
     *
     * @param roomId
     */
    @RequestMapping(value = "/join-meeting-url", method = RequestMethod.POST)
    public String joinMeetingUrl(@RequestParam(required = false, value = "roomId") Integer roomId,
                                 @RequestParam(required = false, value = "userName") String userName,
                                 @RequestParam(required = false, value = "userAuth") String userAuth,
                                 @RequestParam(required = false, value = "autoCheck") Integer autoCheck);


    /**
     * 更新会议室状态
     *
     * @param roomId
     */
    @RequestMapping(value = "edit-status", method = RequestMethod.POST)
    public void editRoomStatus(@RequestParam(required = false, value = "roomId") Integer roomId,
                               @RequestParam(required = false, value = "status") RoomStatus status);

    /**
     * 获取视频会议巡查地址
     *
     * @param roomId
     */
    @RequestMapping(value = "/patrol-meeting-url", method = RequestMethod.POST)
    public String patrolMeetingUrl(@RequestParam(required = false, value = "roomId") Integer roomId,
                                   @RequestParam(required = false, value = "userName") String userName);

    /**
     * 判定某个用户是否在会议室中
     *
     * @param roomId
     * @param userName
     * @return
     */
    @RequestMapping(value = "user-in-room", method = RequestMethod.POST)
    public Boolean userInRoom(@RequestParam(required = false, value = "roomId") Integer roomId,
                              @RequestParam(required = false, value = "userName") String userName);


}
