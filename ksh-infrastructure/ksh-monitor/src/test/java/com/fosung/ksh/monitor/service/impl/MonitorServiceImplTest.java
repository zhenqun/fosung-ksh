package com.fosung.ksh.monitor.service.impl;

import com.fosung.framework.common.json.JsonMapper;
import com.fosung.ksh.monitor.MonitorApplication;
import com.fosung.ksh.monitor.dto.MonitorInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"com.fosung"})
@SpringBootTest(classes = MonitorApplication.class)
public class MonitorServiceImplTest {

    @Autowired
    private MonitorServiceImpl monitorService;

    @Test
    public void queryPersonInfo() {
        List<MonitorInfo> result = monitorService.queryMonitorList();
        for (MonitorInfo personInfo : result) {
            log.info(JsonMapper.toJSONString(personInfo));
        }

    }
}