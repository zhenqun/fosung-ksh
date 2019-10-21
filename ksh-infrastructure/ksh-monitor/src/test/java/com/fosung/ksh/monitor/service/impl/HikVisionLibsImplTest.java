package com.fosung.ksh.monitor.service.impl;

import com.fosung.framework.common.json.JsonMapper;
import com.fosung.ksh.monitor.MonitorApplication;
import com.fosung.ksh.monitor.dto.PersonInfo;
import com.fosung.ksh.monitor.dto.PersonRecordInfo;
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
public class HikVisionLibsImplTest {

    @Autowired
    private LibsServiceImpl libsService;
//    @Test
//    public void addPersonInfo() {
//        libsService.addLibs();
//    }
//
//    @Test
//    public void editPersonInfo() {
//    }
//
//    @Test
//    public void getPersonInfo() {
//       List<PersonInfo> result =  libsService.queryPersonInfo("1555065189292");
//        for (PersonInfo personInfo : result) {
//            log.info(JsonMapper.toJSONString(personInfo));
//        }
//
//    }

//    @Test
//    public void getBlackList() {
//        List<PersonRecordInfo> list =  libsService.getBlackList("1556540851561","2011-07-01 01:01:01","2019-05-08 17:01:01","D02029325");
//        for (PersonRecordInfo personRecordInfo : list) {
//            log.info(JsonMapper.toJSONString(personRecordInfo));
//        }
//    }

    @Test
    public void deleteBlackPerson() {
//        libsService.deleteBlackPerson("7da6e26331a8477395d99ddfcfa46400");
    }
}
