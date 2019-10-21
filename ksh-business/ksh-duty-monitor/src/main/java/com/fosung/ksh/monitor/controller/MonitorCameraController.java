package com.fosung.ksh.monitor.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.monitor.client.CameraVideoClient;
import com.fosung.ksh.monitor.entity.MonitorCamera;
import com.fosung.ksh.monitor.service.MonitorCameraService;
import com.fosung.ksh.sys.client.SysAreaClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wangyh
 */
@Api(description = "设备相关接口", tags = {"3、设备状态接口"})
@RestController
@RequestMapping(value = MonitorCameraController.BASE_PATH)
public class MonitorCameraController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/monitor-camera";


    @Autowired
    private MonitorCameraService cameraService;

    @Autowired
    private CameraVideoClient cameraVideoClient;


    /**
     * 记录分页查询
     *
     * @param pageNum  分页号，由0开始
     * @param pageSize 每页记录数，默认为10
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询设备在线详情")
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public ResponseEntity<Page<MonitorCamera>> query(
            @ApiParam("行政区域编码") @RequestParam Long areaId,
            @ApiParam("是否在线，0：离线 1：在线") @RequestParam(required = false) String monitorStatus,
            @ApiParam(value = "页码", defaultValue = "0")
            @RequestParam(required = false, value = "pagenum",
                    defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @ApiParam(value = "每页显示条数", defaultValue = "0")
            @RequestParam(required = false, value = "pagesize",
                    defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize
    ) {

        Page<MonitorCamera> page = cameraService.queryPageList(areaId, monitorStatus, pageNum, pageSize);
        return Result.success(page);
    }

    @RequestMapping(value = "/queryByVillageId")
    public ResponseEntity<List<MonitorCamera>> queryByVillageId( @RequestParam Long villageId ){
        return Result.success(cameraService.queryByVillageId(villageId));
    }

    @ApiOperation(value = "获取设备在线率离线率")
    @RequestMapping(value = "/rate", method = RequestMethod.POST)
    public ResponseEntity<MonitorCamera> rate(
            @ApiParam("行政区域代码") @RequestParam Long areaId) {
        return Result.success(cameraService.countSelfRate(areaId));
    }

    @ApiOperation(value = "获取直播视频流地址")
    @RequestMapping(value = "/get-city-code", method = RequestMethod.POST)
    public ResponseEntity<String> getCameraVideoRealTime(@RequestParam Long areaId) {
        MonitorCamera monitorCamera = cameraService.getByVillageId(areaId);
        return Result.success(monitorCamera);
    }


    @ApiOperation(value = "获取直播视频流地址")
    @RequestMapping(value = "/realtime", method = RequestMethod.POST)
    public ResponseEntity<String> getCameraVideoRealTime(@RequestParam Long areaId,
                                                         @RequestParam(name = "videoType", defaultValue = "HLS") String videoType) {
        MonitorCamera monitorCamera = cameraService.getByVillageId(areaId);
        return Result.success(cameraVideoClient.getCameraVideoRealTime(monitorCamera.getIndexCode(), videoType,"GET"));
    }


    /**
     * 根据监控点编号和时间获取视频回放url和视频回放时间片段
     */
    @ApiOperation(value = "根据监控点编号和时间获取视频回放url和视频回放时间片段")
    @RequestMapping(value = "/history", method = RequestMethod.POST)
    public ResponseEntity<String> getCameraVideoHistory(@RequestParam Long areaId,
                                                        @ApiParam("开始时间，格式：当前格林尼治时间/1000") @RequestParam("beginTime") String beginTime,
                                                        @ApiParam("结束时间，格式：当前格林尼治时间/1000") @RequestParam("endTime") String endTime) {
        MonitorCamera monitorCamera = cameraService.getByVillageId(areaId);
        return Result.success(cameraVideoClient.getCameraVideoHistory(monitorCamera.getIndexCode(), beginTime, endTime));
    }


}
