package com.fosung.ksh.meeting.task;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.task.AppTaskCluster;
import com.fosung.ksh.meeting.service.MeetingRootOrgService;
import com.fosung.ksh.meeting.service.MeetingUserSyncService;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.dto.SysOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Component
public class SyncUserTask {


    @Autowired
    private MeetingUserSyncService userSyncService;

    @Autowired
    private MeetingRootOrgService rootOrgService;


    @Autowired
    private SysOrgClient sysOrgClient;


    @Resource
    private AppTaskCluster appTaskCluster;

    /**
     * 每天晚上三点，定时同步党员数据到好视通
     */
//    @Scheduled(cron = "0 0 3 * * ?")
    public void autoStartTask() {
//        if (appTaskCluster.needRunTask()) {
        List<String> orgList = rootOrgService.getRootOrgIdList();
        for (String s : orgList) {
            sync(s);
        }
        log.info("好视通用户同步结束。");
//        }
    }


    public void sync(String orgId) {
        userSyncService.batchSync(orgId);
        List<SysOrg> orgList = sysOrgClient.queryOrgInfo(orgId);
        if (UtilCollection.isNotEmpty(orgList)) {
            orgList.forEach(org -> {
                sync(org.getOrgId());
            });
        }

    }


}
