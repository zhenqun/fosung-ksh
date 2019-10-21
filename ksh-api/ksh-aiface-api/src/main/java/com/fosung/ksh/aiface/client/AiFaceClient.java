package com.fosung.ksh.aiface.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 人脸采集接口
 *
 * @author wangyh
 */
@FeignClient(name = "ksh-aiface", path = "/face")
public interface AiFaceClient {


    /**
     * 人脸采集接口
     *
     * @param user
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void create(@RequestParam("personName") String personName,
                       @RequestParam("userHash") String userHash,
                       @RequestParam("url") String url) throws Exception;

}
