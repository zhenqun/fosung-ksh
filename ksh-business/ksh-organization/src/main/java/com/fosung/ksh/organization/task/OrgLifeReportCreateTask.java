package com.fosung.ksh.organization.task;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.task.AppTaskCluster;
import com.fosung.ksh.organization.entity.OrgLifeReport;
import com.fosung.ksh.organization.entity.OrgLifeReportRecord;
import com.fosung.ksh.organization.service.OrgLifeReportRecordService;
import com.fosung.ksh.organization.service.OrgLifeReportService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Component
public class OrgLifeReportCreateTask {

    @Resource
    private AppTaskCluster appTaskCluster;

    @Resource
    private OrgLifeReportService orgLifeReportService;

    @Resource
    private OrgLifeReportRecordService orgLifeReportRecordService;


    @Scheduled(cron = "0 0/10 * * * ?")
//    @Scheduled(cron = "0/1 * * * * ?")
    public void asyncMeetingNoticeTask() {
        boolean needRunTask = appTaskCluster.needRunTask();
        if (needRunTask) {
            log.info("{} 类执行任务，测试用！", this.getClass().getSimpleName());
        }
    }

    /**
     * 每月一号凌晨1点 0 0 1 1 * ?
     * 每月一号创建
     */
    @Scheduled(cron = "0 0 1 1 * ?")
//    @Scheduled(cron = "0/1 * * * * ?")
    public void createOrgLifeReport() {
        boolean needRunTask = appTaskCluster.needRunTask();
        if (!needRunTask) {
            return;
        }

        Calendar calendar = Calendar.getInstance();

        Map<String,Object> params = Maps.newHashMap();
       // params.put("1231",1);
        List<OrgLifeReport> orgLifeReportList = orgLifeReportService.queryAll(params);
        if (UtilCollection.isEmpty(orgLifeReportList)) {
            return;
        }
        List<OrgLifeReportRecord> lifeReportRecordList = Lists.newArrayList();
        for (int i = 0; i < orgLifeReportList.size(); i++) {
            OrgLifeReport orgLifeReport = orgLifeReportList.get(i);
            OrgLifeReportRecord orgLifeReportRecord = new OrgLifeReportRecord();
            int day = orgLifeReport.getHopeDay() > calendar.get(Calendar.DAY_OF_MONTH) ? calendar.getActualMaximum(Calendar.DAY_OF_MONTH) : orgLifeReport.getHopeDay();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), day);

            System.out.println(calendar.getTime());
            orgLifeReportRecord.setHopeDate(calendar.getTime());
            orgLifeReportRecord.setReportId(orgLifeReport.getId());
            orgLifeReportRecord.setBranchId(orgLifeReport.getBranchId());
            orgLifeReportRecord.setBranchCode(orgLifeReport.getOrgCode());
            orgLifeReportRecord.setOrgId(orgLifeReport.getOrgId());
            lifeReportRecordList.add(orgLifeReportRecord);
        }
        orgLifeReportRecordService.saveBatch(lifeReportRecordList);

    }
}
