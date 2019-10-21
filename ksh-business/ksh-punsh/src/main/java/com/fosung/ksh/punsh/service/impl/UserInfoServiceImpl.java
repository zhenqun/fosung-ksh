package com.fosung.ksh.punsh.service.impl;

import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;

import com.fosung.ksh.punsh.dao.UserInfoDao;
import com.fosung.ksh.punsh.entity.UserInfo;
import com.fosung.ksh.punsh.service.UserInfoServcie;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @date 2019-4-1 17:36
 */
@Service
public class UserInfoServiceImpl extends AppJPABaseDataServiceImpl<UserInfo,UserInfoDao> implements UserInfoServcie {

    private  Map<String,String> map = Maps.newHashMap();

    @Override
    public Map<String, String> getQueryExpressions() {
        map.put("pin","pin:EQ");
        return map;
    }

    @Override
    public UserInfo findUserInfoByPin(String s, String sn) {
        UserInfo userInfo = this.entityDao.findUserInfoByPin(s, sn);
        if (userInfo != null) {
            return userInfo;
        } else {
            return null;
        }
    }

    @Override
    public void updateUser(UserInfo user, String sn) {
        String pin = user.getPin();
        String card = user.getCard();
        String grp = user.getGrp();
        String name = user.getName();
        String password = user.getPassword();
        Date time = user.getLastUpdateDatetime();
        this.entityDao.updateUser(pin, card, grp, name, password, time, sn);
    }
}
