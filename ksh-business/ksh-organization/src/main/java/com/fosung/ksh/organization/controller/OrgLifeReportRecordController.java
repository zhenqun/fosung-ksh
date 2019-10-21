package com.fosung.ksh.organization.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.dto.DtoUtil;
import com.fosung.ksh.common.dto.handler.KshDTOCallbackHandler;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.common.util.UtilEasyPoi;
import com.fosung.ksh.organization.entity.OrgLifeReportRecord;
import com.fosung.ksh.organization.service.OrgLifeReportRecordService;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.dto.SysOrg;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = OrgLifeReportRecordController.BASE_PATH)
public class OrgLifeReportRecordController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/orglife/report/record";

    @Autowired
    private OrgLifeReportRecordService orgLifeReportRecordService;


    @Autowired
    private SysOrgClient sysOrgClient;

    /**
     * 根据请求时提交的 branchId ，获取所有子集组织ID，并及移除 branchId
     */
    private Map<String, Object> handleBranchHandle() {
        //获取查询参数
        Map<String, Object> searchParam = getParametersStartingWith(getHttpServletRequest(), DEFAULT_SEARCH_PREFIX);
        String branchId = (String) searchParam.get("branchId");
        if (!Strings.isNullOrEmpty(branchId)){
            searchParam.remove("branchId");
            searchParam.put("branchIdIN", sysOrgClient.queryOrgAllChildId(branchId));
        }
        return searchParam;
    }
    /**
     * 记录分页查询
     *
     * @param pageNum  分页号，由0开始
     * @param pageSize 每页记录数，默认为10
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询")
    @PostMapping("query")
    public ResponseEntity<Page<OrgLifeReportRecord>> query(
            @RequestParam(required = false, value = "pagenum", defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @RequestParam(required = false, value = "pagesize", defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) throws ParseException {
        //获取查询参数
        Map<String, Object> searchParam = handleBranchHandle();

        //执行分页查询
        Page<OrgLifeReportRecord> orgLiftReportRecordPage = orgLifeReportRecordService.queryPageList(searchParam, pageNum, pageSize);
        DtoUtil.handler(orgLiftReportRecordPage.getContent(), getDtoCallbackHandler());

        return Result.success(orgLiftReportRecordPage);
    }


//    @ApiOperation(value = "分页查询")
//    @PostMapping("export")
//    public void export() throws ParseException {
//        //获取查询参数
//        Map<String, Object> searchParam = handleBranchHandle();
//
//        // searchParam.put("branchIdIN",this.getSubBranchIds());
//        List<OrgLifeReportRecord> orgLifeReportList = orgLifeReportRecordService.queryAll(searchParam);
//        UtilEasyPoi.exportExcel(orgLifeReportList, "组织生活报备记录", "组织生活报备记录", OrgLifeReportRecord.class, "组织生活报备记录.xls", getHttpServletResponse());
//    }

    /**
     * 获取详情数据。在url中隐藏id值 ， 查询数据对象的id以post参数方式提交。
     */
    @ApiOperation(value = "获取详情数据")
    @PostMapping("get")
    public ResponseEntity detail(@ApiParam("主键ID") @RequestParam Long id) {
        //查询组织生活报备记录
        OrgLifeReportRecord orgLiftReportRecord = orgLifeReportRecordService.get(id);

        return Result.success(orgLiftReportRecord);
    }

    /**
     * 获取详情数据。在url中隐藏id值 ， 查询数据对象的id以post参数方式提交。
     */
    @ApiOperation(value = "根据支部ID查询报备的主键ID")
    @PostMapping("get-id")
    public ResponseEntity<Long> detail(@ApiParam("支部ID") @RequestParam String branchId) {
        //查询组织生活报备记录
        Long id = orgLifeReportRecordService.getByBranchId(branchId);
        return Result.success(id);
    }


    /**
     * 月报统计查询
     */
    @ApiOperation(value = "月报统计查询")
    @PostMapping("month-report")
    public ResponseEntity monthReport(@ApiParam("组织Code") @RequestParam String orgCode, @ApiParam("年份") @RequestParam String year) {
        //查询组织生活报备记录
        List<Map<String, Object>> list = orgLifeReportRecordService.monthReport(orgCode, year);
        return Result.success(list);
    }


    /**
     * 获取PO到DTO转换的接口。主要用于在前端展示数据之前首先将数据格式处理完成。
     */
    public KshDTOCallbackHandler getDtoCallbackHandler() {
        //创建转换接口类
        return (KshDTOCallbackHandler<OrgLifeReportRecord>) dto -> {
            SysOrg sysOrg = sysOrgClient.getOrgInfo(dto.getOrgId());
            if (sysOrg != null) {
                dto.setOrgName(sysOrg.getOrgName());
            }

            SysOrg branchInfo = sysOrgClient.getOrgInfo(dto.getBranchId());
            if (branchInfo != null) {
                dto.setBranchName(branchInfo.getOrgName());
            }
        };
    }

}