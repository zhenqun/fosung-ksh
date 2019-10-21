package com.fosung.ksh.sys.client;

import com.fosung.ksh.sys.dto.SysOrg;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@FeignClient(name = "ksh-sys-web", path = "/org")
public interface SysOrgClient {



    /**
     * 获取当前登录的用户信息
     *
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.POST)
    public SysOrg getOrgInfo(@RequestParam("orgId") String orgId) ;

    /**
     * 根据orgId，获取党组织详情
     *
     * @param orgId
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public List<SysOrg> queryOrgInfo(@RequestParam("orgId")  String orgId) ;


    /**
     *
     * @param orgId
     * @return
     */
    @RequestMapping(value = "branch", method = RequestMethod.POST)
    public List<SysOrg> branch(@RequestParam("orgId")  String orgId) ;



    /**
     * 根据orgId，获取所有下级党组织ID
     *
     * @param orgId
     * @return
     */
    @RequestMapping(value = "all-id-list", method = RequestMethod.POST)
    public List<String> queryOrgAllChildId(@RequestParam("orgId") String orgId);

}
