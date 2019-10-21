package com.fosung.ksh.meeting.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.meeting.control.dto.user.UserinfoRequestDTO;
import com.fosung.ksh.meeting.entity.MeetingUserSync;
import com.fosung.ksh.meeting.entity.dto.MeetingUserSyncDTO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface MeetingUserSyncService
        extends AppBaseDataService<MeetingUserSync, Long> {

    /**
     * 查询党组织下用户同步信息
     *
     * @param orgId
     * @return
     */
    public List<MeetingUserSyncDTO> query(String orgId, String userName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    /**
     * 同步数据到好视通
     *
     * @param user
     * @throws Exception
     */
    public void sync(UserinfoRequestDTO user) throws Exception;

    void batchSync(String orgId);
}
