package com.fosung.ksh.organization.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.organization.entity.OrgLifeReportRecord;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author wangyh
 */
public interface OrgLifeReportRecordService extends AppBaseDataService<OrgLifeReportRecord, Long> {

    /**
     * 分页查询数据
     *
     * @param param
     * @return
     */
    public Page<OrgLifeReportRecord> queryPageList(Map<String, Object> param, Integer pageNum, Integer pageSize);

    /**
     * 月报
     * @param orgCode
     * @param year
     * @return
     */
    public List<Map<String,Object>> monthReport(String orgCode, String year);

    /**
     * 根据支部ID查询是否存在本月报备计划
     *
     * @param branchId
     * @return
     */
    public Long getByBranchId(String branchId);

    /**
     * 根据支部ID查询是否存在本月报备计划
     *
     * @param branchId
     * @return
     */
    public OrgLifeReportRecord getEntityByBranchId(String branchId);

    /**
     *
     * @param orgLifeId
     */
    public void updateByOrgLifeId(Long orgLifeId);

    /**
     *
     * @return
     */
    List<OrgLifeReportRecord> getOrgLifeIsNull();

    /**
     * 查询当月已经生成报备的
     * @return

    List<OrgLifeReportRecord> getThisMonth(); */
}
