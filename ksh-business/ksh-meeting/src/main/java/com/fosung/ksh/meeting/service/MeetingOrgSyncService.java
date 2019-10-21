package com.fosung.ksh.meeting.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.meeting.entity.MeetingOrgSync;
import com.fosung.ksh.sys.dto.SysOrg;

import java.util.List;

public interface MeetingOrgSyncService
        extends AppBaseDataService<MeetingOrgSync, Long> {
    /**
     * 同步数据到好视通
     */
    public void batchSync(List<SysOrg> rtList, String orgId);
}
