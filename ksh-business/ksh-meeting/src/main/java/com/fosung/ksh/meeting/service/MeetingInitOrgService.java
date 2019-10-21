package com.fosung.ksh.meeting.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.meeting.entity.MeetingInitOrg;

public interface MeetingInitOrgService
        extends AppBaseDataService<MeetingInitOrg, Long> {
    /**
     * 初始化好视通
     */
    public void sync();
}
