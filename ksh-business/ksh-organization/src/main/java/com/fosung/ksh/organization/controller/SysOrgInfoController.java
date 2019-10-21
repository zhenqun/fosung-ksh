package com.fosung.ksh.organization.controller;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilBean;
import com.fosung.ksh.common.dto.DtoUtil;
import com.fosung.ksh.common.dto.handler.KshDTOCallbackHandler;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.meeting.client.MeetingRoomClient;
import com.fosung.ksh.organization.entity.*;
import com.fosung.ksh.organization.service.OrgLifePeopleService;
import com.fosung.ksh.organization.service.OrgLifeService;
import com.fosung.ksh.organization.service.SysOrgInfoService;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.dto.SysOrg;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.types.resources.comparators.Date;
import org.springframework.beans.BeanUtils;
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


@Slf4j
@RestController
@RequestMapping(value=SysOrgInfoController.BASE_PATH)
public class SysOrgInfoController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/sysorginfo" ;

    @Autowired
    private SysOrgInfoService sysOrgInfoService ;

    @Autowired
    private SysOrgClient sysOrgClient;
    @Autowired
    private MeetingRoomClient meetingRoomClient;

    @Autowired
    private OrgLifeService orgLifeService;

    @Autowired
    private OrgLifePeopleService orgLifePeopleService;
    /**
     * 记录分页查询
     * @param pageNum 分页号，由0开始
     * @param pageSize 每页记录数，默认为10
     * @return
     * @throws Exception
     */
    @PostMapping("query")
    public  ResponseEntity<SysOrgInfo> query(
            @RequestParam(required=false,value = "classificationId") String classificationId,
            @RequestParam(required =false,value = "startDate") String startDate,
            @RequestParam(required =false,value = "endDate") String endDate,
            @RequestParam(required=false,value="branchId") String branchId

            ){
        List<SysOrgInfo> orgInfos=new ArrayList<SysOrgInfo>();
        if(branchId.equals("bf1a8f4e-57f3-11e7-87fa-0050569a68e4")){
            orgInfos =  sysOrgInfoService.queryAll(null);
        }else{
            List<SysOrg> sysOrgs=   sysOrgClient.queryOrgInfo(branchId);
            orgInfos=  sysOrgs.stream().map(sysOrg -> {
                SysOrgInfo sysOrgInfo=new SysOrgInfo();
                UtilBean.copyProperties(sysOrg,sysOrgInfo);
                return sysOrgInfo;
            }).collect(Collectors.toList());
        }
        for (int i=0;i<orgInfos.size();i++){
            SysOrgInfo org=orgInfos.get(i);
            String org_id=org.getOrgId();
            List<String> orgs=sysOrgClient.queryOrgAllChildId(org_id);
            Map<String,Object> paramSearch =new HashMap<String, Object>();
            List<String> lsth =new ArrayList<String>();
            paramSearch.put("orgIds" ,orgs);
            paramSearch.put("startDate",startDate);
            paramSearch.put("endDate",endDate);
            paramSearch.put("classificationId",classificationId);
            lsth=orgLifeService.getOrgListIds(paramSearch);
            org.setAllNum(lsth.size()+"");
            if(lsth.size()>0) {
                Set<String> orgLifeIds=lsth.stream().collect(Collectors.toSet());
                paramSearch.put("orgLifeIdIN", orgLifeIds);
                List<OrgLifePeople> orgPeoples = orgLifePeopleService.queryAll(paramSearch);
                org.setYcjNum(orgPeoples.size()+"");
                paramSearch.put("personnelType","JOIN");
                List<OrgLifePeople> orgPeoples2 = orgLifePeopleService.queryAll(paramSearch);
                org.setScjNum(orgPeoples2.size()+"");
            }else{
                org.setYcjNum("0");
                org.setScjNum("0");
            }
        }
         return Result.success(orgInfos);
    }

    //获取详情
    @PostMapping("get")
    @ApiOperation(value = "获取详情数据")
    public ResponseEntity detail(@ApiParam("主键ID") @RequestParam String orgId) {
        //查询组织生活报备记录
        SysOrg sysOrg = sysOrgClient.getOrgInfo(orgId);

        return Result.success(sysOrg);
    }
    /**
     * 获取PO到DTO转换的接口。主要用于在前端展示数据之前首先将数据格式处理完成。
     * @return
     */
    public KshDTOCallbackHandler getDtoCallbackHandler() {

        return (KshDTOCallbackHandler<SysOrgInfo>) dto -> {
            SysOrg sysOrg = sysOrgClient.getOrgInfo(dto.getOrgId());
            if (sysOrg != null) {
                dto.setOrgName(sysOrg.getOrgName());
            }
        };
    }


}