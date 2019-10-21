package com.fosung.ksh.organization.controller;

import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.dto.DtoUtil;
import com.fosung.ksh.common.dto.handler.KshDTOCallbackHandler;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.common.util.DownloadFileUtil;
import com.fosung.ksh.common.util.UtilEasyPoi;
import com.fosung.ksh.organization.entity.OrgLifeReport;
import com.fosung.ksh.organization.service.OrgLifeReportService;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.dto.SysOrg;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wangyh
 */
@RestController
@RequestMapping(value = OrgLifeReportController.BASE_PATH)
public class OrgLifeReportController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/orglife/report";

    @Autowired
    private OrgLifeReportService orgLifeReportService;

    @Autowired
    private SysOrgClient sysOrgClient;



    /**
     * 根据请求时提交的 branchId ，获取所有子集组织ID，并及移除 branchId
     */
    private Map<String, Object> handleBranchHandle() {
        //获取查询参数
        Map<String, Object> searchParam = getParametersStartingWith(getHttpServletRequest(), DEFAULT_SEARCH_PREFIX);
        String branchId = (String) searchParam.get("branchId");
        if (!Strings.isNullOrEmpty(branchId)) {
            searchParam.remove("branchId");
            searchParam.put("branchIdIN", sysOrgClient.queryOrgAllChildId(branchId));
        }
        return searchParam;
    }

    /**
     * 记录分页查询
     * branchId -> 6cfdaecb-0485-41ee-bb3b-d39b437f6cf2
     *
     * @param pageNum  分页号，由0开始
     * @param pageSize 每页记录数，默认为10«
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询")
    @PostMapping("query")
    public ResponseEntity<Page<OrgLifeReport>> query(
            @RequestParam(required = false, value = "pagenum", defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @RequestParam(required = false, value = "pagesize", defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) {
        //获取查询参数
        Map<String, Object> searchParam = handleBranchHandle();

        //执行分页查询
        Page<OrgLifeReport> orgLiftReportPage = orgLifeReportService.custQueryByPage(searchParam, pageNum, pageSize);

        DtoUtil.handler(orgLiftReportPage.getContent(), getDtoCallbackHandler());

        return Result.success(orgLiftReportPage);
    }

    @ApiOperation(value = "模板")
    @RequestMapping("/import/template")
    public ResponseEntity importTemplate() {
        ResponseEntity<InputStreamResource> response = null;
        try {
            response = DownloadFileUtil.download("", "orglife-report-export.xlsx", "导入模板");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }

    @ApiOperation(value = "导入")
    @PostMapping("import")
    public ResponseEntity importReport(@RequestParam(value = "file") MultipartFile file, @RequestParam String orgId, @RequestParam String orgCode) {

        List<OrgLifeReport> orgLifeReportList = UtilEasyPoi.importExcel(file, 0, 1, OrgLifeReport.class);
        for (OrgLifeReport orgLifeReport : orgLifeReportList) {
            if (orgLifeReport.getHopeDay() == null || Strings.isNullOrEmpty(orgLifeReport.getBranchId()) || Strings.isNullOrEmpty(orgLifeReport.getAddress()) || Strings.isNullOrEmpty(orgLifeReport.getContent())) {
                return ResponseEntity.badRequest().body("导入数据信息不全，导入失败！");
            } else if (orgLifeReport.getHopeDay() > 31 || orgLifeReport.getHopeDay() <= 0) {
                return  ResponseEntity.badRequest().body("召开日错误，导入失败！");
            }

            orgLifeReport.setOrgCode(orgCode);
            orgLifeReport.setOrgId(orgId);
        }

        orgLifeReportService.saveBatch(orgLifeReportList);

        return ResponseEntity.ok("导入成功");
    }

    @ApiOperation(value = "导出")
    @RequestMapping("export")
    public void export() {
        //获取查询参数
        Map<String, Object> searchParam = handleBranchHandle();
        List<OrgLifeReport> orgLifeReportList = orgLifeReportService.queryAll(searchParam);
        DtoUtil.handler(orgLifeReportList, getDtoCallbackHandler());
        UtilEasyPoi.exportExcel(orgLifeReportList, "组织生活报备记录", "组织生活报备记录", OrgLifeReport.class, "组织生活报备记录.xls", getHttpServletResponse());
    }

    /**
     * 获取详情数据。在url中隐藏id值 ， 查询数据对象的id以post参数方式提交。
     */
    @ApiOperation(value = "获取详情数据")
    @PostMapping("get")
    public ResponseEntity<OrgLifeReport> detail(@ApiParam("主键ID") @RequestParam Long id) {
        //查询组织生活报备
        OrgLifeReport orgLiftReport = orgLifeReportService.get(id);

        return Result.success(orgLiftReport);
    }

    /**
     * 保存实体对象 。在保存之前进行后端实体属性的验证，保证添加的数据符合业务要求。<br>
     * 如果实体id不为空，进行更新操作，否则进行添加。
     */
    @ApiOperation(value = "保存或修改实体对象")
    @PostMapping("save")
    public ResponseEntity save(@Valid @RequestBody OrgLifeReport orgLiftReport) {
        //id不为空，进行更新操作，否则进行添加
        if (orgLiftReport.getId() != null) {
            //由请求参数中获取需要更新的字段
            Set<String> updateFields = getHttpServletRequest().getParameterMap().keySet();

            Set<String> updateFieldsFull = Sets.newHashSet(updateFields);

            updateFieldsFull.add("orgId");
            updateFieldsFull.add("orgCode");
            updateFieldsFull.add("branchId");
            updateFieldsFull.add("hopeDay");
            updateFieldsFull.add("address");
            updateFieldsFull.add("content");

            //按照字段更新对象
            orgLifeReportService.updateAndReport(orgLiftReport, updateFieldsFull);
            return Result.success();
        } else {
            orgLifeReportService.saveAndReport(orgLiftReport);
            return Result.success();
        }
    }


    /**
     * 删除信息
     */
    @ApiOperation(value = "删除信息")
    @PostMapping("delete")
    public ResponseEntity delete(@ApiParam("组件IDs") @RequestParam(required = true, value = "ids") String ids) {
        if (UtilString.isBlank(ids)) {
            return Result.failure();
        }
        //执行删除
        orgLifeReportService.delete(toLongIds(ids));

        return Result.success();
    }


    /**
     * 获取PO到DTO转换的接口。主要用于在前端展示数据之前首先将数据格式处理完成。
     */
    public KshDTOCallbackHandler getDtoCallbackHandler() {
        //创建转换接口类
        return (KshDTOCallbackHandler<OrgLifeReport>) dto -> {
            SysOrg sysOrg = sysOrgClient.getOrgInfo(dto.getBranchId());
            if (sysOrg != null) {
                dto.setBranchName(sysOrg.getOrgName());
            }
            String content = StringEscapeUtils.unescapeHtml4(dto.getContent());
            dto.setContent(content);
        };
    }
}