package com.fosung.ksh.organization.service.impl;

import com.fosung.framework.common.secure.auth.AppUserDetailsService;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.organization.dao.OrgLifeReportDao;
import com.fosung.ksh.organization.entity.OrgLifeReport;
import com.fosung.ksh.organization.entity.OrgLifeReportRecord;
import com.fosung.ksh.organization.service.OrgLifeReportRecordService;
import com.fosung.ksh.organization.service.OrgLifeReportService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wangyh
 */
@Service
public class OrgLifeReportServiceImpl extends AppJPABaseDataServiceImpl<OrgLifeReport, OrgLifeReportDao> implements OrgLifeReportService {

    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("orgId", "orgId:EQ");
            put("branchId", "branchId:EQ");
            put("branchIdIN", "branchId:IN");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }

    @Override
    public boolean isEnableQueryAllRecord() {
        return true;
    }

    @Resource
    private AppUserDetailsService appUserDetailsService;

    @Resource
    private OrgLifeReportDao orgLifeReportDao;

    @Resource
    private OrgLifeReportRecordService orgLifeReportRecordService;

    @Override
    public void saveAndReport(OrgLifeReport orgLifeReport) {
        super.save(orgLifeReport);
        OrgLifeReportRecord orgLifeReportRecord = orgLifeReportRecordService.getEntityByBranchId(orgLifeReport.getBranchId());
        if (orgLifeReportRecord == null) {
            orgLifeReportRecord = new OrgLifeReportRecord();

            Calendar calendar = Calendar.getInstance();
            int day = orgLifeReport.getHopeDay() > calendar.get(Calendar.DAY_OF_MONTH) ? calendar.getActualMaximum(Calendar.DAY_OF_MONTH) : orgLifeReport.getHopeDay();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), day);
            orgLifeReportRecord.setHopeDate(calendar.getTime());
            orgLifeReportRecord.setReportId(orgLifeReport.getId());
            orgLifeReportRecord.setBranchId(orgLifeReport.getBranchId());
            orgLifeReportRecord.setBranchCode(orgLifeReport.getOrgCode());
            orgLifeReportRecord.setOrgId(orgLifeReport.getOrgId());
            orgLifeReportRecordService.save(orgLifeReportRecord);
        }
    }

    @Override
    public void updateAndReport(OrgLifeReport orgLifeReport, Set<String> updateFieldsFull) {
        super.update(orgLifeReport, updateFieldsFull);
    }

    @Override
    public List<OrgLifeReport> findNoRecord() {
        //List<OrgLifeReportRecord> orgLifeReportRecordList = orgLifeReportRecordService.getThisMonth();
        return null;
    }

    @Transactional
    @Override
    public Page<OrgLifeReport> custQueryByPage(Map<String, Object> searchParam, int pageNum, int pageSize) {
        return super.queryByPage(searchParam, pageNum, pageSize);
    }
}
