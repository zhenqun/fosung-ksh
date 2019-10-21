package com.fosung.ksh.meeting.controller;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fosung.ksh.common.dto.DtoUtil;
import com.fosung.ksh.common.dto.handler.KshDTOCallbackHandler;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.meeting.constant.UserRight;
import com.fosung.ksh.meeting.entity.MeetingRoomPerson;
import com.fosung.ksh.meeting.entity.MeetingTemplateCycle;
import com.fosung.ksh.meeting.entity.MeetingTemplatePerson;
import com.fosung.ksh.meeting.service.MeetingTemplateCycleService;
import com.fosung.ksh.meeting.service.MeetingTemplatePersonService;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.dto.SysOrg;
import com.fosung.ksh.sys.dto.SysUser;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.dto.UtilDTO;
import com.fosung.framework.common.dto.support.DTOCallbackHandler;
import com.fosung.framework.common.util.UtilString;

@RestController
@RequestMapping(value=MeetingTemplatePersonController.BASE_PATH)
public class MeetingTemplatePersonController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/meeting-template-person" ;

    @Autowired
    private MeetingTemplatePersonService meetingTemplatePersonService ;
    @Autowired
    private MeetingTemplateCycleService meetingTemplateCycleService;
    @Autowired
    private SysOrgClient sysOrgClient;


    /**
     * 查询未授权的本地用户列表
     */
    @ApiOperation(value = "查询未授权的本地用户列表")
    @PostMapping("/right-list")
    public ResponseEntity<List<MeetingTemplatePerson>> queryNotRightLocalList(
            @ApiParam(value = "会议室ID", required = true) @RequestParam Long meetingTemplateId,
            @ApiParam(value = "当前角色管理的党组织Id", required = true) @RequestParam UserRight userRight) {

        Map<String, Object> param = Maps.newHashMap();
        param.put("meetingTemplateId", meetingTemplateId);
//        param.put("userRight", userRight);
        List<MeetingTemplatePerson> list = meetingTemplatePersonService.queryAll(param);
            DtoUtil.handler(list, getDtoCallbackHandler());
        return Result.success(list);
    }

    @PostMapping("/left-local-list")
/**
 * 查询未授权的本地用户列表
 */
    @ApiOperation(value = "查询未授权的本地用户列表")
    @RequestMapping(value = "/left-local-list", method = RequestMethod.POST)
    public ResponseEntity<List<SysUser>> queryNotRightLocalList(
            @ApiParam(value = "会议室ID", required = true) @RequestParam Long meetingTemplateId,
            @ApiParam(value = "当前角色管理的党组织Id", required = true) @RequestParam String orgId) {
        return Result.success(meetingTemplatePersonService.queryNotRightLocalList(meetingTemplateId, orgId));
    }

    /**
     * 查询未授权的灯塔用户列表
     */
    @ApiOperation(value = "查询未授权的灯塔用户列表")
    @RequestMapping(value = "/left-dt-list", method = RequestMethod.POST)
    public ResponseEntity<List<SysUser>> queryNotRightDTList(
            @ApiParam(value = "会议室ID", required = true) @RequestParam Long meetingTemplateId,
            @ApiParam(value = "当前角色管理的党组织Id", required = true) @RequestParam String orgId) {
        return Result.success(meetingTemplatePersonService.queryNotRightDTList(meetingTemplateId, orgId));
    }



    @ApiOperation(value = "用户信息授权")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(@RequestBody List<MeetingTemplatePerson> list) throws Exception {
        meetingTemplatePersonService.batchUpdate(list);
    }

    @PostMapping(value = "/query")
    public ResponseEntity<List<MeetingTemplatePerson>> query(
            @ApiParam(value = "当前角色管理的党组织Id", required = false)
    @RequestParam String orgId,@ApiParam(value = "会议室ID", required = true) @RequestParam Long meetingTemplateId){
        Map<String, Object> param = Maps.newHashMap();
        param.put("meetingTemplateId", meetingTemplateId);
        if(orgId!=null&&!orgId.equals("")) {
            param.put("orgIds", sysOrgClient.queryOrgAllChildId(orgId));
        }
        List<MeetingTemplatePerson> list = meetingTemplatePersonService.queryAll(param);
        DtoUtil.handler(list, getDtoCallbackHandler());
        return Result.success(list);
    }



    /**
     * 获取PO到DTO转换的接口。主要用于在前端展示数据之前首先将数据格式处理完成。
     *
     * @return
     */
    public KshDTOCallbackHandler getDtoCallbackHandler() {
        //创建转换接口类
        KshDTOCallbackHandler<MeetingTemplatePerson> dtoCallbackHandler = new KshDTOCallbackHandler<MeetingTemplatePerson>() {
            @Override
            public void doHandler(MeetingTemplatePerson dto) {

                SysOrg sysOrg = sysOrgClient.getOrgInfo(dto.getOrgId());
                if (sysOrg != null) {
                    dto.setOrgCode(sysOrg.getOrgCode());
                    dto.setOrgName(sysOrg.getOrgName());
                }
//                if (dto.getSignInType() != null) {null
//                    dto.setSignInTypeDict(dto.getSignInType().getRemark());
//                }
//                dto.setUserTypeDict(dto.getUserType() != null ? dto.getUserType().getRemark() : null);
//                dto.setUserRightDict(dto.getUserRight().getRemark());

            }
        };
        return dtoCallbackHandler;
    }

}
