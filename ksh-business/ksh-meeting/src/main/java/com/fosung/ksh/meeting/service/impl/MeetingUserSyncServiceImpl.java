package com.fosung.ksh.meeting.service.impl;

import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.common.util.UtilBean;
import com.fosung.ksh.meeting.control.client.UserClient;
import com.fosung.ksh.meeting.control.dto.user.UserResponseDTO;
import com.fosung.ksh.meeting.control.dto.user.UserinfoRequestDTO;
import com.fosung.ksh.meeting.dao.MeetingUserSyncDao;
import com.fosung.ksh.meeting.entity.MeetingUserSync;
import com.fosung.ksh.meeting.entity.dto.MeetingUserSyncDTO;
import com.fosung.ksh.meeting.service.MeetingUserSyncService;
import com.fosung.ksh.sys.client.SysUserClient;
import com.fosung.ksh.sys.dto.SysUser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MeetingUserSyncServiceImpl extends AppJPABaseDataServiceImpl<MeetingUserSync, MeetingUserSyncDao>
        implements MeetingUserSyncService {

    @Autowired
    private UserClient ctrlUserClient;

    @Autowired
    private SysUserClient sysUserClient;

    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("idCard", "idCard:EQ");
            put("userHash", "userHash:EQ");
            put("userHashList", "userHash:IN");
        }
    };

    /**
     * 查询党组织下用户同步信息
     *
     * @param orgId
     * @return
     */
    public List<MeetingUserSyncDTO> query(String orgId, String userName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<SysUser> dtList = sysUserClient.queryDTUserByOrgId(orgId);
        List<SysUser> localList = sysUserClient.queryLocalUserByOrgId(orgId);
        dtList.addAll(localList);
        List<String> userHashList = dtList.stream().map(SysUser::getHash).collect(Collectors.toList());

        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("userHashList", userHashList);
        List<MeetingUserSync> dbList = queryAll(searchParam);

        Map<String, MeetingUserSync> faceMap = dbList.stream().collect(Collectors.toMap(MeetingUserSync::getUserHash, meetingUserFace -> meetingUserFace));
        List<MeetingUserSyncDTO> meetingUserFaceDTOList = Lists.newArrayList();

        for (SysUser sysUser : dtList) {
            MeetingUserSyncDTO dto = new MeetingUserSyncDTO();
            UtilBean.copyProperties(dto, sysUser);
            MeetingUserSync meetingUserSync = faceMap.get(sysUser.getHash());
            if (meetingUserSync != null) {
                dto.setMeetingUserSyncId(meetingUserSync.getId());
                dto.setSync(true);
            }
            meetingUserFaceDTOList.add(dto);

        }
        if (UtilString.isNotBlank(userName.trim())) {
            meetingUserFaceDTOList= meetingUserFaceDTOList.stream().filter(meetingUserFaceDTO -> meetingUserFaceDTO.getRealName().contains(userName))
                    .collect(Collectors.toList());
        }
        return meetingUserFaceDTOList;
    }

    /**
     * 同步数据到好视通
     *
     * @param user
     * @throws Exception
     */
    public void sync(UserinfoRequestDTO user) {
        ctrlUserClient.addUserinfo(user);
    }

    /**
     * 同步数据到好视通
     */
    @Override
    public void batchSync(String orgId) {
        List<MeetingUserSync> addList = Lists.newArrayList();
        List<MeetingUserSync> updateList = Lists.newArrayList();

        List<SysUser> rtList = sysUserClient.queryDTUserByOrgId(orgId);
        List<SysUser> localList = sysUserClient.queryLocalUserByOrgId(orgId);
        rtList.addAll(localList);

        List<String> userHashList = rtList.stream().map(SysUser::getHash).collect(Collectors.toList());

        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("userHashList", userHashList);
        List<MeetingUserSync> dbList = queryAll(searchParam);
        Map<String, MeetingUserSync> syncMap = dbList.stream().collect(Collectors.toMap(MeetingUserSync::getUserHash, meetingUserFace -> meetingUserFace));

        // 获取新增用户
        addList = getAddLIst(rtList, syncMap);

        // 获取修改用户
        updateList = getUpdateList(rtList, syncMap);


        for (MeetingUserSync meetingUserSync : addList) {
            try {
                ctrlUserClient.addUserinfo(userinfoRequestDTO(meetingUserSync));
                save(meetingUserSync);
            } catch (Exception e) {
                UserResponseDTO dto = ctrlUserClient.get(meetingUserSync.getUserHash());
                if (dto != null) {
                    save(meetingUserSync);
                } else {
                    log.error("用户同步失败，{}", ExceptionUtils.getStackTrace(e));
                }
            }
        }
        for (MeetingUserSync meetingUserSync : updateList) {
            ctrlUserClient.editUser(userinfoRequestDTO(meetingUserSync));
            update(meetingUserSync, Sets.newHashSet("realName", "orgId"));
        }
        log.info("\n党员同步结果：\n党组织：{}\n用户总数：{}\n新增用户：{}\n修改用户：{}", orgId, rtList.size(), addList.size(), updateList.size());
    }


    /**
     * 获取被修改的数据
     *
     * @param rtList
     * @param syncMap
     * @return
     */
    private List<MeetingUserSync> getUpdateList(List<SysUser> rtList, Map<String, MeetingUserSync> syncMap) {
        return rtList.stream().filter(sysUser -> {
            MeetingUserSync meetingUserSync = syncMap.get(sysUser.getHash());
            if (meetingUserSync == null) {
                return false;
            }
            return !(sysUser.getRealName().equals(meetingUserSync.getRealName())
                    && sysUser.getOrgId().equals(meetingUserSync.getOrgId()));
        }).map(sysUser -> {
            MeetingUserSync meetingUserSync = syncMap.get(sysUser.getHash());
            meetingUserSync.setOrgId(sysUser.getOrgId());
            meetingUserSync.setRealName(sysUser.getRealName());
            return meetingUserSync;
        }).collect(Collectors.toList());
    }

    /**
     * 获取新增的用户
     *
     * @param rtList
     * @param syncMap
     * @return
     */
    private List<MeetingUserSync> getAddLIst(List<SysUser> rtList, Map<String, MeetingUserSync> syncMap) {
        List<MeetingUserSync> addList;
        addList = rtList.stream().filter(sysUser -> syncMap.get(sysUser.getHash()) == null).map(sysUser -> {
            MeetingUserSync userSync = new MeetingUserSync();
            userSync.setUserHash(sysUser.getHash());
            userSync.setRealName(sysUser.getRealName());
            userSync.setOrgId(sysUser.getOrgId());
            return userSync;

        }).collect(Collectors.toList());

        return addList;
    }

    private UserinfoRequestDTO userinfoRequestDTO(MeetingUserSync meetingUserSync) {
        UserinfoRequestDTO u = new UserinfoRequestDTO();
        u.setNickName(meetingUserSync.getRealName());
        u.setOrgId(meetingUserSync.getOrgId());
        u.setUserName(meetingUserSync.getUserHash());
        return u;
    }

    @Override
    public Map<String, String> getQueryExpressions() {
        return this.expressionMap;
    }
}
