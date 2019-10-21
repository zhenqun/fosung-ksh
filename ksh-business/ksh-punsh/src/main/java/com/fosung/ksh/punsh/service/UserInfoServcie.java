package com.fosung.ksh.punsh.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.punsh.entity.UserInfo;


/**
 * @author lqyu
 * @date 2019-4-1 17:35
 */
public interface UserInfoServcie extends AppBaseDataService<UserInfo,Long> {

    UserInfo findUserInfoByPin(String s, String sn);

    void updateUser(UserInfo user, String sn);
}
