package com.fosung.ksh.meeting.control.hst.controller;


import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.meeting.control.hst.request.org.AddOrgRequestDTO;
import com.fosung.ksh.meeting.control.hst.request.org.EditOrgRequestDTO;
import com.fosung.ksh.meeting.control.hst.service.DepartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 好视通用户同步API
 *
 * @author toquery
 * @version 1
 */
@Slf4j
@RestController
@RequestMapping(value = "/org")
@Api(description = "好视通组织同步接口", tags = "会控组织接口")
public class OrgController extends AppIBaseController {

    @Resource
    private DepartService departService;

    /**
     * 新增用户
     */
    @ApiOperation(value = "新增组织")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Integer addUserinfo(@RequestBody AddOrgRequestDTO org) throws Exception {
        return departService.addOrg(org);
    }


    /**
     * 修改用户
     */
    @ApiOperation(value = "修改组织")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public void editRoominfo(@RequestBody EditOrgRequestDTO org) throws Exception {
        departService.editOrg(org);
    }


    @ApiOperation(value = "根据数据orgId获取好视通组织的ID")
    @RequestMapping(value = "get-by-org-id", method = RequestMethod.POST)
    public Integer addUserinfo(@RequestParam String orgId) throws Exception {
        Assert.notNull(orgId, "党组织Id不能为空");
        Integer departId = departService.getDepartinfo(0, "", orgId, 0, 20);
        return departId;
    }

    @ApiOperation(value = "查询党组织信息")
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public List<Map> queyOrgList(@RequestParam Integer departId,
                                 @RequestParam(required = false) String departName,
                                 @RequestParam(required = false)  String orgId,
                                 @RequestParam Integer currPage,
                                 @RequestParam Integer pageSize) throws Exception {
        return departService.queyOrgList(departId, departName, orgId, currPage, pageSize);
    }

}
