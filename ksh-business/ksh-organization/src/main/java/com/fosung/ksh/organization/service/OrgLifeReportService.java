package com.fosung.ksh.organization.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.organization.entity.OrgLifeReport;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wangyh
 */
public interface OrgLifeReportService extends AppBaseDataService<OrgLifeReport, Long> {

    void saveAndReport(OrgLifeReport orgLiftReport);

    void updateAndReport(OrgLifeReport orgLiftReport, Set<String> updateFieldsFull);

    /**
     * 获取当月未生成报备记录的报备
     */
    List<OrgLifeReport> findNoRecord();

    Page<OrgLifeReport> custQueryByPage(Map<String, Object> searchParam, int pageNum, int pageSize);
}
