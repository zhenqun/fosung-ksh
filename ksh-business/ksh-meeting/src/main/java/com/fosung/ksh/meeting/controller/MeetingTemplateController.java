package com.fosung.ksh.meeting.controller;

import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.dto.UtilDTO;
import com.fosung.framework.common.dto.support.DTOCallbackHandler;
import com.fosung.framework.task.AppTaskCluster;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.ksh.common.dto.DtoUtil;
import com.fosung.ksh.common.dto.handler.KshDTOCallbackHandler;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.common.util.UtilDateTime;
import com.fosung.ksh.meeting.constant.*;
import com.fosung.ksh.meeting.entity.MeetingRoom;
import com.fosung.ksh.meeting.entity.MeetingTemplate;
import com.fosung.ksh.meeting.entity.MeetingTemplateCycle;
import com.fosung.ksh.meeting.service.MeetingRoomService;
import com.fosung.ksh.meeting.service.MeetingTemplateCycleService;
import com.fosung.ksh.meeting.service.MeetingTemplatePersonService;
import com.fosung.ksh.meeting.service.MeetingTemplateService;
import com.fosung.ksh.meeting.service.impl.MeetingRoomServiceImpl;
import com.fosung.ksh.meeting.service.impl.MeetingTemplateServiceImpl;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.dto.SysOrg;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Api(description = "视频模板")
@RestController
@RequestMapping(value = com.fosung.ksh.meeting.controller.MeetingTemplateController.BASE_PATH)
public class MeetingTemplateController extends AppIBaseController{
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/meeting-template";
    @Autowired
    private SysOrgClient sysOrgClient;
    @Autowired
    private MeetingTemplateService meetingTemplateService;
    @Autowired
    private MeetingRoomService meetingRoomService;
    @Autowired
    private MeetingTemplateCycleService meetingTemplateCycleService ;
    @Autowired
    private MeetingTemplatePersonService meetingTemplatePersonService;

    @PostMapping("query")
    public ResponseEntity<Page<MeetingTemplate>> query(

            @RequestParam(required = false, value = "pagenum", defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @RequestParam(required = false, value = "pagesize", defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) {
        //获取查询参数
        Map<String, Object> searchParam = getParametersStartingWith(getHttpServletRequest(), DEFAULT_SEARCH_PREFIX);
        Object orgid=searchParam.get("orgId");
        if(orgid!=null){
            List<String> orgids=  sysOrgClient.queryOrgAllChildId(orgid.toString());
            searchParam.put("orgIds",orgids);
        }
        //执行分页查询
        Page<MeetingTemplate> meetingTemplatePage = meetingTemplateService.queryByPage(searchParam, pageNum, pageSize);
         DtoUtil.handler(meetingTemplatePage.getContent(), getDtoCallbackHandler());
        return Result.success(meetingTemplatePage);
    }

    @PostMapping("save")
    public ResponseEntity<MeetingTemplate> save(@Valid @RequestBody MeetingTemplate meetingTemplate) throws Exception {
        Set<String> updateFields = Sets.newLinkedHashSet("roomName", "maxUserCount", "intervalTime","templateType","sign","signDuration","repairSign","repairDuration");
        MeetingTemplate template=meetingTemplateService.saveTemplate(meetingTemplate,updateFields);
        return Result.success(template);
    }

    @PostMapping("delete")
    public ResponseEntity delete(@RequestParam(value = "id") Long id){
        meetingTemplateService.delete(id);
        return Result.success();
    }

    @PostMapping("dels")
    public ResponseEntity dels(@RequestParam(value = "ids") String ids){
        System.out.println(ids);
        String[] idAll=ids.split(",");
        for (int i=0;i<idAll.length;i++){
            long id=Long.parseLong(idAll[i]);
            System.out.println(id);
            meetingTemplateService.delete(id);
        }
        return Result.success();
    }

    @PostMapping("get")
    public  ResponseEntity<MeetingTemplate> get(@RequestParam(value = "id") Long id){
        MeetingTemplate meetingTemplate=meetingTemplateService.get(id);

        if (meetingTemplate != null) {
            getDtoCallbackHandler().doHandler(meetingTemplate);
            //查看应参加人数
            Map<String, Object> searchParam = new HashMap<String, Object>();
            searchParam.put("meetingTemplateId",meetingTemplate.getId());
            searchParam.put("userRights","'"+UserRight.ATTENDEE+"',"+"'"+UserRight.CHAIRMAN+"',"+"'"+UserRight.HEARER+"'");
            Integer personNum= meetingTemplatePersonService.meetingTemplatePersonNum(searchParam);
            meetingTemplate.setPersonNum(personNum);
            //查看应参加党组织数
            List<Map<String,Object>> orgNum= meetingTemplatePersonService.meetingTemplateOrgNum(searchParam);
            meetingTemplate.setOrgNum(orgNum.size());
        }
        MeetingTemplateCycle cycle=meetingTemplateCycleService.getByTemplateId(id);
        meetingTemplate.setCycle(cycle);
        return Result.success(meetingTemplate);
    }

    @ApiOperation(value = "召开会议",
            response = MeetingTemplate.class)
    @RequestMapping(value = "convene", method = RequestMethod.POST)
    public ResponseEntity<MeetingRoom> convene(@RequestBody MeetingTemplate meetingTemplate) throws InvocationTargetException, IllegalAccessException {
        MeetingRoom meetingRoom=meetingTemplateService.convene(meetingTemplate);
        return Result.success(meetingRoomService.get(meetingRoom.getId()));
    }
    /**
     * 获取PO到DTO转换的接口。主要用于在前端展示数据之前首先将数据格式处理完成。
     *
     * @return
     */
    /**
     * 获取PO到DTO转换的接口。主要用于在前端展示数据之前首先将数据格式处理完成。
     *
     * @return
     */
    public KshDTOCallbackHandler getDtoCallbackHandler() {
        //创建转换接口类
        KshDTOCallbackHandler<MeetingTemplate> dtoCallbackHandler = new KshDTOCallbackHandler<MeetingTemplate>() {
            @Override
            public void doHandler(MeetingTemplate dto) {
                SysOrg sysOrg = sysOrgClient.getOrgInfo(dto.getOrgId());
                if (sysOrg != null) {
                    dto.setOrgCode(sysOrg.getOrgCode());
                    dto.setOrgName(sysOrg.getOrgName());
                }

            }
        };
        return dtoCallbackHandler;
    }
}
