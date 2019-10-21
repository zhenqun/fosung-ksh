package com.fosung.ksh.meeting.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.ksh.meeting.constant.UserType;
import com.fosung.ksh.meeting.entity.MeetingRoomPerson;
import com.fosung.ksh.sys.dto.SysUser;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 参会人员授权
 *
 * @author wangyh
 */
public interface MeetingRoomPersonService extends AppBaseDataService<MeetingRoomPerson, Long> {


    /**
     * 查询未授权的灯塔用户信息
     *
     * @param meetingRoomId
     * @param orgId
     * @return
     */
    public List<SysUser> queryNotRightDTList(Long meetingRoomId, String orgId);

    /**
     * 根据用户Hash和会议室Id,获取授权的用户
     *
     * @param meetingRoomId
     * @param userHash
     * @return
     */
    public MeetingRoomPerson get(Long meetingRoomId, String userHash);
    /**
     * 查询未授权的本地用户信息
     *
     * @param meetingRoomId
     * @param orgId
     * @return
     */
    public List<SysUser> queryNotRightLocalList(Long meetingRoomId, String orgId);

    /**
     * 统计签到人数
     *
     * @return
     */
    public Map<String, Integer> signInNum(Long meetingRecordId, String orgId, UserType userType);

    /**
     * 查询签到列表
     *
     * @return
     */
    public List<MeetingRoomPerson> signInList(Long meetingRoomId, String orgId ,UserType userType);

    /**
     * 授权用户批量新增删除
     *
     * @param personList
     */
    public void batchUpdate(List<MeetingRoomPerson> personList);

    /**
     * 授权用户批量新增删除
     *
     * @param personList
     */
    public void batchUpdate(List<MeetingRoomPerson> personList,Long meetingRoomId);

    /**
     * 用户签到
     *
     * @param meetingRoomId     签到的会议记录ID
     * @param meetingRoomPerson 签到人员信息
     * @return
     */
    public ResponseParam sign(Long meetingRoomId, MeetingRoomPerson meetingRoomPerson) throws UnsupportedEncodingException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    /**
     * 正在接入站点数量
     */
    int sitesIng(String orgId);

    // 本年度接入站点数量
    int sitesAllYear(String orgId);

    //获取会议室应参加党组织数
    List<Map<String,Object>> meeetingRoomOrgNum(Map<String, Object> searchParam);
    //获取会议室应参加人数
    Integer meeetingRoomPersonNum(Map<String, Object> searchParam);

    int sitesNum(Map<String,Object>  paramMap);

    List<Map<String, Object>> listOrgId( Map<String, Object> searchParam);
}
