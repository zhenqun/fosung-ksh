package com.fosung.ksh.monitor.client;

import com.fosung.ksh.common.response.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: fosung-ksh
 * @description: 海康视频流Client
 * @author: LZ
 * @create: 2019-05-10 11:24
 **/
@FeignClient(name = "ksh-monitor", path = "/monitor/video")
public interface CameraVideoClient {
    /**
     * 实时视频 根据监控点编号获取HLS（RTSP）地址(向下兼容用)
     */

    @RequestMapping(value = "/realtime", method = RequestMethod.POST)
    String getCameraVideoRealTime(@RequestParam("cameraIndexCode") String cameraIndexCode,
                                  @RequestParam(name = "videoType", defaultValue = "HLS") String videoType,
                                  @RequestParam(name = "getType", defaultValue = "GET") String getType);

   /**
     * 根据监控点编号和时间获取视频回放url和视频回放时间片段
     */
    @RequestMapping(value = "/history", method = RequestMethod.POST)
    String getCameraVideoHistory(@RequestParam("cameraIndexCode") String cameraIndexCode,
                                 @RequestParam("beginTime") String beginTime,
                                 @RequestParam("endTime") String endTime);

}
