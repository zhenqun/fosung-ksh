package com.fosung.ksh.organization.task;

import com.alibaba.fastjson.JSON;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.task.AppTaskCluster;
import com.fosung.ksh.organization.entity.OrgLife;
import com.fosung.ksh.organization.entity.OrgLifeBranch;
import com.fosung.ksh.organization.entity.OrgLifeReportRecord;
import com.fosung.ksh.organization.service.OrgLifeBranchService;
import com.fosung.ksh.organization.service.OrgLifeReportRecordService;
import com.fosung.ksh.organization.service.OrgLifeService;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Component
public class OrgLifeMapReport {

    @Resource
    private AppTaskCluster appTaskCluster;

    @Resource
    private OrgLifeService orgLifeService;

//    @Resource
//    private OrgLifeTypeService orgLifeTypeService;

    @Resource
    private OrgLifeBranchService orgLifeBranchService;

    @Resource
    private OrgLifeReportRecordService orgLifeReportRecordService;

    @Scheduled(cron = "0 0 23 * * ?")
    public void orgLifeMapReport() {

        boolean needRunTask = appTaskCluster.needRunTask();
        if (!needRunTask) {
            return;
        }

        // 获取到所有未关联组织生活的 报备
        List<OrgLifeReportRecord> orgLifeReportRecordList = orgLifeReportRecordService.getOrgLifeIsNull();

        if (UtilCollection.isEmpty(orgLifeReportRecordList)) {
            log.info("未找到 未关联组织生活的 报备");
            return;
        }

        // Map<String, List<OrgLifeReportRecord>> orgLifeReportRecordMap = orgLifeReportRecordList.stream().collect(Collectors.groupingBy(OrgLifeReportRecord::getBranchId));

        // 获取到主题党日(2253028057613312)类型的组织生活
        List<OrgLife> orgLifeList = orgLifeService.findThisMouthOrgLifeByTypeId("2253028057613312");
        if (UtilCollection.isEmpty(orgLifeList)) {
            log.info("未找到 当月 主题党日(2253028057613312)类型的组织生活 ");
            return;
        }

        // 通过未关联组织生活的报备，主题党日类型组织生活ID 查询 支部ID和组织生活对应关系
        Set<String> branchIds = orgLifeReportRecordList.stream().filter(item -> !Strings.isNullOrEmpty(item.getBranchId())).map(OrgLifeReportRecord::getBranchId).collect(Collectors.toSet());
        Map<String, Object> branchParam = Maps.newHashMap();
        branchParam.put("orgLifeIdIN", orgLifeList.stream().map(OrgLife::getId).collect(Collectors.toSet()));
        branchParam.put("branchIdIN", branchIds);
        List<OrgLifeBranch> orgLifeBranchList = orgLifeBranchService.queryAll(branchParam);

        if (UtilCollection.isEmpty(orgLifeBranchList)) {
            log.info("未找到 当月 主题党日(2253028057613312)类型 支部ID为 {} 组织生活 ", JSON.toJSONString(branchIds));
            return;
        }

        Map<String, List<OrgLifeBranch>> branchIdOrgLifeMap = orgLifeBranchList.stream().collect(Collectors.groupingBy(OrgLifeBranch::getBranchId));

        orgLifeReportRecordList.forEach(item -> {
            List<OrgLifeBranch> orgLifeBranches = branchIdOrgLifeMap.get(item.getBranchId());
            if (UtilCollection.isNotEmpty(orgLifeBranches)) {
                OrgLifeBranch orgLifeBranch = orgLifeBranches.get(0);
                item.setOrgLifeId(orgLifeBranch.getOrgLifeId());
                orgLifeReportRecordService.update(item, Sets.newHashSet("orgLifeId"));
            }
        });
    }


}
