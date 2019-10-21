package com.fosung.ksh.meeting.control.hst.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilDate;
import com.fosung.framework.common.util.UtilString;
import com.fosung.ksh.common.response.ResponseResult;
import com.fosung.ksh.meeting.control.hst.config.HstProperties;
import com.fosung.ksh.meeting.control.hst.config.constant.*;
import com.fosung.ksh.meeting.control.hst.request.room.AddRoomRequestDTO;
import com.fosung.ksh.meeting.control.hst.request.room.EditRoomRequestDTO;
import com.fosung.ksh.meeting.control.hst.request.room.UserRightByRoomDTO;
import com.fosung.ksh.meeting.control.hst.response.RoomResponseDTO;
import com.fosung.ksh.meeting.control.hst.service.MeetingRoomService;
import com.fosung.ksh.meeting.control.hst.util.HstWebserviceUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 好视通会控服务
 *
 * @author wangyh
 */
@Slf4j
@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {

    /**
     * 配置项
     */
    @Autowired
    private HstProperties hstProperties;


    /**
     * 创建会议室，并对用户进行授权
     *
     * @return
     */
    public Integer createMeetingRoom(AddRoomRequestDTO meetingRoomDTO) throws Exception {

        Integer roomId = meetingRoomDTO.getRoomId();
        //如果roomId不存在，则创建会议室，否则修改会议室基本信息
        if (meetingRoomDTO.getRoomId() == null) {


            //第一步，创建会议室
            RoomResponseDTO responseDTO = addRoominfo(meetingRoomDTO);
            roomId = responseDTO.getRoomId();

            //第二步，设置会议室类型
            RoomType roomType = meetingRoomDTO.getRoomType();
            if (RoomType.FIXED == roomType) {
                editRoomTypeToFixed(roomId);
            } else {
                String startTime = UtilDate.getDateFormatStr(meetingRoomDTO.getHopeStartTime(), AppProperties.DATE_TIME_PATTERN);
                String endTime = UtilDate.getDateFormatStr(meetingRoomDTO.getHopeEndTime(), AppProperties.DATE_TIME_PATTERN);
                editRoomTypeToReservation(roomId, startTime, endTime);
            }


        } else {
            EditRoomRequestDTO editRoomRequestDTO = new EditRoomRequestDTO();
            BeanUtils.copyProperties(editRoomRequestDTO, meetingRoomDTO);
            editRoominfo(editRoomRequestDTO);
        }
        //第三步，对用户进行授权
        doUserRightByRoomid(roomId, meetingRoomDTO.getUserRightList());
        return roomId;
    }


    /**
     * 新增会议室
     *
     * @param requestDto 会议室详细信息
     * @return
     */
    public RoomResponseDTO addRoominfo(AddRoomRequestDTO requestDto) throws Exception {
        String url = hstProperties.getWebServiceUrl();
        String method = hstProperties.getMethod().getMeeting().getAddRoominfo();
        requestDto.setKeyCode(hstProperties.getKeyCode());

        Object[] params = requestDto.getValues();
        ResponseResult responseParam = HstWebserviceUtil.execute(url, method, params);

        List<Map> data = (List<Map>) responseParam.getData();
        Map map = data.get(0);
        RoomResponseDTO dto = new RoomResponseDTO();
        BeanUtils.populate(dto, map);
        return dto;
    }


    /**
     * 修改会议室为固定会议室
     *
     * @param roomId
     */
    public void editRoomTypeToFixed(Integer roomId) throws Exception {
        String url = hstProperties.getWebServiceUrl();
        String method = hstProperties.getMethod().getMeeting().getEditRoomTypeToFixed();
        Object[] args = new Object[]{
                roomId, hstProperties.getKeyCode()
        };
        HstWebserviceUtil.execute(url, method, args);
    }

    /**
     * 修改会议室为预约会议室
     *
     * @param startTime 会议开始时间
     * @param endTime   会议结束时间
     * @param roomId    会议室ID
     */
    public void editRoomTypeToReservation(Integer roomId, String startTime, String endTime) throws Exception {
        String url = hstProperties.getWebServiceUrl();
        String method = hstProperties.getMethod().getMeeting().getEditRoomTypeToReservation();

        Object[] args = new Object[]{
                roomId, startTime, endTime, hstProperties.getKeyCode()
        };
        HstWebserviceUtil.execute(url, method, args);
    }


    /**
     * 修改视频会议基本参数
     *
     * @param roominfoRequestDto
     * @throws Exception
     */
    public void editRoominfo(EditRoomRequestDTO roominfoRequestDto) throws Exception {
        String url = hstProperties.getWebServiceUrl();
        String method = hstProperties.getMethod().getMeeting().getEditRoominfo();
        roominfoRequestDto.setKeyCode(hstProperties.getKeyCode());
        Object[] params = roominfoRequestDto.getValues();
        HstWebserviceUtil.execute(url, method, params);
    }

    /**
     * 可以让会议室一次授权多个用户的访问权限。有获得授权的用户才能参加会议。
     *
     * @param roomId
     * @param list
     * @throws Exception
     */
    public void doUserRightByRoomid(Integer roomId, List<UserRightByRoomDTO> list) throws Exception {
        Assert.notEmpty(list, "待授权用户不能为空");

        String url = hstProperties.getWebServiceUrl();
        String method = hstProperties.getMethod().getMeeting().getDoUserRightByRoomid();
        String arrUserNameAndRight = "";
        for (UserRightByRoomDTO rightByRoom : list) {
            String str = rightByRoom.getUserName() + "," + rightByRoom.getAuth().getCode();
            arrUserNameAndRight = UtilString.isEmpty(arrUserNameAndRight) ? str : arrUserNameAndRight + "#" + str;
        }

        Object[] args = new Object[]{
                arrUserNameAndRight, roomId, hstProperties.getKeyCode()
        };
        HstWebserviceUtil.execute(url, method, args);
    }


    /**
     * 根据会议室ID删除会议室
     *
     * @param roomId
     */
    public void delRoominfo(Integer roomId) throws Exception {
        String url = hstProperties.getWebServiceUrl();
        String method = hstProperties.getMethod().getMeeting().getDelRoominfo();
        Object[] args = new Object[]{
                roomId, hstProperties.getKeyCode()
        };
        HstWebserviceUtil.execute(url, method, args);
    }

    /**
     * 更新会议室状态
     *
     * @param roomId
     */
    public void editRoomStatus(Integer roomId, RoomStatus status) throws Exception {
        String url = hstProperties.getWebServiceUrl();
        String method = hstProperties.getMethod().getMeeting().getEditRoomStatus();
        Object[] args = new Object[]{
                roomId, status.getCode(), hstProperties.getKeyCode()
        };
        HstWebserviceUtil.execute(url, method, args);
    }


    /**
     * 可以获得会议室的配置信息，可用会议室id或会议室名称做搜索过滤条件。
     *
     * @param roomId
     * @param searchName
     * @param serachType 类型值1为前后模糊搜索，0全匹配
     * @param currPage
     * @param pageSize
     */
    public List<RoomResponseDTO> getRoominfoList(Integer roomId, String searchName, SerachType serachType, Integer currPage, Integer pageSize) throws Exception {
        String url = hstProperties.getWebServiceUrl();
        String method = hstProperties.getMethod().getMeeting().getGetRoominfoList();
        Object[] args = new Object[]{roomId, searchName, serachType.getCode(), currPage, pageSize, hstProperties.getKeyCode()};
        ResponseResult responseParam = HstWebserviceUtil.execute(url, method, args);
        List<Map> list = (List<Map>) responseParam.getData();
        List<RoomResponseDTO> rooms = responseParam.datalist(list, RoomResponseDTO.class);
        return rooms;
    }


    /**
     * 获取会议室当前登录人数
     *
     * @return
     */
    public Integer getCurUserCount(Integer roomId) throws Exception {
        String url = hstProperties.getWebServiceUrl();
        String method = hstProperties.getMethod().getMeeting().getGetCurUserCount();
        Object[] args = new Object[]{roomId, hstProperties.getKeyCode()};
        ResponseResult responseParam = HstWebserviceUtil.execute(url, method, args);
        List<JSONArray> list = (List<JSONArray>) responseParam.getData();
        return list.get(0).getInteger(0);
    }


    /**
     * 获取会议室当前登录人数
     *
     * @return
     */
    public Boolean userInRoom(Integer roomId, String userName) throws Exception {
        String url = hstProperties.getWebServiceUrl();
        String method = hstProperties.getMethod().getMeeting().getUserInRoom();
        Object[] args = new Object[]{roomId, userName, hstProperties.getKeyCode()};
        ResponseResult responseParam = HstWebserviceUtil.execute(url, method, args);
        List<JSONArray> list = (List<JSONArray>) responseParam.getData();
        return UtilCollection.isNotEmpty(list);
    }


    /**
     * 获取好视通客户端启动地址
     *
     * @param roomId   好视通ID
     * @param userName 用户名
     * @return
     */
    public String joinMeetingUrl(Integer roomId, String userName, UserAuth userAuth,Integer autoCheck) throws UnsupportedEncodingException {
        Map<String, Object> uriVariables = Maps.newHashMap();
        if (UtilString.isNotBlank(userName)) {
            String encodeStr = URLEncoder.encode(userName, "UTF-8");
            uriVariables.put("userName", encodeStr);
        }
        if (roomId != null) {
            uriVariables.put("roomId", roomId);
        }
        // 主席6分屏，参会画中画，旁听1分屏
        if (userAuth == UserAuth.CHAIRMAN) {
            uriVariables.put("layoutCode", PCLayoutCode.WV6.name());
        } else if (userAuth == UserAuth.ATTENDEE) {
            uriVariables.put("layoutCode", PCLayoutCode.WV3.name());
        } else if (userAuth == UserAuth.HEARER) {
            uriVariables.put("layoutCode", PCLayoutCode.WV1.name());
        }

        String encodeStr = Base64Utils.encodeToString(hstProperties.getDefaultPwd().getBytes());
        uriVariables.put("userPwd", encodeStr);
        uriVariables.put("autoCheck",autoCheck);
        String url = hstProperties.getJoinMeetingUrl();
        url = UriComponentsBuilder.fromUriString(url).uriVariables(uriVariables).build().toUriString();
        return url;
    }

    /**
     * 获取好视通客户端视频会议巡查启动地址
     *
     * @param roomId   好视通ID
     * @param userName 用户名
     * @return
     */
    public String patrolMeetingUrl(Integer roomId, String userName) throws Exception {
        Assert.notNull(roomId, "会议室id不能为空。");
        Assert.notNull(userName, "用户名不能为空。");

        //获取用户巡查功能之前，先对用户进行授权
        makePatrolRight(roomId, userName);

        Map<String, Object> uriVariables = Maps.newHashMap();
        if (UtilString.isNotBlank(userName)) {
            String encodeStr = URLEncoder.encode(userName, "UTF-8");
            uriVariables.put("userName", encodeStr);
        }
        uriVariables.put("roomId", roomId);

        String encodeStr = Base64Utils.encodeToString(hstProperties.getDefaultPwd().getBytes());
        uriVariables.put("userPwd", encodeStr);

        String url = hstProperties.getPatrolMeetingUrl();
        url = UriComponentsBuilder.fromUriString(url).uriVariables(uriVariables).build().toUriString();
        return url;
    }

    /**
     * 巡查用户授权
     *
     * @param roomId
     * @param userName
     * @throws Exception
     */
    private void makePatrolRight(Integer roomId, String userName) throws Exception {
        UserRightByRoomDTO ur = new UserRightByRoomDTO();
        ur.setUserName(userName);
        ur.setAuth(UserAuth.CHAIRMAN);
        List<UserRightByRoomDTO> list = Lists.newArrayList();
        list.add(ur);
        doUserRightByRoomid(roomId, list);
    }
}
