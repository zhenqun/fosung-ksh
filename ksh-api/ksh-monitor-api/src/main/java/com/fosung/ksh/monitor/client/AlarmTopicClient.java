package com.fosung.ksh.monitor.client;

import com.fosung.ksh.monitor.dto.AlarmTopicInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient( name = "ksh-monitor",path = "alarmtopic")
public interface AlarmTopicClient {
    @RequestMapping(path = "getAlarmInfo",method = RequestMethod.POST)
    AlarmTopicInfo getAlarmTopicInfo(@RequestParam("appKey") String appkey);
}
