package com.fosung.ksh.organization.service.impl;

import com.fosung.framework.dao.config.mybatis.page.MybatisPageRequest;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.organization.constant.OrgLifeReportStatus;
import com.fosung.ksh.organization.dao.OrgLifeReportRecordDao;
import com.fosung.ksh.organization.entity.OrgLifeReportRecord;
import com.fosung.ksh.organization.service.OrgLifeReportRecordService;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangyh
 */
@Service
public class OrgLifeReportRecordServiceImpl extends AppJPABaseDataServiceImpl<OrgLifeReportRecord, OrgLifeReportRecordDao>
        implements OrgLifeReportRecordService {

    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("orgLifeId", "orgLifeId:EQ");
            put("orgLifeIsNull", "orgLifeId:ISNULL");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }


    /**
     * 分页查询数据
     *
     * @param param
     * @return
     */
    @Override
    public Page<OrgLifeReportRecord> queryPageList(Map<String, Object> param, Integer pageNum, Integer pageSize) {
//        Page<OrgLifeReportRecord> page = this.entityDao.queryPageList(param, MybatisPageRequest.of(pageNum, pageSize));
        Page<OrgLifeReportRecord> page = null;
                Object statusObj = param.getOrDefault("status",null);
        // 如果获取到查询状态的参数
        if (statusObj != null && statusObj instanceof String && !Strings.isNullOrEmpty((String)statusObj)){
            String status = (String) statusObj;
            OrgLifeReportStatus orgLifeReportStatus = OrgLifeReportStatus.valueOf(status);
            switch (orgLifeReportStatus){
                case AFT:{
                    page = this.entityDao.queryAft(param, MybatisPageRequest.of(pageNum, pageSize));
                    break;
                }
                case BEF:{
                    page = this.entityDao.queryBef(param, MybatisPageRequest.of(pageNum, pageSize));
                    break;
                }
                case NORMAL:{
                    page = this.entityDao.queryNormal(param, MybatisPageRequest.of(pageNum, pageSize));
                    break;
                }
                case UNDO:{
                    page = this.entityDao.queryUndo(param, MybatisPageRequest.of(pageNum, pageSize));
                    break;
                }

            }
        } else {
            // 不带状态查询
            page = this.entityDao.queryPageList(param, MybatisPageRequest.of(pageNum, pageSize));
        }
        return page;
    }

    /**
     * 月报统计
     *
     * @param orgCode
     * @param year
     * @return
     */
    @Override
    public List<Map<String, Object>> monthReport(String orgCode, String year) {
        return this.entityDao.monthReport(orgCode, year);
    }

    /**
     * 根据支部ID查询是否存在本月报备计划
     *
     * @param branchId
     * @return
     */
    @Override
    public Long getByBranchId(String branchId) {
        return this.entityDao.getByBranchId(branchId);
    }

    @Override
    public OrgLifeReportRecord getEntityByBranchId(String branchId) {
        return this.entityDao.getOrgLifeReportRecordByBranchId(branchId);
    }

    @Override
    public void updateByOrgLifeId(Long orgLifeId) {
        this.entityDao.updateByOrgLifeId(orgLifeId);
    }

    @Override
    public List<OrgLifeReportRecord> getOrgLifeIsNull() {
        Map<String, Object> param = Maps.newHashMap();
        param.put("orgLifeIsNull", "23423");
//        param.put("orgLifeIsNull","23423");
//        param.put("orgLifeIsNull","23423");
        return super.queryAll(param);
    }
}
