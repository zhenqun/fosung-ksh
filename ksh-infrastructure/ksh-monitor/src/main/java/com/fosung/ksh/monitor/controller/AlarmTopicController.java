package com.fosung.ksh.monitor.controller;

import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.monitor.dto.AlarmTopicInfo;
import com.fosung.ksh.monitor.service.AlarmTopicInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取报警topic 信息
 */
@RestController
@RequestMapping(AlarmTopicController.BASE_URL)
public class AlarmTopicController {
    public  static  final String BASE_URL="alarmtopic";


    @Autowired
    AlarmTopicInfoService alarmTopicInfoService;
    @RequestMapping("getAlarmInfo")
    public ResponseEntity<AlarmTopicInfo> getAlarmTopicInfo(@Validated @RequestParam("appKey")String appKey){
        return Result.success(alarmTopicInfoService.getAlarmTopicInfo(appKey));
    }
}
