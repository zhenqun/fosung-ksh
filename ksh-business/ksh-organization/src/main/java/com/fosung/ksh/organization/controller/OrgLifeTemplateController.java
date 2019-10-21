package com.fosung.ksh.organization.controller;

import com.fosung.framework.common.dto.UtilDTO;
import com.fosung.framework.common.dto.support.DTOCallbackHandler;
import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.ksh.common.dto.DtoUtil;
import com.fosung.ksh.common.dto.handler.KshDTOCallbackHandler;
import com.fosung.ksh.common.response.ResponseResult;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.organization.constant.OrgTemplateType;
import com.fosung.ksh.organization.entity.OrgLifeTemplate;
import com.fosung.ksh.organization.service.OrgLifeTemplateService;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.dto.SysArea;
import com.fosung.ksh.sys.dto.SysOrg;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  组织生活模板
 */
@Api(description = "组织生活模板相关接口",
        tags = {"1、组织生活接口"},
        produces = "dengshichao")
@RestController
@RequestMapping(value = OrgLifeTemplateController.BASE_PATH)
public class OrgLifeTemplateController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/orglife/template";

    @Autowired
    private OrgLifeTemplateService orgLifeTemplateService;

    @Autowired
    private SysOrgClient sysOrgClient;


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
    public ResponseEntity<Page<OrgLifeTemplate>> query(
            @RequestParam(required = false, value = "pagenum", defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @RequestParam(required = false, value = "pagesize", defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) {
        //获取查询参数
        Map<String, Object> searchParam = getParametersStartingWith(getHttpServletRequest(), DEFAULT_SEARCH_PREFIX);
        //执行分页查询
        if (UtilString.isNotBlank((String) searchParam.get("isPush"))) {
            searchParam.put("isPush", "true".equals(searchParam.get("isPush")));
        }
        Page<OrgLifeTemplate> orgLifeTemplatePage = orgLifeTemplateService.queryByPage(searchParam, pageNum, pageSize);

        DtoUtil.handler(orgLifeTemplatePage.getContent(), getDtoCallbackHandler());

        return Result.success(orgLifeTemplatePage);
    }

    /**
     * 支部查询父级已经启用的模版
     */
    @ApiOperation(value = "支部查询父级已经启用的模版")
    @PostMapping("/push/list")
    public ResponseEntity detail(@ApiParam("组织IDs") @RequestParam String orgIds) {
        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("templateType", OrgTemplateType.BRANCH);
        searchParam.put("orgIds", orgIds.split(","));
        searchParam.put("isPush", true);
        List<OrgLifeTemplate> list = orgLifeTemplateService.queryAll(searchParam);
        return Result.success(list);
    }

    /**
     * 获取详情数据。在url中隐藏id值 ， 查询数据对象的id以post参数方式提交。
     */
    @ApiOperation(value = "获取详情数据")
    @PostMapping("get")
    public ResponseEntity<OrgLifeTemplate> detail(@RequestParam Long id) {

        //查询模板表
        OrgLifeTemplate orgLifeTemplate = orgLifeTemplateService.get(id);

        return Result.success(orgLifeTemplate);
    }

    /**
     * 保存实体对象 。在保存之前进行后端实体属性的验证，保证添加的数据符合业务要求。<br>
     * 如果实体id不为空，进行更新操作，否则进行添加。
     *
     */
    @ApiOperation(value = "保存或修改数据")
    @PostMapping("save")
    public ResponseEntity save(@Valid @RequestBody OrgLifeTemplate orgLifeTemplate) {
        //id不为空，进行更新操作，否则进行添加
        if (orgLifeTemplate.getId() != null) {
            //由请求参数中获取需要更新的字段
            Set<String> updateFields = getHttpServletRequest().getParameterMap().keySet();
            Set<String> fields = Sets.newHashSet("title","summary","content");
            fields.addAll(updateFields);
            //按照字段更新对象
            if (orgLifeTemplate.getIsPush()) {
                orgLifeTemplate.setPushTime(new Date());
                fields.add("pushTime");
            }

            if (orgLifeTemplate.getIsPush() != null){
                fields.add("isPush");
            }

            orgLifeTemplateService.update(orgLifeTemplate, fields);

            return Result.success();
        } else {
            orgLifeTemplateService.save(orgLifeTemplate);
            return Result.success();
        }
    }


    /**
     * 删除信息
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除信息")
    @PostMapping("delete")
    public ResponseEntity delete(@ApiParam("主键IDS") @RequestParam(required = true, value = "ids") String ids) {
        if (UtilString.isBlank(ids)) {
            return Result.failure();
        }
        //执行删除
        orgLifeTemplateService.delete(toLongIds(ids));
        return Result.success();
    }

    /**
     * 组织生活模板可用个数
     *
     * @return
     */
    @ApiOperation(value = "组织生活模板可用个数")
    @PostMapping("countNum")
    public ResponseEntity countNum(@ApiParam("组织ID") @RequestParam(value = "orgId") String orgId) {
        Integer num = orgLifeTemplateService.countNum(orgId);
        return Result.success(num);
    }

    /**
     * 获取PO到DTO转换的接口。主要用于在前端展示数据之前首先将数据格式处理完成。
     *
     */
    public KshDTOCallbackHandler getDtoCallbackHandler() {
        //创建转换接口类
        return (KshDTOCallbackHandler<OrgLifeTemplate>) dto -> {
            SysOrg sysOrg = sysOrgClient.getOrgInfo(dto.getOrgId());
            if (sysOrg != null) {
                dto.setOrgName(sysOrg.getOrgName());
            }
        };
    }


}