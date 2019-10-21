package com.fosung.ksh.meeting.controller;

import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.ksh.common.dto.DtoUtil;
import com.fosung.ksh.common.dto.handler.KshDTOCallbackHandler;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.meeting.constant.MeetingType;
import com.fosung.ksh.meeting.constant.Sign;
import com.fosung.ksh.meeting.constant.UserRight;
import com.fosung.ksh.meeting.constant.UserType;
import com.fosung.ksh.meeting.entity.MeetingJoinRecord;
import com.fosung.ksh.meeting.entity.MeetingRoom;
import com.fosung.ksh.meeting.entity.MeetingRoomPerson;
import com.fosung.ksh.meeting.service.MeetingJoinRecordService;
import com.fosung.ksh.meeting.service.MeetingRoomPersonService;
import com.fosung.ksh.meeting.service.MeetingRoomService;
import com.fosung.ksh.meeting.util.ExcelUtil;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.dto.SysOrg;
import com.fosung.ksh.sys.dto.SysUser;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyh
 */
@Slf4j
@Api(description = "视频会议会议室接口", tags = {"2、参会人员接口"})
@RestController
@RequestMapping(value = MeetingRoomPersonController.BASE_PATH)
public class MeetingRoomPersonController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/meeting-room-person";

    @Autowired
    private MeetingRoomPersonService meetingRoomPersonService;


    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private SysOrgClient sysOrgClient;


    @Autowired
    private MeetingJoinRecordService meetingJoinRecordService;

    /**
     * 查询未授权的灯塔用户列表
     */
    @ApiOperation(value = "查看当前会议室所有授权的用户")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseEntity<List<MeetingRoomPerson>> query(
            @ApiParam(value = "会议室ID", required = true) @RequestParam Long meetingRoomId,
            @ApiParam(value = "当前角色管理的党组织Id", required = false) @RequestParam String orgId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("meetingRoomId", meetingRoomId);
        if(orgId!=null&&!orgId.equals("")) {
            param.put("orgIds", sysOrgClient.queryOrgAllChildId(orgId));
        }
        param.put("notUserRight", UserRight.NOAUTH);
        List<MeetingRoomPerson> list = meetingRoomPersonService.queryAll(param);
        DtoUtil.handler(list, getDtoCallbackHandler());
        return Result.success(list);
    }


    /**
     * 查询未授权的灯塔用户列表
     */
    @ApiOperation(value = "查看当前会议室所有授权的用户")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity<MeetingRoomPerson> get(
            @ApiParam(value = "会议室ID", required = true) @RequestParam Long meetingRoomId,
            @ApiParam(value = "当前用户ID", required = true) @RequestParam String userHash) {
        ;
        return Result.success(meetingRoomPersonService.get(meetingRoomId, userHash));
    }


    /**
     * 获取视频会议签到人数
     */
    @ApiOperation(value = "查询当前会议室签到结果")
    @RequestMapping(value = "/sign/num", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> signInNum(
            @ApiParam(value = "会议室ID", required = true) @RequestParam(required = true) Long meetingRoomId,
            @ApiParam(value = "当前角色管理的党组织Id", required = true) @RequestParam(required = true) String orgId) {
        MeetingRoom meetingRoom = meetingRoomService.get(meetingRoomId);
        Map<String, Integer> result = meetingRoomPersonService.signInNum(meetingRoomId, orgId,
                MeetingType.ORGANIZATION == meetingRoom.getMeetingType() ? UserType.DT : null);
        return Result.success(result);
    }

    /**
     * 获取视频会议签到人数
     */
    @ApiOperation(value = "查询当前会议室签到结果")
    @RequestMapping(value = "/sign/list", method = RequestMethod.POST)
    public ResponseEntity<List<MeetingRoomPerson>> signInList(
            @ApiParam(value = "会议室ID", required = true) @RequestParam(required = false) Long meetingRoomId,
            @ApiParam(value = "当前角色管理的党组织Id", required = true) @RequestParam(required = false) String orgId,
            @ApiParam(value = "是否签到", required = false) @RequestParam(required = false) Boolean sign
    ) {
        MeetingRoom meetingRoom = meetingRoomService.get(meetingRoomId);
        List<MeetingRoomPerson> list = meetingRoomPersonService.signInList(meetingRoomId, orgId,
                MeetingType.ORGANIZATION == meetingRoom.getMeetingType() ? UserType.DT : null);

        if(sign!=null){
            list = list.stream()
                    .filter(person ->sign ? person.getSignInType() != null : person.getSignInType() == null)
                    .collect(Collectors.toList());
        }

        DtoUtil.handler(list, getDtoCallbackHandler());
        return Result.success(list);
    }

    /**
     * 查询未授权的灯塔用户列表
     */
    @ApiOperation(value = "查询未授权的灯塔用户列表")
    @RequestMapping(value = "/left-dt-list", method = RequestMethod.POST)
    public ResponseEntity<List<SysUser>> queryNotRightDTList(
            @ApiParam(value = "会议室ID", required = true) @RequestParam Long meetingRoomId,
            @ApiParam(value = "当前角色管理的党组织Id", required = true) @RequestParam String orgId) {
        return Result.success(meetingRoomPersonService.queryNotRightDTList(meetingRoomId, orgId));
    }

    /**
     * 查询未授权的本地用户列表
     */
    @ApiOperation(value = "查询未授权的本地用户列表")
    @RequestMapping(value = "/left-local-list", method = RequestMethod.POST)
    public ResponseEntity<List<SysUser>> queryNotRightLocalList(
            @ApiParam(value = "会议室ID", required = true) @RequestParam Long meetingRoomId,
            @ApiParam(value = "当前角色管理的党组织Id", required = true) @RequestParam String orgId) {
        return Result.success(meetingRoomPersonService.queryNotRightLocalList(meetingRoomId, orgId));
    }


    /**
     * 查询未授权的本地用户列表
     */
    @ApiOperation(value = "查询未授权的本地用户列表")
    @RequestMapping(value = "/right-list", method = RequestMethod.POST)
    public ResponseEntity<List<MeetingRoomPerson>> queryNotRightLocalList(
            @ApiParam(value = "会议室ID", required = true) @RequestParam Long meetingRoomId,
            @ApiParam(value = "当前角色管理的党组织Id", required = true) @RequestParam UserRight userRight) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("meetingRoomId", meetingRoomId);
        List<MeetingRoomPerson> list = meetingRoomPersonService.queryAll(param);
        DtoUtil.handler(list, getDtoCallbackHandler());
        return Result.success(list);
    }

    /**
     * 查询未授权的本地用户列表
     */
    @ApiOperation(value = "签到接口")
    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    public ResponseParam sign(@RequestBody MeetingRoomPerson meetingRoomPerson) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, UnsupportedEncodingException {
        log.info("人员签到：\nperson={}", JsonMapper.toJSONString(meetingRoomPerson,true));

        Long meetingRoomId = meetingRoomPerson.getMeetingRoomId();
        if (meetingRoomId == null) {
            Integer hstRoomId = meetingRoomPerson.getHstRoomId();
            Assert.notNull(hstRoomId, "meetingRoomId和hstRoomId不能同时为空");

            MeetingRoom meetingRoom = meetingRoomService.getByHstRoomId(hstRoomId);
            Assert.notNull(meetingRoom, "未能找到" + hstRoomId + "对应的会议室");
            meetingRoomId = meetingRoom.getId();
            meetingRoomPerson.setMeetingRoomId(meetingRoomId);
        }

        meetingRoomPersonService.sign(meetingRoomId, meetingRoomPerson);
        return ResponseParam.success().message("签到成功");
    }

    /**
     * 取消签到
     */
    @ApiOperation(value = "签退")
    @RequestMapping(value = "/un-sign", method = RequestMethod.POST)
    public void unSing(@RequestParam Long id) {
        MeetingRoomPerson meetingRoomPerson = new MeetingRoomPerson();
        meetingRoomPerson.setSignInTime(null);
        meetingRoomPerson.setSignInType(null);
        meetingRoomPerson.setId(id);
        meetingRoomPersonService.update(meetingRoomPerson, Sets.newHashSet("signInTime", "signInType"));
    }


    /**
     * 保存实体对象 。在保存之前进行后端实体属性的验证，保证添加的数据符合业务要求。<br>
     * 如果实体id不为空，进行更新操作，否则进行添加。
     *
     */
    /**
     * 查询未授权的本地用户列表
     */
    @ApiOperation(value = "用户信息授权")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(@RequestBody List<MeetingRoomPerson> list) throws Exception {
        meetingRoomPersonService.batchUpdate(list);
    }


    /**
     * 获取PO到DTO转换的接口。主要用于在前端展示数据之前首先将数据格式处理完成。
     *
     * @return
     */
    public KshDTOCallbackHandler getDtoCallbackHandler() {
        //创建转换接口类
        KshDTOCallbackHandler<MeetingRoomPerson> dtoCallbackHandler = new KshDTOCallbackHandler<MeetingRoomPerson>() {
            @Override
            public void doHandler(MeetingRoomPerson dto) {

                SysOrg sysOrg = sysOrgClient.getOrgInfo(dto.getOrgId());
                if (sysOrg != null) {
                    dto.setOrgCode(sysOrg.getOrgCode());
                    dto.setOrgName(sysOrg.getOrgName());
                }
                if (dto.getSignInType() != null) {
                    dto.setSignInTypeDict(dto.getSignInType().getRemark());
                }
                dto.setUserTypeDict(dto.getUserType() != null ? dto.getUserType().getRemark() : null);
                dto.setUserRightDict(dto.getUserRight().getRemark());

            }
        };
        return dtoCallbackHandler;
    }

    @PostMapping("patrol-meeting-down")
    public ResponseEntity<List<MeetingRoomPerson>> queryDown(
            @ApiParam(value = "页码", defaultValue = "0")
            @RequestParam(required = false, value = "pagenum",
                    defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @ApiParam(value = "每页显示条数", defaultValue = "0")
            @RequestParam(required = false, value = "pagesize",
                    defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize,
            @RequestParam(value = "hstRoomId") String hstRoomId,
            @RequestParam(value = "meetingRoomId") String meetingRoomId
    ) {
        //获取查询参数
        Map<String, Object> searchParam = new HashMap<String, Object>();
        searchParam.put("meetingRoomId", meetingRoomId);
        searchParam.put("userRights", UserRight.ATTENDEE + "," + UserRight.CHAIRMAN + "," + UserRight.HEARER);
//        searchParam.put("hstRoomId",hstRoomId);
        //执行分页查询
        Page<MeetingRoomPerson> meetingRoomPersonPage = meetingRoomPersonService.queryByPage(searchParam, pageNum, pageSize);
        List<MeetingRoomPerson> roomPeoples = meetingRoomPersonPage.getContent();
        for (int i = 0; i < roomPeoples.size(); i++) {
            MeetingRoomPerson dto = roomPeoples.get(i);
            Long meetRoomId = dto.getMeetingRoomId();
            String userHash = dto.getUserHash();
            Integer joinRecords = meetingJoinRecordService.countMeetingRooms(meetRoomId, userHash);
            dto.setJoinRecordsNum(joinRecords);

            MeetingRoom meetingRoom = meetingRoomService.get(meetRoomId);
            Map<String, Integer> result = meetingRoomPersonService.signInNum(meetRoomId, dto.getOrgId(),
                    MeetingType.ORGANIZATION == meetingRoom.getMeetingType() ? UserType.DT : null);
            dto.setAttendanceNum(result.get("signNum"));
        }
        DtoUtil.handler(meetingRoomPersonPage.getContent(), getDtoCallbackHandler());
        return Result.success(meetingRoomPersonPage);
    }


    @ApiOperation(value = "查看当前会议室所有授权的用户")
    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public void excel(
            @ApiParam(value = "会议室ID", required = true) @RequestParam Long meetingRoomId,
            @ApiParam(value = "当前角色管理的党组织Id", required = false) @RequestParam String orgId,
            HttpServletResponse response) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("meetingRoomId", meetingRoomId);
        param.put("orgId", orgId);
        param.put("notUserRight", UserRight.NOAUTH);
        List<MeetingRoomPerson> list = meetingRoomPersonService.queryAll(param);
        MeetingRoom meetingRoom = meetingRoomService.get(meetingRoomId);
        DtoUtil.handler(list, getDtoCallbackHandler());

        String title[] = {"姓名", "所属党组织", "手机号", "角色", "签到方式", "签到时间"};
        //excel文件名
        String fileName = meetingRoom.getRoomName() + "签到会议列表.xls";
        //sheet名
        String sheetName = "签到列表";
        String content[][] = new String[list.size()][4];
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            MeetingRoomPerson obj = list.get(i);
            content[i][0] = obj.getPersonName();
            content[i][1] = obj.getOrgName();
            content[i][2] = obj.getTelephone();
            content[i][3] = obj.getUserRightDict();
            content[i][4] = obj.getSignInTypeDict();
            Date sign = obj.getSignInTime();
            if (sign != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(obj.getSignInTime());
                content[i][5] = dateString;
            } else {
                content[i][5] = "";
            }
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 实际参加党组织
  * 接口
     */
    @RequestMapping(value = "/shouldJoinOrg", method = RequestMethod.POST)
    public ResponseEntity<List<MeetingRoom>> shouldJoinOrg(
            @ApiParam(value = "会议室ID", required = true) @RequestParam(required =false) Long meetingRoomId,
            @ApiParam(value = "当前角色管理的党组织Id", required = true) @RequestParam(required =false)  String orgId
    ) {
        List<String> orgIds= sysOrgClient.queryOrgAllChildId(orgId);
        List<MeetingRoom> mRooms=new ArrayList<MeetingRoom>();
        MeetingRoom meetingRoom = meetingRoomService.get(meetingRoomId);
        Map<String, Object> param = Maps.newHashMap();
        param.put("meetingRoomId", meetingRoomId);
        param.put("signInType",true);
        param.put("orgIds",orgIds);
        List<Map<String, Object>> list = meetingRoomPersonService.listOrgId(param);
        for (int i=0;i<list.size();i++){
            MeetingRoom mr=new MeetingRoom();
            Map<String, Object> map=list.get(i);
            Object str=map.get("orgId");
            SysOrg org= sysOrgClient.getOrgInfo(str.toString());
            mr.setSign(Sign.ENABLE);
            mr.setHstRoomId(meetingRoom.getHstRoomId());
            mr.setRoomName(meetingRoom.getRoomName());
            mr.setOrgName(org.getOrgName());
            mRooms.add(mr);
        }
        return Result.success(mRooms);
    }

    /**
     * 应参加党组织
     * 接口
     */
    @RequestMapping(value = "/needJoinOrg", method = RequestMethod.POST)
    public ResponseEntity<List<MeetingRoom>> needJoinOrg(
            @ApiParam(value = "会议室ID", required = true) @RequestParam(required =false) Long meetingRoomId,
            @ApiParam(value = "当前角色管理的党组织Id", required = true) @RequestParam(required =false)  String orgId
    ) {
        List<String> orgIds= sysOrgClient.queryOrgAllChildId(orgId);
        List<MeetingRoom> mRooms=new ArrayList<MeetingRoom>();
        MeetingRoom meetingRoom = meetingRoomService.get(meetingRoomId);
        Map<String, Object> param = Maps.newHashMap();
        param.put("meetingRoomId", meetingRoomId);
        param.put("signInType",false);
        param.put("orgIds",orgIds);
        param.put("userRights","'"+UserRight.ATTENDEE+"',"+"'"+UserRight.CHAIRMAN+"',"+"'"+UserRight.HEARER+"'");
        List<Map<String, Object>> list = meetingRoomPersonService.listOrgId(param);
        for (int i=0;i<list.size();i++){
            MeetingRoom mr=new MeetingRoom();
            Map<String, Object> map=list.get(i);
            Object str=map.get("orgId");
            SysOrg org= sysOrgClient.getOrgInfo(str.toString());
            //查看orgId 下有沒有人参加
            Map<String, Object> search = Maps.newHashMap();
            search.put("meetingRoomId", meetingRoomId);
            search.put("signInType",true);
            List<String> orgs=new ArrayList<String>();
            orgs.add(str.toString());
            search.put("orgIds",orgs);
            List<Map<String, Object>> lSearch =  meetingRoomPersonService.listOrgId(search);
            if(lSearch.size()>0){
            mr.setSign(Sign.ENABLE);
            }else{
                mr.setSign(Sign.DISABLE);
            }
            mr.setHstRoomId(meetingRoom.getHstRoomId());
            mr.setRoomName(meetingRoom.getRoomName());
            mr.setOrgName(org.getOrgName());
            mRooms.add(mr);
        }
        return Result.success(mRooms);
    }
    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
