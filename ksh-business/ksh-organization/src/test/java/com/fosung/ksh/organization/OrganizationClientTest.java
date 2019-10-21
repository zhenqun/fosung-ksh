package com.fosung.ksh.organization;

import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.util.UtilString;
import com.fosung.ksh.organization.entity.OrgLife;
import com.fosung.ksh.organization.service.OrgLifeService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mzlion.easyokhttp.HttpClient;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 用户信息
 */
@Slf4j
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"com.fosung"})
@SpringBootTest(classes = OrganizationApplication.class)
public class OrganizationClientTest {

    private static final long serialVersionUID = 166454455614338585L;


    @Autowired
    private OrgLifeService orgLifeService;


    @Test
    public void sync() {
        List<OrgLife> list = orgLifeService.queryAll(Maps.newHashMap());
        for (OrgLife orgLife : list) {
            try {
                JSONObject o = HttpClient.post("http://visual.fosung.com:90/ksh-organization/org-life/get").param("id", orgLife.getId().toString()).asBean(JSONObject.class);
                if (o != null && UtilString.isNotBlank(o.getString("meetingContent"))) {
                    log.info("主要内容：" + o.get("meetingContent"));
                    orgLife.setMeetingContent(o.getString("meetingContent"));
                    orgLifeService.update(orgLife, Sets.newHashSet("meetingContent"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
