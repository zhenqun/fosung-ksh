package com.fosung.ksh.oauth2.client.service.impl;

import com.fosung.framework.common.secure.auth.constant.UserSource;
import com.fosung.ksh.oauth2.client.OauthClientApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@Slf4j
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"com.fosung"})
@SpringBootTest(classes = OauthClientApplication.class)
public class Oauth2ClientServiceImplTest {


    @Autowired
    Oauth2ClientServiceImpl oauth2ClientService;
    @Test
    public void login() {
        oauth2ClientService.login("13853585063","sxd770106",UserSource.DT);
    }
}
