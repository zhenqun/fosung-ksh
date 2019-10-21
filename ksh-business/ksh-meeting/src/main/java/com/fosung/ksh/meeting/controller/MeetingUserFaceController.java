package com.fosung.ksh.meeting.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.common.util.UtilEasyPoi;
import com.fosung.ksh.meeting.entity.MeetingUserFace;
import com.fosung.ksh.meeting.service.MeetingUserFaceService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangyh
 */
@Api(description = "会议室接口", tags = {"3、人脸采集接口"})
@RestController
@RequestMapping(value = MeetingUserFaceController.BASE_PATH)
public class MeetingUserFaceController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/face";


    @Autowired
    private MeetingUserFaceService meetingUserFaceService;

    @ApiOperation(value = "人脸采集")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<MeetingUserFace> create(@RequestBody MeetingUserFace meetingUserFace) throws Exception {
        return Result.success(meetingUserFaceService.create(meetingUserFace));
    }


    @ApiOperation(value = "查询用户人脸采集列表")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseEntity<List<MeetingUserFace>> query(@ApiParam("支部ID") @RequestParam String orgId,
                                                       @ApiParam("姓名") @RequestParam(required = false, defaultValue = "") String realName) throws Exception {
        return Result.success(meetingUserFaceService.queryByOrgId(orgId, realName));
    }

    @ApiOperation(value = "获取用户采集信息")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity<MeetingUserFace> query(@ApiParam("用户hash") @RequestParam String hash) {
        return Result.success(meetingUserFaceService.get(hash));
    }


    @ApiOperation(value = "导出人脸采集结果")
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(@ApiParam("要导出的ID集合，通过','进行连接") @RequestParam String ids) throws Exception {
        String id[] = ids.split(",");

        List<String> idList = Lists.newArrayList();
        for (String s : id) {
            idList.add(s);
        }
        List<MeetingUserFace> meetingUserFaces = meetingUserFaceService.export(idList);
        UtilEasyPoi.exportExcel(meetingUserFaces, "人员导出结果", "人员导出结果", MeetingUserFace.class, "人员导出结果.xls", getHttpServletResponse());
    }

}
