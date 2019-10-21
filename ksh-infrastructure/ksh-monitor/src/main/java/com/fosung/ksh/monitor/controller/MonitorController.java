package com.fosung.ksh.monitor.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.monitor.dto.MonitorInfo;
import com.fosung.ksh.monitor.service.MonitorService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping(value = MonitorController.BASE_PATH)
public class MonitorController extends AppIBaseController {

    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/monitor";

    @Resource
    private MonitorService monitorService;

    @ApiOperation("人员打卡记录")
    @RequestMapping(value = "list",method = RequestMethod.POST)
    public ResponseEntity<List<MonitorInfo>> queryMonitorList() {
        return Result.success(monitorService.queryMonitorList());
    }
}
