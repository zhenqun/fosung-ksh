package com.fosung.ksh.meeting.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.meeting.constant.UserRight;
import com.fosung.ksh.meeting.entity.MeetingRoom;
import com.fosung.ksh.meeting.entity.MeetingRoomPerson;
import org.springframework.data.domain.Page;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wangyh
 */
public interface MeetingRoomService extends AppBaseDataService<MeetingRoom, Long> {


    /**
     * 通过好视通会议室ID获取会议记录详细信息
     *
     * @param hstRoomId 好视通会议室ID
     * @return
     */
    public MeetingRoom getByHstRoomId(Integer hstRoomId);

    /**
     * 查询我的会议室
     *
     * @param orgId
     * @param userHash
     * @param roomName
     * @return
     */
    public Page<Map<String, Object>> queryMyMeetingList(String orgId, String userHash, String roomName,String meetingStatus, int pageNum, int pageSize);

    /**
     * 查询正在进行中的会议室
     * @param orgId
     * @param roomName
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<MeetingRoom> queryGoingMeetingList(String orgId,String roomName, String meetingType, int pageNum, int pageSize);
    /**
     * 分类统计正在召开的会议室数量
     *
     * @param orgId
     * @return
     */
    public Map<String, Integer> countMeetingType(String orgId,String meetingType);

    /**
     * 获取PC端登录会议室地址
     *
     * @param hstRoomId
     * @param userHash
     * @param userRight
     * @return
     */
    public String joinMeetingUrl(Integer hstRoomId, String userHash, UserRight userRight,Integer autoCheck);


    /**
     * 获取PC端视频会议巡查地址
     *
     * @param hstRoomId
     * @param userHash
     * @param userRight
     * @return
     */
    public String patrolMeetingUrl(Integer hstRoomId, String userHash);

    /**
     * 保存或者修改会议室信息
     *
     * @param meetingRoom
     * @param updateFields
     * @return
     */
    public MeetingRoom save(MeetingRoom meetingRoom, Set<String> updateFields) throws InvocationTargetException, IllegalAccessException;

    /**
     * 召开会议室,会同步在会控接口中创建会议室
     *
     * @param meetingRoom
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public MeetingRoom convene(MeetingRoom meetingRoom) throws InvocationTargetException, IllegalAccessException;

    /**
     * 保存会议室基本信息，并召开会议室
     *
     * @param meetingRoom
     * @return
     */
    public MeetingRoom saveAndConvene(MeetingRoom meetingRoom) throws InvocationTargetException, IllegalAccessException;

    /**
     * 用户授权
     */
    public void userRight(Long meetingRoomId);

    /**
     * 根据会议室Id,关闭会议室
     *
     * @param meetingRoomId
     */
    public void close(Long meetingRoomId);

    /**
     * 关闭会议室
     *
     * @return
     */
    public int finish();

    /**
     * 启动会议室
     *
     * @return
     */
    public int start();

    /**
     * 本年度会议数量
     */
    int meetingsAllYear(String orgId);

    public MeetingRoom create(Long meetingRoomId) throws InvocationTargetException, IllegalAccessException;
    public List<MeetingRoomPerson> addChairman(List<MeetingRoomPerson> persons);

    int sitesIng (Map<String,Object>  paramMap);
}
