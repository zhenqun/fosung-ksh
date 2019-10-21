package com.fosung.ksh.oauth2.client;

import com.fosung.ksh.oauth2.client.dto.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "ksh-oauth2-client")
public interface Oauth2Client {
    /**
     * 获取当前登录的用户信息
     *
     * @return
     */
    @RequestMapping(value = "user/info", method = RequestMethod.POST)
    public SysUser userInfo();

}
