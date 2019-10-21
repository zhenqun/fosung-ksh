package com.fosung.ksh.meeting.controller;

import com.fosung.framework.dao.config.mybatis.page.MybatisPage;
import com.fosung.framework.dao.config.mybatis.page.MybatisPageImpl;
import com.fosung.framework.dao.config.mybatis.page.support.PageHelper;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.ksh.common.dto.DtoUtil;
import com.fosung.ksh.common.dto.handler.KshDTOCallbackHandler;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.common.util.UtilRequest;
import com.fosung.ksh.meeting.constant.MeetingStatus;
import com.fosung.ksh.meeting.constant.MeetingType;
import com.fosung.ksh.meeting.constant.UserRight;
import com.fosung.ksh.meeting.control.client.RoomClient;
import com.fosung.ksh.meeting.entity.MeetingRoom;
import com.fosung.ksh.meeting.entity.MeetingRoomPerson;
import com.fosung.ksh.meeting.service.MeetingRoomPersonService;
import com.fosung.ksh.meeting.service.MeetingRoomService;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.fosung.ksh.sys.client.SysUserClient;
import com.fosung.ksh.sys.dto.SysOrg;
import com.google.common.collect.Maps;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.record.formula.functions.T;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author wangyh
 */
@Slf4j
@Api(description = "视频会议会议室接口", tags = {"1、会议室接口"})
@RestController
@RequestMapping(value = MeetingRoomController.BASE_PATH)
public class MeetingRoomController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/meeting-room";

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private SysUserClient sysUserClient;

    @Autowired
    private SysOrgClient sysOrgClient;

    @Autowired
    private RoomClient roomClient;

    @Autowired
    private MeetingRoomPersonService meetingRoomPersonService;
    /**
     * 记录分页查询
     *
     * @param pageNum  分页号，由0开始
     * @param pageSize 每页记录数，默认为10
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询会议室列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search_orgId", value = "当前用户管理的党组织", dataTypeClass = String.class, required = true, paramType = "query"),
            @ApiImplicitParam(name = "search_meetingName", value = "会议室名称，支持模糊查询", dataTypeClass = String.class, paramType = "query")
    })
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public ResponseEntity<Page<MeetingRoom>> query(
            @ApiParam(value = "页码", defaultValue = "0")
            @RequestParam(required = false, value = "pagenum",
                    defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @ApiParam(value = "每页显示条数", defaultValue = "0")
            @RequestParam(required = false, value = "pagesize",
                    defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize
    ) {

        //获取查询参数
        Map<String, Object> searchParam = getParametersStartingWith(getHttpServletRequest(), DEFAULT_SEARCH_PREFIX);

        Object orgid=searchParam.get("orgId");
        if(orgid!=null){
          List<String> orgids=  sysOrgClient.queryOrgAllChildId(orgid.toString());
          searchParam.put("orgIds",orgids);
        }
        searchParam.put("haveMeetingStatus", MeetingStatus.GOING);
        //执行分页查询
        Page<MeetingRoom> meetingRoomPage = meetingRoomService.queryByPage(searchParam, pageNum, pageSize, new String[]{"hopeStartTime_desc", "createDatetime_desc"});
        DtoUtil.handler(meetingRoomPage.getContent(), getDtoCallbackHandler());

        return Result.success(meetingRoomPage);
    }


    /**
     * 记录分页查询
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询我的会议室列表")
    @RequestMapping(value = "my", method = RequestMethod.POST)
    public ResponseEntity<Page<Map<String, Object>>> queryMyMeetingList(
            @ApiParam(value = "党组织ID")
            @RequestParam(required = false) String orgId,
            @ApiParam(value = "用户唯一标志")
            @RequestParam(required = true) String userHash,
            @ApiParam(value = "会议室名称")
            @RequestParam(required = false) String roomName,
            @ApiParam(value = "会议室是否召开")
            @RequestParam(required = false) String meetingStatus,
            @ApiParam(value = "页码", defaultValue = "0")
            @RequestParam(required = false, value = "pagenum",
                    defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @ApiParam(value = "每页显示条数", defaultValue = "0")
            @RequestParam(required = false, value = "pagesize",
                    defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) {

        //获取查询参数
        Page<Map<String, Object>> page = meetingRoomService.queryMyMeetingList(orgId, userHash, roomName,meetingStatus,  pageNum,  pageSize);


        return Result.success(page);
    }


    /**
     * 查询可巡查的视频会议列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "分页查询可巡查的会议室",
            response = MeetingRoom.class)
    @PostMapping("patrol-meeting-list")
    public ResponseEntity<Page<MeetingRoom>> queryPatrolMeeting(
            @ApiParam(value = "用户管理的党组织ID", required = true)
            @RequestParam String orgId,
            @ApiParam(value = "会议室名称")
            @RequestParam(required = false) String roomName,
            @ApiParam(value = "会议室类型")
            @RequestParam(required = false) String meetingType,
            @RequestParam(required = false, value = "pagenum",
                    defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @ApiParam(value = "每页显示条数", defaultValue = "0")
            @RequestParam(required = false, value = "pagesize",
                    defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) {


        //获取查询参数

        // todo 此处查询较慢，现在系统框架中in查询超级慢，待优化
        Page<MeetingRoom> meetingRoomPage = meetingRoomService.queryGoingMeetingList(orgId,roomName, meetingType, pageNum, pageSize);


        DtoUtil.handler(meetingRoomPage.getContent(), getDtoCallbackHandler());


        return Result.success(meetingRoomPage);
    }


    /**
     * 获取会议室详情
     *
     * @return
     */
    @ApiOperation(value = "获取会议室信息详情")
    @RequestMapping(value = "get", method = RequestMethod.POST)
    public ResponseEntity<MeetingRoom> get(@RequestParam Long id) {
        MeetingRoom meetingRoom = meetingRoomService.get(id);
        if (meetingRoom != null) {
            getDtoCallbackHandler().doHandler(meetingRoom);
            //查看应参加人数
            Map<String, Object> searchParam = new HashMap<String, Object>();
            searchParam.put("meetingRoomId",meetingRoom.getId());
            searchParam.put("userRights","'"+UserRight.ATTENDEE+"',"+"'"+UserRight.CHAIRMAN+"',"+"'"+UserRight.HEARER+"'");
            Integer personNum= meetingRoomPersonService.meeetingRoomPersonNum(searchParam);
            meetingRoom.setPersonNum(personNum);
            //查看应参加党组织数
            List<Map<String,Object>> orgNum= meetingRoomPersonService.meeetingRoomOrgNum(searchParam);
            meetingRoom.setOrgNum(orgNum.size());
        }
        return Result.success(meetingRoom);
    }

    /**
     * 获取会议室详情
     *
     * @return
     */
    @ApiOperation(value = "获取会议室信息详情")
    @RequestMapping(value = "get-hst-id", method = RequestMethod.POST)
    public ResponseEntity<MeetingRoom> getByHstRoomId(@RequestParam Integer hstRoomId) {
        MeetingRoom meetingRoom = meetingRoomService.getByHstRoomId(hstRoomId);
        if (meetingRoom != null) {
            getDtoCallbackHandler().doHandler(meetingRoom);
        }
        return Result.success(meetingRoom);
    }

    /**
     * 分类获取正在召开的会议室数量
     *
     * @return
     */
    @ApiOperation(value = "分类获取正在召开的会议室数量")
    @RequestMapping(value = "count", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Integer>> get(@RequestParam String orgId,@RequestParam String meetingType) {
        return Result.success(meetingRoomService.countMeetingType(orgId,meetingType));
    }


    /**
     * 获取会议室详情
     *
     * @return
     */
    @ApiOperation(value = "获取PC端启动地址")
    @RequestMapping(value = "join-meeting-url", method = RequestMethod.POST)
    public ResponseEntity<String> joinMeetingUrl(
            @ApiParam("会议室ID") @RequestParam(required = false) Long meetingRoomId,
            @ApiParam("好视通会议室ID") @RequestParam(required = false) Integer hstRoomId,
            @ApiParam("用户唯一标志") @RequestParam String userHash,
            @ApiParam("用户权限") @RequestParam UserRight userRight
    ) {
        Integer autoCheck=1;
        if (meetingRoomId != null) {
            MeetingRoom meetingRoom = meetingRoomService.get(meetingRoomId);
            Date time=meetingRoom.getHopeStartTime();
            Date newDate=new Date();
            //获取毫秒数
            Long startLong = time.getTime();
            Long endLong = newDate.getTime();
             //计算时间差,单位毫秒
            Long ms = endLong-startLong;
            if(ms>0){
                autoCheck=1;
            }else{
                autoCheck=0;
            }
            hstRoomId = meetingRoom.getHstRoomId();
        }


        return Result.success(meetingRoomService.joinMeetingUrl(hstRoomId, userHash, userRight,autoCheck));
    }

    /**
     * 获取会议室详情
     *
     * @return
     */
    @ApiOperation(value = "获取PC端视频会议巡查")
    @RequestMapping(value = "patrol-meeting-url", method = RequestMethod.POST)
    public ResponseEntity<String> patrolMeetingUrl(
            @ApiParam("好视通会议室ID") @RequestParam Integer hstRoomId,
            @ApiParam("用户唯一标志") @RequestParam String userHash
    ) {
        return Result.success(meetingRoomService.patrolMeetingUrl(hstRoomId, userHash));
    }


    /**
     * 保存实体对象 。在保存之前进行后端实体属性的验证，保证添加的数据符合业务要求。<br>
     * 如果实体id不为空，进行更新操作，否则进行添加。
     *
     * @param meetingRoom
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "保存会议室信息",
            notes = "只在数据库保存会议室信息，并不进行召开会议室",
            response = MeetingRoom.class)
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseEntity<MeetingRoom> save(@Valid @RequestBody MeetingRoom meetingRoom) throws Exception {
        Set<String> updateFields = Sets.newLinkedHashSet("roomName", "maxUserCount", "intervalTime","sign","signDuration","repairSign","repairDuration");
        meetingRoomService.save(meetingRoom, updateFields);
        return Result.success(meetingRoom);
    }

    /**
     * 召开会议室,会同步在会控接口中创建会议室
     *
     * @param meetingRoom
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @ApiOperation(value = "召开会议",
            response = MeetingRoom.class)
    @RequestMapping(value = "convene", method = RequestMethod.POST)
    public ResponseEntity<MeetingRoom> convene(@RequestBody MeetingRoom meetingRoom) throws InvocationTargetException, IllegalAccessException {
        meetingRoomService.convene(meetingRoom);
        return Result.success(meetingRoomService.get(meetingRoom.getId()));
    }


    /**
     * 保存会议室基本信息，并召开会议室
     *
     * @param meetingRoom
     * @param updateFields
     * @return
     */
    @ApiOperation(value = "保存会议室基本信息，并召开会议室",
            response = MeetingRoom.class)
    @RequestMapping(value = "save-and-convene", method = RequestMethod.POST)
    public ResponseEntity saveAndConvene(@RequestBody MeetingRoom meetingRoom) throws InvocationTargetException, IllegalAccessException {
        meetingRoomService.saveAndConvene(meetingRoom);
        return Result.success(meetingRoom);
    }

    /**
     * 用户授权到好视通
     *
     * @param meetingRoom
     * @param updateFields
     * @return
     */
    @ApiOperation(value = "用户授权到好视通")
    @RequestMapping(value = "user-right", method = RequestMethod.POST)
    public void userRight(Long id) {
        meetingRoomService.userRight(id);
    }


    /**
     * 关闭会议室
     *
     * @param meetingRoom
     * @param updateFields
     * @return
     */
    @ApiOperation(value = "关闭会议室")
    @RequestMapping(value = "close", method = RequestMethod.POST)
    public void close(Long id) throws InvocationTargetException, IllegalAccessException {
        meetingRoomService.close(id);
    }


    /**
     * 获取PO到DTO转换的接口。主要用于在前端展示数据之前首先将数据格式处理完成。
     *
     * @return
     */
    public KshDTOCallbackHandler getDtoCallbackHandler() {
        //创建转换接口类
        KshDTOCallbackHandler<MeetingRoom> dtoCallbackHandler = new KshDTOCallbackHandler<MeetingRoom>() {
            @Override
            public void doHandler(MeetingRoom dto) {
                SysOrg sysOrg = sysOrgClient.getOrgInfo(dto.getOrgId());
                if (sysOrg != null) {
                    dto.setOrgCode(sysOrg.getOrgCode());
                    dto.setOrgName(sysOrg.getOrgName());
                }
                if (dto.getMeetingStatus() != null) {
                    dto.setMeetingStatusDict(dto.getMeetingStatus().getRemark());
                }

                dto.setMeetingTypeDict(dto.getMeetingType().getRemark());

            }
        };
        return dtoCallbackHandler;
    }



}
