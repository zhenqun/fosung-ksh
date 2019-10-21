package com.fosung.ksh.duty.client;

import com.alibaba.fastjson.JSON;
import com.fosung.ksh.duty.config.HikProperties;
import com.fosung.ksh.monitor.client.AlarmTopicClient;
import com.fosung.ksh.monitor.dto.AlarmTopicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
     @Autowired
     HikProperties hikProperties;
     @Autowired
     AlarmTopicClient alarmTopicClient;
     @Autowired
     HkiMQClient hkiMQClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AlarmTopicInfo alarmTopicInfo = alarmTopicClient.getAlarmTopicInfo(hikProperties.getAppKey());
        System.out.println(JSON.toJSON(alarmTopicInfo));
        hkiMQClient.setUserName(alarmTopicInfo.getTopic().get("userName") + "");
        hkiMQClient.setPassWord(alarmTopicInfo.getTopic().get("password") + "");
        hkiMQClient.start();

    }
}
