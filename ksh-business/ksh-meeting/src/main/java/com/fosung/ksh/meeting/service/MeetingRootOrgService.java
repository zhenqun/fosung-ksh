package com.fosung.ksh.meeting.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.meeting.entity.MeetingRootOrg;

import java.util.List;

public interface MeetingRootOrgService
        extends AppBaseDataService<MeetingRootOrg, Long> {
    List<String> getRootOrgIdList();
}
