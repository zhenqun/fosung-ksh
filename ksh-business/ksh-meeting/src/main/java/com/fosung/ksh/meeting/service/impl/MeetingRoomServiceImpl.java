package com.fosung.ksh.meeting.service.impl;

import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilDate;
import com.fosung.framework.dao.config.mybatis.page.MybatisPageRequest;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.common.util.DateUtil;
import com.fosung.ksh.meeting.constant.*;
import com.fosung.ksh.meeting.control.client.RoomClient;
import com.fosung.ksh.meeting.control.constant.RoomStatus;
import com.fosung.ksh.meeting.control.constant.UserAuth;
import com.fosung.ksh.meeting.control.dto.room.AddRoomRequestDTO;
import com.fosung.ksh.meeting.control.dto.room.UserRightByRoomDTO;
import com.fosung.ksh.meeting.dao.MeetingRoomDao;
import com.fosung.ksh.meeting.entity.MeetingRoom;
import com.fosung.ksh.meeting.entity.MeetingRoomPerson;
import com.fosung.ksh.meeting.service.MeetingRoomPersonService;
import com.fosung.ksh.meeting.service.MeetingRoomService;
import com.fosung.ksh.oauth2.client.Oauth2Client;
import com.fosung.ksh.oauth2.client.dto.SysUser;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.client.SysUserClient;
import com.fosung.ksh.sys.dto.SysOrg;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyh
 */
@Slf4j
@Service
public class MeetingRoomServiceImpl extends AppJPABaseDataServiceImpl<MeetingRoom, MeetingRoomDao> implements MeetingRoomService {


    @Autowired
    private MeetingRoomPersonService meetingRoomPersonService;

    @Autowired
    private RoomClient roomClient;

    @Autowired
    private SysUserClient sysUserClient;


    @Autowired
    private SysOrgClient sysOrgClient;

    @Autowired
    private Oauth2Client oauth2Client;

    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("roomName", "roomName:LIKE");
//            put("orgId", "orgId:EQ");
            put("orgIds", "orgId:IN");
            put("hstRoomId", "hstRoomId:EQ");
            put("meetingStatus", "meetingStatus:EQ");
            put("meetingType", "meetingType:EQ");
            put("haveMeetingStatus", "meetingStatus:ISNOTNULL");
        }
    };


    /**
     * 查询我的会议室
     *
     * @param pageNum
     * @param pageSize
     * @param orgId
     * @param userHash
     * @param roomName
     * @return
     */
    public Page<Map<String, Object>> queryMyMeetingList(String orgId, String userHash, String roomName,String meetingStatus ,int pageNum, int pageSize) {
        Page<Map<String, Object>> page = this.entityDao.queryMyMeetingList(orgId, roomName, userHash ,meetingStatus, MybatisPageRequest.of(pageNum, pageSize));
        page.stream().forEach(res -> {
            String meetingType = (String) res.get("meeting_type");
            res.put("meeting_type_dict", MeetingType.valueOf(meetingType).getRemark());
            res.put("hope_start_time", UtilDate.getDateFormatStr((Date) res.get("hope_start_time"), AppProperties.DATE_TIME_PATTERN));
            if(res.get("real_end_time")!=null) {
                res.put("real_end_time", UtilDate.getDateFormatStr((Date) res.get("real_end_time"), AppProperties.DATE_TIME_PATTERN));
            }
            SysOrg sysOrg = sysOrgClient.getOrgInfo((String) res.get("org_id"));
            if (sysOrg != null) {
                res.put("org_name", sysOrg.getOrgName());
            }
        });
        return page;
    }


    /**
     * 查询进行中的会议室
     *
     * @param orgId
     * @param roomName
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<MeetingRoom> queryGoingMeetingList(String orgId, String roomName, String meetingType, int pageNum, int pageSize) {
        List<String> orgIdList = sysOrgClient.queryOrgAllChildId(orgId);
        Page<MeetingRoom> meetingRoomPage = this.entityDao.queryGoingMeetingList(orgIdList,roomName, meetingType, MybatisPageRequest.of(pageNum, pageSize));
        return meetingRoomPage;
    }

    /**
     * 分类统计正在召开的会议室数量
     *
     * @param orgId
     * @return
     */
    public Map<String, Integer> countMeetingType(String orgId,String meetingType) {
        List<String> orgIds = sysOrgClient.queryOrgAllChildId(orgId);
        Map<String,Object>  paramMap=new HashMap<String, Object>();
        paramMap.put("orgIds",orgIds);
        paramMap.put("meetingType",meetingType);
        paramMap.put("meetingStatus","GOING");
        Integer num = this.entityDao.countMeetingNum(paramMap);
        Map<String, Integer> result = Maps.newHashMap();
        result.put("meetingNum", num);//正在召开视频会议

        Map<String,Object>  paramMap2=new HashMap<String, Object>();
        paramMap2.put("orgIds",orgIds);
        paramMap2.put("meetingType",meetingType);
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        paramMap2.put("time",dateString);
        Integer num2 = this.entityDao.countMeetingNum(paramMap2);
        result.put("meetingDataNum", num2);//今日召开视频会议

        int sitesNum=this.entityDao.sitesIng(paramMap2);
        result.put("sitesNum",sitesNum);

        //总召开视频会议
        Map<String,Object>  paramMap3=new HashMap<String, Object>();
        paramMap3.put("orgIds",orgIds);
        paramMap3.put("meetingType",meetingType);
        Integer num3 = this.entityDao.countMeetingNum(paramMap3);
        result.put("allNum", num3);//正在召开视频会议
        return result;
    }


    /**
     * 通过好视通会议室ID获取会议记录详细信息
     *
     * @param hstRoomId 好视通会议室ID
     * @return
     */
    public MeetingRoom getByHstRoomId(Integer hstRoomId) {
        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("hstRoomId", hstRoomId);
        List<MeetingRoom> list = queryAll(searchParam);
        if (UtilCollection.isNotEmpty(list)) {
            Assert.isTrue(list.size() == 1, "好视通ID" + hstRoomId + "对应多条数据，应该对应一条");
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取PC端登录会议室地址
     *
     * @param hstRoomId
     * @param userHash
     * @param userRight
     * @return
     */
    public String joinMeetingUrl(Integer hstRoomId, String userHash, UserRight userRight,Integer autoCheck) {
        return roomClient.joinMeetingUrl(hstRoomId, userHash, userRight.name(),autoCheck);
    }


    /**
     * 获取PC端视频会议巡查地址
     *
     * @param hstRoomId
     * @param userHash
     * @param
     * @return
     */
    public String patrolMeetingUrl(Integer hstRoomId, String userHash) {
        return roomClient.patrolMeetingUrl(hstRoomId, userHash);
    }

    /**
     * 保存并召开会议室
     *
     * @param meetingRoom
     * @param
     * @return
     */
    public MeetingRoom saveAndConvene(MeetingRoom meetingRoom) throws InvocationTargetException, IllegalAccessException {
        save(meetingRoom, Sets.newLinkedHashSet("roomName", "maxUserCount", "intervalTime"));
        convene(meetingRoom);
        return meetingRoom;
    }


    /**
     * 保存会议室数据
     *
     * @param meetingRoom
     * @param updateFields
     * @return
     */
    public MeetingRoom save(MeetingRoom meetingRoom, Set<String> updateFields) throws InvocationTargetException, IllegalAccessException {
        List<MeetingRoomPerson> persons = meetingRoom.getPersons();
        if (meetingRoom.getId() == null) {
            save(meetingRoom);
            persons = addChairman(persons);
        } else {
            update(meetingRoom, updateFields);
            if (get(meetingRoom.getId()).getHstRoomId() != null) {
                create(meetingRoom.getId());
            }
        }
        if (UtilCollection.isNotEmpty(persons)) {
            meetingRoomPersonService.batchUpdate(persons, meetingRoom.getId());
        }

        return meetingRoom;
    }


    /**
     * 召开会议室,会同步在会控接口中创建会议室
     *
     * @param meetingRoom
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public MeetingRoom convene(MeetingRoom meetingRoom) throws InvocationTargetException, IllegalAccessException {
        Set<String> updateFiles = Sets.newLinkedHashSet("roomType",
                "hopeStartTime", "hopeEndTime", "realStartTime", "realEndTime");
        //固定会议室则设置当前时间为预计开始时间
        if (meetingRoom.getRoomType() == RoomType.FIXED) {
            Date date = new Date();
            meetingRoom.setHopeStartTime(date);
            meetingRoom.setRealStartTime(date);
        } else if (meetingRoom.getRoomType() == RoomType.HOPE) {
            meetingRoom.setRealStartTime(meetingRoom.getHopeStartTime());
        }
        update(meetingRoom, updateFiles);
        return create(meetingRoom.getId());
    }

    /**
     * 创建会议室，如果会议室存在，则修改会议室基本信息，并重新进行授权
     *
     * @param meetingRoomId
     * @return
     */
    public MeetingRoom create(Long meetingRoomId) throws InvocationTargetException, IllegalAccessException {
        MeetingRoom meetingRoom = get(meetingRoomId);
        Assert.notNull(meetingRoom, "会议室" + meetingRoomId + "不存在");

        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("meetingRoomId", meetingRoomId);
        List<MeetingRoomPerson> persons = meetingRoomPersonService.queryAll(queryParams);
        persons=persons.stream().filter(person->person.getUserRight()!=UserRight.UNAUTHORIZED).collect(Collectors.toList());
        AddRoomRequestDTO roomDto = toHstAddRoominfoRequestDto(meetingRoom, persons);
        Integer roomId = roomClient.create(roomDto);
        meetingRoom.setHstRoomId(roomId);
        if (RoomType.FIXED == meetingRoom.getRoomType()) {
            meetingRoom.setMeetingStatus(MeetingStatus.GOING);
        } else if (RoomType.HOPE == meetingRoom.getRoomType()) {
            meetingRoom.setMeetingStatus(MeetingStatus.NOTSTART);
        }

        update(meetingRoom, Sets.newLinkedHashSet("hstRoomId", "meetingStatus"));
        return meetingRoom;
    }

    /**
     * 用户授权
     */
    public void userRight(Long meetingRoomId) {
        Map<String, Object> queryParams = Maps.newHashMap();
        queryParams.put("meetingRoomId", meetingRoomId);
        List<MeetingRoomPerson> persons = meetingRoomPersonService.queryAll(queryParams);
        List<UserRightByRoomDTO> list = toHstRightList(persons);
        AddRoomRequestDTO dto = new AddRoomRequestDTO();
        dto.setRoomId(get(meetingRoomId).getHstRoomId());
        dto.setUserRightList(list);
        roomClient.doUserRightByRoomid(dto);
    }

    /**
     * 根据会议室Id,关闭会议室
     *
     * @param meetingRoomId
     */
    public void close(Long meetingRoomId) {
        MeetingRoom meetingRoom = this.get(meetingRoomId);
        if(meetingRoom!=null) {
            meetingRoom.setMeetingStatus(MeetingStatus.FINISHED);
            meetingRoom.setRealEndTime(new Date());
            this.update(meetingRoom, Sets.newLinkedHashSet("meetingStatus", "realEndTime"));
            roomClient.editRoomStatus(meetingRoom.getHstRoomId(), RoomStatus.CLOSED);
        }
    }

    /**
     * 启动会议室
     *
     * @return
     */
    public int start() {
        return this.entityDao.start(new Date());
    }

    /**
     * 关闭会议室
     *
     * @return
     */
    public int finish() {
        return this.entityDao.finish(new Date());
    }


    /**
     * 设置当前登录用户为会议主席
     *
     * @param persons
     * @return
     */
    public List<MeetingRoomPerson> addChairman(List<MeetingRoomPerson> persons) {
        if (UtilCollection.isEmpty(persons)) {
            persons = Lists.newArrayList();
        }
        SysUser user = oauth2Client.userInfo();
        Assert.notNull(user, "当前用户登录超时，请刷新页面重新操作");
        //查看当前用户在persons 中吗
        for (int i=0;i<persons.size();i++){
            MeetingRoomPerson person=  persons.get(i);
            if(person.getUserHash().equals(user.getHash())){
               persons.remove(person);
                break;
            }
        }
        MeetingRoomPerson meetingRoomPerson = new MeetingRoomPerson();
        meetingRoomPerson.setUserHash(user.getHash());
        meetingRoomPerson.setPersonName(user.getRealName());
        meetingRoomPerson.setTelephone(user.getTelephone());
        meetingRoomPerson.setUserType(UserType.LOCAL);
        meetingRoomPerson.setUserRight(UserRight.CHAIRMAN);
        meetingRoomPerson.setOrgId(user.getOrgId());
        persons.add(meetingRoomPerson);
        return persons;
    }

    @Override
    public int sitesIng(Map<String,Object>  paramMap) {

        return this.entityDao.sitesIng(paramMap);
    }

    /**
     * 本年度会议数量
     *
     * @param
     */
    @Override
    public int meetingsAllYear(String orgId) {
        List<String> orgIds = sysOrgClient.queryOrgAllChildId(orgId);


        return this.entityDao.meetingsAllYear(orgIds, DateUtil.getYearFirst(), DateUtil.getYearLast());
    }

    /**
     * 转换好视通会控数据
     *
     * @return
     */
    private AddRoomRequestDTO toHstAddRoominfoRequestDto(MeetingRoom meetingRoom, List<MeetingRoomPerson> persons) throws InvocationTargetException, IllegalAccessException {
        AddRoomRequestDTO addRoominfoRequestDto = new AddRoomRequestDTO();
        BeanUtils.copyProperties(meetingRoom, addRoominfoRequestDto);
        addRoominfoRequestDto.setRoomId(meetingRoom.getHstRoomId());
        addRoominfoRequestDto.setRoomType(com.fosung.ksh.meeting.control.constant.RoomType.valueOf(meetingRoom.getRoomType().name()));
        addRoominfoRequestDto.setVerifyMode(meetingRoom.getVerifyMode().getCode());
        addRoominfoRequestDto.setEnableChairPwd(meetingRoom.getEnableChairPwd().getCode());
        addRoominfoRequestDto.setUserRightList(toHstRightList(persons));

        addRoominfoRequestDto.setCheckInType(meetingRoom.getSign().getCode());
        addRoominfoRequestDto.setCheckInTime(meetingRoom.getSignDuration());

        addRoominfoRequestDto.setAdditionalCheckInType(meetingRoom.getRepairSign().getCode());
        addRoominfoRequestDto.setAdditionalCheckInTime(meetingRoom.getRepairDuration());
        return addRoominfoRequestDto;
    }

    /**
     * 转换好视通用户授权列表
     *
     * @return
     */
    private List<UserRightByRoomDTO> toHstRightList(List<MeetingRoomPerson> persons) {
        List<UserRightByRoomDTO> hstList = Lists.newArrayList();
        persons.stream().forEach(p -> {
            UserRightByRoomDTO hstUR = new UserRightByRoomDTO();
            hstUR.setUserName(p.getUserHash());
            UserAuth hstAuthEnum = UserAuth.valueOf(p.getUserRight().name());
            hstUR.setAuth(hstAuthEnum);
            hstList.add(hstUR);
        });
        return hstList;
    }

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }



}
