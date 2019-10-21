package com.fosung.ksh.meeting.control.hst.controller;


import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.meeting.control.hst.config.constant.RoomStatus;
import com.fosung.ksh.meeting.control.hst.config.constant.UserAuth;
import com.fosung.ksh.meeting.control.hst.request.room.AddRoomRequestDTO;
import com.fosung.ksh.meeting.control.hst.request.room.EditRoomRequestDTO;
import com.fosung.ksh.meeting.control.hst.service.MeetingRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * 好视通视频会议创建服务接口API
 */
@Slf4j
@RestController
@Api(description = "好视通会控接口", tags = "会控会议室接口")
@RequestMapping(value = "/meeting-room")
public class MeetingRoomController extends AppIBaseController {

    @Autowired
    private MeetingRoomService meetingRoomService;


    /**
     * 新增会议室,并对会议室信息进行授权
     *
     * @param requestDTO
     * @return
     */
    @ApiOperation(value = "创建会议室",
            notes = "创建会议室，并对会议室进行授权，授权成功后，会返回在好视通生成的会议室ID")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ResponseEntity<Integer> createMeetingRoom(@RequestBody AddRoomRequestDTO requestDTO) throws Exception {
        return Result.success(meetingRoomService.createMeetingRoom(requestDTO));
    }


    @ApiOperation(value = "修改会议室基本信息")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public void editRoominfo(@RequestBody EditRoomRequestDTO requestDTO) throws Exception {
        meetingRoomService.editRoominfo(requestDTO);
    }

    /**
     * 根据会议室I，对用户进行授权
     *
     * @throws Exception
     */
    @ApiOperation(value = "根据会议室ID，对用户进行授权", response = Void.class)
    @RequestMapping(value = "do-user-right", method = RequestMethod.POST)
    public void doUserRightByRoomid(@RequestBody AddRoomRequestDTO requestDTO) throws Exception {
        meetingRoomService.doUserRightByRoomid(requestDTO.getRoomId(), requestDTO.getUserRightList());
    }

    /**
     * 根据会议室ID删除会议室
     *
     * @param roomId
     */
    @ApiOperation(value = "根据会议室ID，删除会议室", response = Void.class)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public void delRoominfo(Integer roomId) throws Exception {
        meetingRoomService.delRoominfo(roomId);
    }

    /**
     * 更新会议室状态
     *
     * @param roomId
     */
    @ApiOperation(value = "根据会议室ID，更新会议室状态")
    @RequestMapping(value = "edit-status", method = RequestMethod.POST)
    public void editRoomStatus(Integer roomId, RoomStatus status) throws Exception {
        meetingRoomService.editRoomStatus(roomId, status);
    }

    /**
     * 获取好视通客户端启动地址
     *
     * @param roomId   好视通ID
     * @param userName 用户名
     * @return
     */
    @ApiOperation(value = "获取会议室当前人数")
    @RequestMapping(value = "cur-user-count", method = RequestMethod.POST)
    public ResponseEntity<Integer> getCurUserCount(Integer roomId) throws Exception {
        return Result.success(meetingRoomService.getCurUserCount(roomId));
    }

    /**
     * 验证用户是否在会议室内
     *
     * @param roomId   好视通ID
     * @param userName 用户名
     * @return
     */
    @ApiOperation(value = "判定用户是否在某个会议室中")
    @RequestMapping(value = "user-in-room", method = RequestMethod.POST)
    public ResponseEntity<Boolean> userInRoom(Integer roomId, String userName) throws Exception {
        return Result.success(meetingRoomService.userInRoom(roomId, userName));
    }


    /**
     * 获取好视通客户端启动地址
     *
     * @param roomId   好视通ID
     * @param userName 用户名
     * @return
     */
    @ApiOperation(value = "获取好视通会议室启动地址")
    @RequestMapping(value = "join-meeting-url", method = RequestMethod.POST)
    public ResponseEntity<String> joinMeetingUrl(Integer roomId, String userName, UserAuth userAuth,Integer autoCheck) throws UnsupportedEncodingException {
        return Result.success(meetingRoomService.joinMeetingUrl(roomId, userName, userAuth,autoCheck));
    }

    /**
     * 获取好视通客户端视频会议巡查启动地址
     *
     * @param roomId   好视通ID
     * @param userName 用户名
     * @return
     */
    @ApiOperation(value = "获取好视通视频会议室巡查地址")
    @RequestMapping(value = "patrol-meeting-url", method = RequestMethod.POST)
    public ResponseEntity<String> patrolMeetingUrl(Integer roomId, String userName) throws Exception {
        return Result.success(meetingRoomService.patrolMeetingUrl(roomId, userName));
    }
}
