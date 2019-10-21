package com.fosung.ksh.organization.dao;

import com.fosung.framework.dao.config.mybatis.page.MybatisPage;
import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.framework.dao.jpa.annotation.MybatisQuery;
import com.fosung.ksh.organization.entity.OrgLifeReportRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author wangyh
 */
public interface OrgLifeReportRecordDao extends AppJPABaseDao<OrgLifeReportRecord, Long>{
    /**
     * 自定义分页查询
     * @param param
     * @param pageRequest
     * @return
     */
    @MybatisQuery
    public MybatisPage<OrgLifeReportRecord> queryPageList(@Param("param") Map<String, Object> param, Pageable pageRequest);

    /**
     * 根据支部ID查询是否存在本月报备计划
     * @param branchId
     * @return
     */
    @MybatisQuery
    public Long getByBranchId(@Param("branchId") String branchId);

    /**
     * 根据支部ID查询是否存在本月报备计划
     * @param branchId
     * @return
     */
    @MybatisQuery
    public OrgLifeReportRecord getOrgLifeReportRecordByBranchId(@Param("branchId") String branchId);

    /**
     *
     * @param orgLifeId
     */
    @MybatisQuery
    public void updateByOrgLifeId(@Param("orgLifeId") Long orgLifeId);

    /**
     * 月报统计
     * @param orgCode
     * @param year
     * @return
     */
    @MybatisQuery
    public List<Map<String,Object>> monthReport(@Param("orgCode") String orgCode, @Param("year") String year);

    /**
     * 查询延后召开的组织生活报备
     */
    @MybatisQuery
    MybatisPage<OrgLifeReportRecord> queryAft(@Param("param") Map<String, Object> param, PageRequest pageRequest);

    /**
     * 查询提前召开的组织生活报备
     */
    @MybatisQuery
    MybatisPage<OrgLifeReportRecord> queryBef(@Param("param") Map<String, Object> param, PageRequest pageRequest);

    /**
     * 查询正常召开的组织生活报备
     */
    @MybatisQuery
    MybatisPage<OrgLifeReportRecord> queryNormal(@Param("param") Map<String, Object> param, PageRequest pageRequest);
    /**
     * 查询未正常召开的组织生活报备
     */
    @MybatisQuery
    MybatisPage<OrgLifeReportRecord> queryUndo(@Param("param")Map<String, Object> param, PageRequest pageRequest);
}