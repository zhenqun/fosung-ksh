package com.fosung.ksh.monitor.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.monitor.dto.PersonRecordInfo;
import com.fosung.ksh.monitor.service.SignRecordService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping(value = SignRecordController.BASE_PATH)
public class SignRecordController extends AppIBaseController {

    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/sign-record";

    @Resource
    private SignRecordService signRecordService;

    @ApiOperation("人员打卡记录")
    @RequestMapping(value = "list",method = RequestMethod.POST)
    public ResponseEntity<List<PersonRecordInfo>> creategetBlackList(
            @ApiParam("人脸库ID") @RequestParam String libIds,
            @ApiParam("开始时间") @RequestParam String startAlarmTime,
            @ApiParam("结束时间") @RequestParam String endAlarmTime,
            @ApiParam("设备编号") @RequestParam(required = false) String indexCode) {
        return Result.success(signRecordService.getBlackList(libIds, startAlarmTime, endAlarmTime, indexCode));
    }
}
