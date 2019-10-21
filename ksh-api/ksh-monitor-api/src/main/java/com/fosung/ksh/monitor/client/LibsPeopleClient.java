package com.fosung.ksh.monitor.client;

import com.fosung.ksh.monitor.dto.PersonInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
@FeignClient(name = "ksh-monitor", path = "/libs-people")
public interface LibsPeopleClient {


    /**
     * 新增黑名单
     *
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public PersonInfo addPersonInfo(@RequestBody PersonInfo personInfo);

    /**
     * 修改黑明单信息
     *
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public PersonInfo editPersonInfo(@RequestBody PersonInfo personInfo);

    /**
     * 根据证件号码，查询人员在人脸库中的信息
     *
     * @param credentialsNum
     * @param listLibIds
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.POST)
    public List<PersonInfo> getPersonInfo(
            @RequestParam("credentialsNum") String credentialsNum,
            @RequestParam("listLibIds") String listLibIds) ;

    /**
     * 查询黑名单信息
     *
     * @param listLibIds
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public List<PersonInfo> queryPersonInfo(
            @RequestParam("listLibIds") String listLibIds);
    /**
     * 删除黑名单
     *
     * @param humanId
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public void deleteBlackPerson(@RequestParam("humanId") String humanId);
}
