package com.fosung.ksh.meeting.task;

import com.fosung.framework.task.AppTaskCluster;
import com.fosung.ksh.meeting.service.MeetingOrgSyncService;
import com.fosung.ksh.meeting.service.MeetingRootOrgService;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.dto.SysOrg;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用于定时同步党组织到好视通系统中去
 *
 * @author wangyihua
 * @date 2019-06-18 10:42
 */
@Slf4j
@Component
public class SyncOrgTask {


    @Autowired
    private MeetingOrgSyncService orgSyncService;

    @Autowired
    private MeetingRootOrgService rootOrgService;

    @Autowired
    private SysOrgClient sysOrgClient;

    @Autowired
    private SyncUserTask syncUserTask;

    @Resource
    private AppTaskCluster appTaskCluster;

    private static final String ROOT_ORG_ID = "030b9e46-b8ea-47ec-9feb-fb8c3eead801";

    /**
     * 每天1点，定时同步党组织到好视通
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void execute() {
        if(appTaskCluster.needRunTask()) {
            List<String> orgList = rootOrgService.getRootOrgIdList();

            SysOrg sysOrg = sysOrgClient.getOrgInfo(ROOT_ORG_ID);
            List<SysOrg> rootList = Lists.newArrayList(sysOrg);
            orgSyncService.batchSync(rootList, ROOT_ORG_ID);

            sync(ROOT_ORG_ID, orgList);
            log.info("好视通党组织同步结束。");


            //党组织同步完成后，执行用户同步
            syncUserTask.autoStartTask();

        }
    }

    public void sync(String orgId, List<String> dbList) {
        List<SysOrg> orgList = sysOrgClient.queryOrgInfo(orgId);
        orgSyncService.batchSync(orgList, orgId);
        orgList.stream()
                .filter(
                        org -> {
                            int level = org.getLevel() == null ? 0 : org.getLevel().intValue();
                            return org.getHasChildren()
                                    && (level != 3 || (level == 3 && dbList.contains(org.getOrgId())));
                        }).forEach(org -> sync(org.getOrgId(), dbList));
    }


}
