package com.fosung.ksh.surface.client;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 好视通视频会议用户模块接口
 *
 * @author wangyh
 */
@FeignClient(name = "ksh-data-sync")
public interface SyncUserClient {

    /**
     * 根据用户hash获取用户详细信息
     *
     * @return
     */
    @RequestMapping(value = "/sync/user", method = RequestMethod.POST)
    public List<JSONObject> syncUser(@RequestParam("syncDate") String syncDate);

}
