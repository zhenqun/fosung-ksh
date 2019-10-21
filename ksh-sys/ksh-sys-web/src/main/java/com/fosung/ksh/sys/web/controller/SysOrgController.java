package com.fosung.ksh.sys.web.controller;

import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.sys.dto.SysOrg;
import com.fosung.ksh.sys.service.SysOrgService;
import com.google.common.collect.Lists;
import com.mzlion.core.utils.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangyh
 */
@Slf4j
@Api(description = "党组织接口", tags = {"2、党组织接口"})
@RestController
@RequestMapping(value = SysOrgController.BASE_PATH)
public class SysOrgController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/org";

    @Autowired
    private SysOrgService sysOrgService;

    @ApiOperation(value = "获取党组织详情")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity<SysOrg> getOrgInfo(
            @ApiParam("党组织ID") @RequestParam String orgId) {
        return Result.success(sysOrgService.getOrgInfo(orgId));
    }

    @ApiOperation(value = "获取下级党组织")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<List<Map<String, Object>>> queryOrgList(@ApiParam("党组织ID") @RequestParam String orgId,
                                                                  @ApiParam("党组织ID") @RequestParam(required = false) String orgName) {
        if (UtilString.isEmpty(orgName)) {
            List<SysOrg> list = sysOrgService.queryOrgList(orgId);
            List<Map<String, Object>> orgs = Lists.newArrayList();
            list.stream().forEach(sysOrg -> {
                orgs.add(BeanUtils.toMapAsValueObject(sysOrg));
            });
            return Result.success(orgs);
        }
        return Result.success(sysOrgService.queryOrgAllChild(orgId, orgName));
    }

    @ApiOperation(value = "获取下级党支部")
    @RequestMapping(value = "/branch", method = RequestMethod.POST)
    public ResponseEntity<List<SysOrg>> queryOrgList(@ApiParam("党组织ID") @RequestParam String orgId) {
        return Result.success(sysOrgService.queryOrgBranchInfo(orgId));
    }


    @ApiOperation(value = "获取所有下级党组织Id")
    @RequestMapping(value = "/all-id-list", method = RequestMethod.POST)
    public ResponseEntity<List<String>> queryAllChrildrenIdList(@ApiParam("党组织ID") @RequestParam String orgId) {
        List<SysOrg> orgs = sysOrgService.queryOrgAllChild(orgId);
        List<String> ids = Lists.newArrayList();
        orgs.stream().filter(org -> org != null).forEach(org -> {
            ids.add(org.getOrgId());
        });
        ids.add(orgId);
        return Result.success(ids);
    }

    /**
     * 获取上级
     * @param orgId
     * @return
     */
    @PostMapping(value = "all-parent")
    public ResponseEntity<List<SysOrg>> queryAllParentByOrgId(@RequestParam String orgId) {
        log.debug("llllllllllll");
        List<SysOrg> list = Lists.newArrayList();
        queryAllParentByOrgId(list, orgId);
        return Result.success(list);
    }
    /**
     * 递归获取当前节点的所有父级节点
     *
     * @param orgId
     */
    private void queryAllParentByOrgId(List<SysOrg> list, String orgId) {
        SysOrg sysOrg = sysOrgService.getOrgInfo(orgId);
        list.add(sysOrg);
        if (UtilString.isNotEmpty(sysOrg.getParentId())) {
            queryAllParentByOrgId(list, sysOrg.getParentId());
        }
    }

}
