package com.fosung.ksh.meeting.service.impl;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.aiface.client.AiFaceClient;
import com.fosung.ksh.common.util.UtilBean;
import com.fosung.ksh.common.util.UtilDT;
import com.fosung.ksh.meeting.dao.MeetingUserFaceDao;
import com.fosung.ksh.meeting.entity.MeetingUserFace;
import com.fosung.ksh.meeting.entity.dto.MeetingUserFaceDTO;
import com.fosung.ksh.meeting.service.MeetingUserFaceService;
import com.fosung.ksh.sys.client.SysUserClient;
import com.fosung.ksh.sys.dto.SysUser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author wangyh
 */
@Slf4j
@Service
public class MeetingUserFaceServiceImpl extends AppJPABaseDataServiceImpl<MeetingUserFace, MeetingUserFaceDao>
        implements MeetingUserFaceService {


    @Autowired
    private AiFaceClient aiFaceClient;

    @Autowired
    private SysUserClient sysUserClient;

    @Autowired
    private RedisTemplate redisTemplate;
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


    public List<MeetingUserFace> export(List<String> idCardList) {
        List<MeetingUserFace> list = Lists.newArrayList();
        for (String idCard : idCardList) {
            String key = "FACE COLLECTION_" + idCard;
            MeetingUserFace json = (MeetingUserFace) redisTemplate.opsForValue().get(key);
            if (json != null) {
                list.add(json);
            }
        }
        return list;
    }

    /**
     * 通过ORGID获取用户列表
     *
     * @param orgId
     * @return
     */

    public List<MeetingUserFaceDTO> queryByOrgId(String orgId, final String userName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<SysUser> dtUserList = sysUserClient.queryDTUserByOrgId(orgId);
        List<String> userHashList = dtUserList.stream().map(SysUser::getHash).collect(Collectors.toList());

        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("userHashList", userHashList);
        List<MeetingUserFace> faceList = queryAll(searchParam);
        Map<String, MeetingUserFace> faceMap = faceList.stream().collect(Collectors.toMap(MeetingUserFace::getUserHash, meetingUserFace -> meetingUserFace));
        List<MeetingUserFaceDTO> meetingUserFaceDTOList = Lists.newArrayList();

        for (SysUser sysUser : dtUserList) {
            MeetingUserFaceDTO meetingUserFaceDTO = new MeetingUserFaceDTO();
            UtilBean.copyProperties(meetingUserFaceDTO, sysUser);
            MeetingUserFace meetingUserFace = faceMap.get(sysUser.getHash());
            if (meetingUserFace != null) {
                meetingUserFaceDTO.setMeetingUserFaceId(meetingUserFace.getId());
                meetingUserFaceDTO.setImageUrl(meetingUserFace.getImageUrl());
            }
            meetingUserFaceDTOList.add(meetingUserFaceDTO);

        }
        if (UtilString.isNotBlank(userName.trim())) {
            meetingUserFaceDTOList= meetingUserFaceDTOList.stream().filter(meetingUserFaceDTO -> meetingUserFaceDTO.getRealName().contains(userName))
                    .collect(Collectors.toList());
        }
        return meetingUserFaceDTOList;
    }


    /**
     * 人脸采集
     *
     * @param meetingUserFace
     */
    public MeetingUserFace create(MeetingUserFace meetingUserFace) {
        if (UtilString.isEmpty(meetingUserFace.getUserHash()) && !UtilString.isIdCard(meetingUserFace.getIdCard())) {
            return createInfo(meetingUserFace, "身份证号错误");
        }
        String hash = UtilString.isNotEmpty(meetingUserFace.getUserHash()) ? meetingUserFace.getUserHash() : UtilDT.getUserNameIDHash(meetingUserFace.getPersonName(), meetingUserFace.getIdCard().toUpperCase());
        SysUser sysUser = sysUserClient.getUserInfo(hash);
        if (sysUser == null) {
            return createInfo(meetingUserFace, "未找到该用户信息，请检查身份证号与姓名是否正确");
        }

        try {
            meetingUserFace.setUserHash(hash);
            aiFaceClient.create(meetingUserFace.getPersonName(), URLEncoder.encode(hash, "utf-8"), meetingUserFace.getImageUrl());
        } catch (Exception e) {
            return createInfo(meetingUserFace, e.getMessage());
        }
        saveOrUpdate(meetingUserFace);
        return createInfo(meetingUserFace, "采集成功");
    }


    /**
     * 保存和修改
     */
    private void saveOrUpdate(MeetingUserFace meetingUserFace) {

        MeetingUserFace face = get(meetingUserFace.getUserHash());

        if (face != null) {
            meetingUserFace.setId(face.getId());
            update(meetingUserFace, Sets.newLinkedHashSet("imageUrl"));
        } else {
            meetingUserFace.setId(null);
            save(meetingUserFace);
        }
    }

    /**
     * 根据userHash
     *
     * @param hash
     * @return
     */
    public MeetingUserFace get(String hash) {
        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("userHash", hash);
        List<MeetingUserFace> list = queryAll(searchParam);
        return UtilCollection.isNotEmpty(list) ? list.get(0) : null;
    }

    private MeetingUserFace createInfo(MeetingUserFace sysFace, String message) {
        String key = "FACE COLLECTION_" + sysFace.getIdCard();
        sysFace.setMessage(message);
        redisTemplate.opsForValue().set(key, sysFace, 1, TimeUnit.HOURS);
        return sysFace;
    }


    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }
}
