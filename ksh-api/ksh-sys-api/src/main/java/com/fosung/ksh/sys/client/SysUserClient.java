package com.fosung.ksh.sys.client;

import com.fosung.ksh.sys.dto.SysRole;
import com.fosung.ksh.sys.dto.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@FeignClient(name = "ksh-sys-web", path = "/user")
public interface SysUserClient {

    /**
     * 获取当前登录的用户信息
     *
     * @return
     */
    @RequestMapping(value = "info", method = RequestMethod.POST)
    public SysUser userInfo();

    @RequestMapping(value = "/user-name", method = RequestMethod.POST)
    public SysUser getUserInfoByUserName(@RequestParam("userName") String userName);


    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public List<SysRole> queryRoleList(@RequestParam("userHash") String userHash);


    /**
     * 获取用户详细信息
     *
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.POST)
    public SysUser getUserInfo(@RequestParam("userHash") String userHash);

    /**
     * 查询党组织下的全部用户
     *
     * @param orgId
     * @return
     */
    @RequestMapping(value = "/dt-user-list", method = RequestMethod.POST)
    public List<SysUser> queryDTUserByOrgId(@RequestParam("orgId") String orgId);

    /**
     * 查询党组织下的全部用户
     *
     * @param orgId
     * @return
     */
    @RequestMapping(value = "/local-user-list", method = RequestMethod.POST)
    public List<SysUser> queryLocalUserByOrgId(@RequestParam("orgId") String orgId);

    /**
     * 查询目录下当前时间以后同步的数据
     *
     * @param date
     * @return
     */
    @RequestMapping(value = "/update-list", method = RequestMethod.POST)
    public List<SysUser> queryUpdateList(@RequestParam("orgId") String orgId,
                                         @RequestParam("date") Date date);


    @RequestMapping(value = "/permission-user-list", method = RequestMethod.POST)
    public ResponseEntity<List<SysUser>> queryUserByPermissionList(@RequestParam("permissionName") String permissionName,
                                                                   @RequestParam("manageId") String manageId,
                                                                   @RequestParam("clientId") String clientId);

    @RequestMapping(value = "/permission-all-user-list", method = RequestMethod.POST)
    public ResponseEntity<List<SysUser>> queryAllUserByPermissionList(@RequestParam("permissionName") String permissionName,
                                                                      @RequestParam("manageId") String manageId,
                                                                      @RequestParam("clientId") String clientId);
}
