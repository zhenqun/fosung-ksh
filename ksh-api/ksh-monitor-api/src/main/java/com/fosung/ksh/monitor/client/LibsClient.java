package com.fosung.ksh.monitor.client;

import com.fosung.ksh.monitor.dto.PersonInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
@FeignClient(name = "ksh-monitor", path = "/libs")
public interface LibsClient {


    /**
     * 新建人脸库
     *
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addLibs(@RequestParam("listLibName") String listLibName,
                          @RequestParam("describe") String describe);

    /**
     * 修改人脸库
     *
     * @param listLibId
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public  void modifyLibs(@RequestParam("listLibId") String listLibId,
                                      @RequestParam("listLibName") String listLibName,
                                      @RequestParam("describe") String describe);

    /**
     * 删除人脸库
     * @param listLibId
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public void deleteLibs(@RequestParam("listLibId") String listLibId);

}
