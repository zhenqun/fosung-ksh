package com.fosung.ksh.monitor.controller;

import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.monitor.dto.CameraHlsHistoryTotal;
import com.fosung.ksh.monitor.service.CameraVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: fosung-ksh
 * @description: 视频流
 * @author: LZ
 * @create: 2019-05-10 09:47
 **/
@Api(description = "获取海康视频流", tags = {"获取海康视频流"})
@Controller
@RequestMapping(CameraVideoController.BASE_URL)
public class CameraVideoController extends AppIBaseController {
    /**
     * 基础路径
     */
    public static final String BASE_URL ="/monitor/video";
    /**
     * 获取视频流服务
     */
    @Autowired
    private CameraVideoService cameraVideoService;

    /**
     * 实时视频 根据监控点编号获取HLS（RTSP）地址(向下兼容用)
     */

    @ApiOperation("实时视频流")
    @RequestMapping(value = "/realtime", method = RequestMethod.POST)
    public  ResponseEntity<String> getCameraVideoRealTime(@ApiParam("监控点编号") @RequestParam("cameraIndexCode") String cameraIndexCode,
                                                          @ApiParam("视频流格式（RTSP，HLS）") @RequestParam(name = "videoType",defaultValue ="HLS" )String videoType,
                                                          @ApiParam("视频流获取方式") @RequestParam(name = "=getType",defaultValue ="GET" )String getType){
        if (UtilString.equalsIgnoreCase("REFRESH",getType)){
            return Result.success(cameraVideoService.refreshCameraVideoRealTime(cameraIndexCode,videoType));
        }
        return Result.success(cameraVideoService.getCameraVideoRealTime(cameraIndexCode,videoType));
    }

    /**
     * 根据监控点编号和时间获取视频回放url和视频回放时间片段
     */
    @ApiOperation("历史视频流")
    @RequestMapping(value = "/history", method = RequestMethod.POST)
    public ResponseEntity<String> getCameraVideoHistory(@ApiParam("监控点编号") @RequestParam("cameraIndexCode") String cameraIndexCode,
                                                        @ApiParam("开始时间") @RequestParam("beginTime") String beginTime,
                                                        @ApiParam("结束时间") @RequestParam("endTime") String endTime){
        CameraHlsHistoryTotal cameraVideoHistory = cameraVideoService.getCameraVideoHistory(cameraIndexCode, beginTime, endTime);
        Assert.notNull(cameraVideoHistory,"获取视频流失败");
        return Result.success(cameraVideoHistory.getTotalTimeUrl());
    }

}
