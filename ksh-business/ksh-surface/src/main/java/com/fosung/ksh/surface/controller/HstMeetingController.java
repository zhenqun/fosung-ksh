package com.fosung.ksh.surface.controller;

import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.dto.DTOCallbackHandlerDelegate;
import com.fosung.framework.common.dto.support.DTOCallbackHandler;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilDate;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.ksh.common.response.ResponseResult;
import com.fosung.ksh.meeting.client.MeetingJoinRecordClient;
import com.fosung.ksh.meeting.client.MeetingRoomClient;
import com.fosung.ksh.meeting.client.MeetingRoomPersonClient;
import com.fosung.ksh.meeting.constant.SigninType;
import com.fosung.ksh.meeting.dto.MeetingRoom;
import com.fosung.ksh.meeting.dto.MeetingRoomPerson;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihua
 * @date 2019-05-07 13:03
 */
@Slf4j
@Api(description = "福生可视化对外提供接口服务", tags = {"1、会议室接口"})
@RestController
public class HstMeetingController extends AppIBaseController {


    @Autowired
    private MeetingRoomClient meetingRoomClient;
    @Autowired
    private MeetingRoomPersonClient personClient;

    @Autowired
    private MeetingJoinRecordClient recordClient;

    /**
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "结束会议室")
    @RequestMapping(value = "/hst/meeting/finish", method = RequestMethod.POST)
    public ResponseParam login(
            @ApiParam(value = "好视通会议室ID")
            @RequestParam Integer hstRoomId) {
        MeetingRoom meetingRoom = meetingRoomClient.getByHstRoomId(hstRoomId);
        if (meetingRoom != null) {
            meetingRoomClient.close(meetingRoom.getId());
            return ResponseParam.success().data("会议室关闭成功");
        }
        return ResponseParam.fail().data("无效的会议室ID,会议室关闭失败");
    }


    /**
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "人员签到")
    @RequestMapping(value = "/hst/sign", method = RequestMethod.POST)
    public ResponseParam sign(
            @ApiParam(value = "好视通会议室ID")
            @RequestParam Integer hstRoomId,
            @ApiParam(value = "签到人员唯一标志")
            @RequestParam String userHash,
            @ApiParam(value = "签到方式")
            @RequestParam String signinType) {

        log.info("\n人员签到接口：\nhstRoomId={}\nuserHash={}\nsignType={}", hstRoomId, userHash, signinType);
        MeetingRoom meetingRoom = meetingRoomClient.getByHstRoomId(hstRoomId);
        if (meetingRoom != null) {
            MeetingRoomPerson meetingRoomPerson = new MeetingRoomPerson();
            meetingRoomPerson.setUserHash(userHash);
            meetingRoomPerson.setMeetingRoomId(meetingRoom.getId());
            meetingRoomPerson.setHstRoomId(hstRoomId);
            meetingRoomPerson.setSignInType(SigninType.valueOf(signinType));
            ResponseResult result = personClient.sign(meetingRoomPerson);
            if (result.ok()) {
                return ResponseParam.success().data("签到成功");
            } else {
                return ResponseParam.fail().data(result.getMessage());
            }

        }
        return ResponseParam.fail().data("当前会议室不存在，签到失败");


    }


    /**
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "人员退出登录")
    @RequestMapping(value = "/hst/meeting/join/record/leave", method = RequestMethod.POST)
    public ResponseParam leave(
            @ApiParam(value = "好视通会议室ID")
            @RequestParam Integer hstRoomId,
            @ApiParam(value = "签到人员唯一标志")
            @RequestParam String userHash) {
        recordClient.leave(hstRoomId, userHash);
        return ResponseParam.success();


    }

    /**
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "人员签到统计")
    @RequestMapping(value = "/hst/sign/num", method = RequestMethod.POST)
    public ResponseParam signNum(
            @ApiParam(value = "好视通会议室ID")
            @RequestParam Integer hstRoomId,
            @ApiParam(value = "组织ID")
            @RequestParam String orgId) {
        MeetingRoom meetingRoom = meetingRoomClient.getByHstRoomId(hstRoomId);
        if (meetingRoom != null) {
            Map<String, Object> result = personClient.signInNum(meetingRoom.getId(), orgId);
            result.put("singNum", result.get("signNum"));
            return ResponseParam.success().data(result);
        }
        return ResponseParam.fail().data("当前会议室不存在，获取数据失败");

    }

    /**
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "人员签到列表")
    @RequestMapping(value = "/hst/sign/list", method = RequestMethod.POST)
    public ResponseParam signList(
            @ApiParam(value = "好视通会议室ID")
            @RequestParam Integer hstRoomId,
            @ApiParam(value = "组织ID")
            @RequestParam String orgId,
            @ApiParam(value = "是否签到", required = false) @RequestParam(required = false) Boolean sign) {
        MeetingRoom meetingRoom = meetingRoomClient.getByHstRoomId(hstRoomId);
        if (meetingRoom != null) {
            List<MeetingRoomPerson> result = personClient.signInList(meetingRoom.getId(), orgId, sign);
            List<JSONObject> list = Lists.newArrayList();
            result.stream().forEach(person -> {


                JSONObject jsonObject = JsonMapper.parseObject(JsonMapper.toJSONString(person));
                jsonObject.put("signinType", person.getSignInType());
                jsonObject.put("signinType_dict", person.getSignInType() == null ? null : person.getSignInType().getRemark());
                if(person.getSignInTime()!=""){
                    jsonObject.put("signinTime", person.getSignInTime());
                }
                else{
                    jsonObject.put("signinTime", "");
                }
                list.add(jsonObject);
            });
            return ResponseParam.success().datalist(list);
        }
        return ResponseParam.fail().data("当前会议室不存在，获取数据失败");

    }

    @ApiOperation(value = "实际参加党组织列表")
    @RequestMapping(value = "/hst/shouldJoinOrg", method = RequestMethod.POST)
    public ResponseParam shouldJoinOrg(
            @ApiParam(value = "好视通会议室ID")
            @RequestParam Integer hstRoomId,
            @ApiParam(value = "组织ID")
            @RequestParam String orgId) {
        MeetingRoom meetingRoom = meetingRoomClient.getByHstRoomId(hstRoomId);
        if (meetingRoom != null) {
            List<MeetingRoomPerson> result = personClient.shouldJoinOrg(meetingRoom.getId(), orgId);
            List<JSONObject> list = Lists.newArrayList();
            result.stream().forEach(person -> {
                JSONObject jsonObject = JsonMapper.parseObject(JsonMapper.toJSONString(person));
                jsonObject.put("hstRoomId", hstRoomId);
                jsonObject.put("roomName", jsonObject.getString("roomName"));
                jsonObject.put("orgName", jsonObject.getString("orgName"));
                jsonObject.put("sign", jsonObject.getString("sign"));//DISABLE 未签到 ENABLE签到
                list.add(jsonObject);
            });
            return ResponseParam.success().datalist(list);
        }
        return ResponseParam.fail().data("当前会议室不存在，获取数据失败");

    }

    @ApiOperation(value = "实际参加党组织列表")
    @RequestMapping(value = "/hst/needJoinOrg", method = RequestMethod.POST)
    public ResponseParam needJoinOrg(
            @ApiParam(value = "好视通会议室ID")
            @RequestParam Integer hstRoomId,
            @ApiParam(value = "组织ID")
            @RequestParam String orgId) {
        MeetingRoom meetingRoom = meetingRoomClient.getByHstRoomId(hstRoomId);
        if (meetingRoom != null) {
            List<MeetingRoomPerson> result = personClient.needJoinOrg(meetingRoom.getId(), orgId);
            List<JSONObject> list = Lists.newArrayList();
            result.stream().forEach(person -> {
                JSONObject jsonObject = JsonMapper.parseObject(JsonMapper.toJSONString(person));
                jsonObject.put("hstRoomId", hstRoomId);
//                jsonObject.put("roomName",jsonObject.getString("roomName"));
                jsonObject.put("roomName", jsonObject.getString("roomName"));
                jsonObject.put("orgName", jsonObject.getString("orgName"));
                jsonObject.put("sign", jsonObject.getString("sign"));//DISABLE 未签到 ENABLE签到
                list.add(jsonObject);
            });
            return ResponseParam.success().datalist(list);
        }
        return ResponseParam.fail().data("当前会议室不存在，获取数据失败");

    }

}
