package com.fosung.ksh.meeting.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.meeting.control.dto.user.UserinfoRequestDTO;
import com.fosung.ksh.meeting.entity.dto.MeetingUserSyncDTO;
import com.fosung.ksh.meeting.service.MeetingUserSyncService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author wangyh
 */
@Api(description = "视频会议会议室接口", tags = {"3、用户同步接口"})
@RestController
@RequestMapping(value = UserSyncController.BASE_PATH)
public class UserSyncController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/user-sync";


    @Autowired
    private MeetingUserSyncService userSyncService;


    /**
     * 获取视频会议签到人数
     */
    @ApiOperation(value = "查询当前党组织数据同步结果")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseEntity<List<MeetingUserSyncDTO>> query(
            @ApiParam(value = "当前角色管理的党组织Id", required = true) @RequestParam String orgId,
            @ApiParam(value = "当前角色管理的党组织Id", required = false) String realName) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return Result.success(userSyncService.query(orgId, realName));
    }


    /**
     * 获取视频会议签到人数
     */
    @ApiOperation(value = "同步用户数据到会控系统")
    @RequestMapping(value = "/sync", method = RequestMethod.POST)
    public void sync(@RequestBody UserinfoRequestDTO userinfoRequestDTO) throws Exception {
        userSyncService.sync(userinfoRequestDTO);
    }

    /**
     * 获取视频会议签到人数
     */
    @ApiOperation(value = "批量同步用户数据到会控系统")
    @RequestMapping(value = "/batch")
    public void batchSync(@ApiParam(value = "需要批量同步的组织ID", required = true)
                          @RequestParam String orgId) {
        userSyncService.batchSync(orgId);
    }
}
