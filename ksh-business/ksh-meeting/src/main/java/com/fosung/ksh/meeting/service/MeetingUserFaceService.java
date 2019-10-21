package com.fosung.ksh.meeting.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.meeting.entity.MeetingUserFace;
import com.fosung.ksh.meeting.entity.dto.MeetingUserFaceDTO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author wangyh
 */
public interface MeetingUserFaceService extends AppBaseDataService<MeetingUserFace, Long> {
    /**
     * 通过ORGID获取用户列表
     *
     * @param orgId
     * @return
     */

    public List<MeetingUserFaceDTO> queryByOrgId(String orgId, final String userName)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    /**
     * 根据userHash
     *
     * @param hash
     * @return
     */
    public MeetingUserFace get(String hash);

    /**
     * 人脸采集
     *
     * @param sysFace
     */
    public MeetingUserFace create(MeetingUserFace sysFace);


    /**
     * 获取人脸采集结果
     *
     * @param idCardList
     * @return
     */
    public List<MeetingUserFace> export(List<String> idCardList);
}
