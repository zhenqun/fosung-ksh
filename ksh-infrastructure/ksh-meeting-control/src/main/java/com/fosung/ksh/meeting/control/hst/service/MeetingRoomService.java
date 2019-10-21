package com.fosung.ksh.meeting.control.hst.service;

import com.fosung.ksh.meeting.control.hst.config.constant.RoomStatus;
import com.fosung.ksh.meeting.control.hst.config.constant.SerachType;
import com.fosung.ksh.meeting.control.hst.config.constant.UserAuth;
import com.fosung.ksh.meeting.control.hst.request.room.AddRoomRequestDTO;
import com.fosung.ksh.meeting.control.hst.request.room.EditRoomRequestDTO;
import com.fosung.ksh.meeting.control.hst.request.room.UserRightByRoomDTO;
import com.fosung.ksh.meeting.control.hst.response.RoomResponseDTO;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 好视通会控服务
 *
 * @author wangyh
 */
public interface MeetingRoomService {


    /**
     * 创建会议室，并对用户进行授权
     *
     * @return
     */
    public Integer createMeetingRoom(AddRoomRequestDTO meetingRoomDTO) throws Exception;


    /**
     * 修改视频会议基本参数
     *
     * @param roominfoRequestDto
     * @throws Exception
     */
    public void editRoominfo(EditRoomRequestDTO roominfoRequestDto) throws Exception;

    /**
     * 可以让会议室一次授权多个用户的访问权限。有获得授权的用户才能参加会议。
     *
     * @param roomId
     * @param list
     * @throws Exception
     */
    public void doUserRightByRoomid(Integer roomId, List<UserRightByRoomDTO> list) throws Exception;

    /**
     * 根据会议室ID删除会议室
     *
     * @param roomId
     */
    public void delRoominfo(Integer roomId) throws Exception;

    /**
     * 更新会议室状态
     *
     * @param roomId
     */
    public void editRoomStatus(Integer roomId, RoomStatus status) throws Exception;

    /**
     * 可以获得会议室的配置信息，可用会议室id或会议室名称做搜索过滤条件。
     *
     * @param roomId
     * @param searchName
     * @param serachType 类型值1为前后模糊搜索，0全匹配
     * @param currPage
     * @param pageSize
     */
    public List<RoomResponseDTO> getRoominfoList(Integer roomId, String searchName, SerachType serachType, Integer currPage, Integer pageSize) throws Exception;

    /**
     * 获取会议室当前登录人数
     *
     * @return
     */
    public Integer getCurUserCount(Integer roomId) throws Exception;

    /**
     * 获取好视通客户端启动地址
     *
     * @param roomId   好视通ID
     * @param userName 用户名
     * @return
     */
    public String joinMeetingUrl(Integer roomId, String userName, UserAuth userAuth,Integer autoCheck) throws UnsupportedEncodingException;

    /**
     * 获取好视通客户端视频会议巡查启动地址
     *
     * @param roomId   好视通ID
     * @param userName 用户名
     * @return
     */
    public String patrolMeetingUrl(Integer roomId, String userName) throws Exception;

    /**
     * 获取会议室当前登录人数
     *
     * @return
     */
    public Boolean userInRoom(Integer roomId, String userName) throws Exception;

}
