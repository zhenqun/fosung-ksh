package com.fosung.ksh.monitor.service.impl;

import com.fosung.framework.common.json.JsonMapper;
import com.fosung.ksh.monitor.dto.AlarmTopicInfo;
import com.fosung.ksh.monitor.service.AlarmTopicInfoService;
import com.fosung.ksh.monitor.util.HikVisionArtemisUtil;
import org.springframework.stereotype.Service;

@Service
public class AlarmTopicInfoServiceImpl implements AlarmTopicInfoService {

    @Override
    public AlarmTopicInfo getAlarmTopicInfo(String appkey) {
        String topicInfoStr = HikVisionArtemisUtil.callApiAlarmTopicInfo(appkey);
        AlarmTopicInfo alarmTopicInfo = JsonMapper.parseObject(topicInfoStr, AlarmTopicInfo.class);
        return alarmTopicInfo;
    }
}
