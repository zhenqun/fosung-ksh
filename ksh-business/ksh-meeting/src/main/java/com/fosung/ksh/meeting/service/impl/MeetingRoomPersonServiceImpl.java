package com.fosung.ksh.meeting.service.impl;

import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.ksh.common.util.DateUtil;
import com.fosung.ksh.common.util.UtilBean;
import com.fosung.ksh.meeting.constant.SigninType;
import com.fosung.ksh.meeting.constant.UserRight;
import com.fosung.ksh.meeting.constant.UserType;
import com.fosung.ksh.meeting.dao.MeetingRoomPersonDao;
import com.fosung.ksh.meeting.entity.MeetingJoinRecord;
import com.fosung.ksh.meeting.entity.MeetingRoomPerson;
import com.fosung.ksh.meeting.service.MeetingJoinRecordService;
import com.fosung.ksh.meeting.service.MeetingRoomPersonService;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.client.SysUserClient;
import com.fosung.ksh.sys.dto.SysUser;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyh
 */
@Slf4j
@Service
public class MeetingRoomPersonServiceImpl extends AppJPABaseDataServiceImpl<MeetingRoomPerson, MeetingRoomPersonDao> implements MeetingRoomPersonService {


    @Autowired
    private SysOrgClient sysOrgClient;

    @Autowired
    private SysUserClient sysUserClient;

    @Autowired
    private MeetingJoinRecordService meetingJoinRecordService;
    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("meetingRoomId", "meetingRoomId:EQ");
            put("userHash", "userHash:EQ");
            put("signInType", "signInType:ISNOTNULL");
            put("orgIds", "orgId:IN");
            put("userType", "userType:EQ");
            put("userRight", "userRight:EQ");
            put("notUserRight", "userRight:NEQ");
            put("userRights", "userRight:IN");

        }
    };


    /**
     * 查询未授权的灯塔用户信息
     *
     * @param meetingRoomId
     * @param orgId
     * @return
     */
    public List<SysUser> queryNotRightDTList(Long meetingRoomId, String orgId) {
        List<SysUser> users = sysUserClient.queryDTUserByOrgId(orgId);
        if (UtilCollection.isNotEmpty(users)) {
            users = users.stream().filter(user -> {
                MeetingRoomPerson person = get(meetingRoomId, user.getHash());
                return person == null || person.getUserRight() == UserRight.NOAUTH;
            }).collect(Collectors.toList());
        }
        return users;
    }


    /**
     * 查询未授权的本地用户信息
     *
     * @param meetingRoomId
     * @param orgId
     * @return
     */
    public List<SysUser> queryNotRightLocalList(Long meetingRoomId, String orgId) {
        List<SysUser> users = sysUserClient.queryLocalUserByOrgId(orgId);
        if (UtilCollection.isNotEmpty(users)) {
            users = users.stream().filter(user -> {
                MeetingRoomPerson person = get(meetingRoomId, user.getHash());
                return person == null || person.getUserRight() == UserRight.NOAUTH;
            }).collect(Collectors.toList());
        }
        return users;
    }


    /**
     * 统计签到人数
     *
     * @return
     */
    public Map<String, Integer> signInNum(Long meetingRoomId, String orgId, UserType userType) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        List<String> orgIds = sysOrgClient.queryOrgAllChildId(orgId);
        //应参加人数   1.userRight 不为null signInType为null
        Integer signNeed = this.entityDao.countSignNeed(meetingRoomId, orgIds, userType == null ? null : userType.name(), "userRight", null, null);

        //实际参加人数  1.userRight 为null signInType不为null
        Integer signSJ = this.entityDao.countSignNeed(meetingRoomId, orgIds, userType == null ? null : userType.name(), null, "signInType", null);

        //应参加党组织数
        Integer signOrgNeed = this.entityDao.countOrgSignNeed(meetingRoomId, orgIds, userType == null ? null : userType.name(), "userRight", null);

        //实际参加党组织数
        Integer signOrgSJ = this.entityDao.countOrgSignNeed(meetingRoomId, orgIds, userType == null ? null : userType.name(), null, "signInType");

        //未参加人数
        Integer noSign = this.entityDao.countSignNeed(meetingRoomId, orgIds, userType == null ? null : userType.name(), "userRight", null, "signNotType");
        result.put("needNum", signNeed);
        result.put("signNum", signSJ);
        result.put("needOrgNum", signOrgNeed);
        result.put("signOrgNum", signOrgSJ);
        result.put("notNum", noSign);
        return result;
    }

    /**
     * 查询签到列表
     *
     * @return
     */
    public List<MeetingRoomPerson> signInList(Long meetingRoomId, String orgId, UserType userType) {
        Map<String, Object> param = Maps.newHashMap();
        List<String> orgIds = sysOrgClient.queryOrgAllChildId(orgId);

        param.put("meetingRoomId", meetingRoomId);
        param.put("orgIds", orgIds);
        param.put("userType", userType);
        return this.queryAll(param);
    }


    /**
     * 查询党组织
     */
    public List<Map<String, Object>> listOrgId(Map<String, Object> searchParam) {

        return this.entityDao.getOrgByMeetingRoom(searchParam);
    }


    /**
     * 用户批量授权
     *
     * @param personList
     */
    public void batchUpdate(List<MeetingRoomPerson> personList, Long meetingRoomId) {
        Set<String> updateFields = new HashSet<String>() {{
            add("userRight");
        }};

        personList.stream().forEach(person -> {
            MeetingRoomPerson p = get(meetingRoomId, person.getUserHash());
            //如果当前用户已经存在，则修改授权信息
            if (p != null) {
                p.setUserRight(person.getUserRight());
                update(p, updateFields);
            } else {
                person.setMeetingRoomId(meetingRoomId);
                save(person);
            }
        });
    }


    /**
     * 授权用户批量新增删除
     *
     * @param personList
     */
    public void batchUpdate(List<MeetingRoomPerson> personList) {
        Set<String> updateFields = new HashSet<String>() {{
            add("userRight");
        }};

        personList.stream().forEach(person -> {
            MeetingRoomPerson p = get(person.getMeetingRoomId(), person.getUserHash());
            //如果当前用户已经存在，则修改授权信息
            if (p != null) {
                p.setUserRight(person.getUserRight());
                update(p, updateFields);
            } else {
                save(person);
            }
        });
    }


    /**
     * 根据用户Hash和会议室Id,获取授权的用户
     *
     * @param meetingRoomId
     * @param userHash
     * @return
     */
    public MeetingRoomPerson get(Long meetingRoomId, String userHash) {
        Map<String, Object> searchParam = new HashMap<String, Object>() {{
            put("meetingRoomId", meetingRoomId);
            put("userHash", userHash);
        }};
        List<MeetingRoomPerson> list = queryAll(searchParam);

        //如果数据已存在，则进行修改
        if (UtilCollection.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 正在接入站点数量
     */
    @Override
    public int sitesIng(String orgId) {
        List<String> orgIds = sysOrgClient.queryOrgAllChildId(orgId);


        return entityDao.sitesIng(orgIds);
    }

    // 本年度接入站点数量
    @Override
    public int sitesAllYear(String orgId) {
        List<String> orgIds = sysOrgClient.queryOrgAllChildId(orgId);

        return entityDao.sitesAllYear(orgIds, DateUtil.getYearFirst(), DateUtil.getYearLast());
    }

    @Override
    public List<Map<String, Object>> meeetingRoomOrgNum(Map<String, Object> searchParam) {
        return entityDao.meeetingRoomOrgNum(searchParam);
    }

    @Override
    public Integer meeetingRoomPersonNum(Map<String, Object> searchParam) {
        return entityDao.meeetingRoomPersonNum(searchParam);
    }

    @Override
    public int sitesNum(Map<String, Object> paramMap) {
        return entityDao.sitesNum(paramMap);
    }


    /**
     * 用户签到
     *
     * @param meetingRoomId     签到的会议记录ID
     * @param meetingRoomPerson 签到人员信息
     * @return
     */
    public ResponseParam sign(Long meetingRoomId, MeetingRoomPerson meetingRoomPerson) throws UnsupportedEncodingException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        ResponseParam res = null;
        Set<String> updateFields = Sets.newHashSet("signInType", "signInTime");

        if (meetingRoomPerson.getSignInType() == SigninType.FACE) {
            String userHash = URLDecoder.decode(meetingRoomPerson.getUserHash(), "UTF-8");
            meetingRoomPerson.setUserHash(userHash);
        }


        // 查询用户是否存在
        MeetingRoomPerson mrp = get(meetingRoomPerson.getMeetingRoomId(), meetingRoomPerson.getUserHash());

        if (mrp == null) {
            MeetingRoomPerson newPerson = getMeetingRoomPerson(meetingRoomPerson.getUserHash());
            newPerson.setMeetingRoomId(meetingRoomId);
            newPerson.setSignInType(meetingRoomPerson.getSignInType());
            newPerson.setUserRight(UserRight.UNAUTHORIZED);
            this.save(newPerson);
        } else {

            //如果未签到或者签到方式优先级较低，则修改签到方式和签到时间
            if (mrp.getSignInType() == null || meetingRoomPerson.getSignInType().getCode() > mrp.getSignInType().getCode()) {
                mrp.setSignInTime(new Date());
                mrp.setSignInType(meetingRoomPerson.getSignInType());
                update(mrp, updateFields);
            } else {
                res = ResponseParam.fail().message("用户已经签到");
            }
        }


        // 保存人员登录记录
        if (meetingRoomPerson.getSignInType() == SigninType.LOGIN) {
            MeetingJoinRecord meetingJoinRecord = new MeetingJoinRecord();
            UtilBean.copyProperties(meetingJoinRecord, meetingRoomPerson);
            meetingJoinRecord.setJoinDate(new Date());
            meetingJoinRecord.setId(null);
            meetingJoinRecordService.save(meetingJoinRecord);
        }
        return res == null ? ResponseParam.success() : res;
    }

    /**
     * 初始化签到人员信息
     *
     * @param hash
     * @return
     */
    private MeetingRoomPerson getMeetingRoomPerson(String hash) {
        // 用户不存在，执行新增操作
        SysUser sysUser = sysUserClient.getUserInfo(hash);

        MeetingRoomPerson person = null;
        person = new MeetingRoomPerson();
        person.setOrgId(sysUser.getOrgId());
        person.setTelephone(sysUser.getTelephone());
        person.setPersonName(sysUser.getRealName());
        person.setUserHash(hash);
        person.setUserRight(UserRight.HEARER);
        person.setSignInTime(new Date());
        return person;
    }


    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }
}
