package com.fosung.ksh.meeting.control.hst.controller;


import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.meeting.control.hst.config.constant.SerachType;
import com.fosung.ksh.meeting.control.hst.request.user.UserinfoRequestDTO;
import com.fosung.ksh.meeting.control.hst.response.UserResponseDTO;
import com.fosung.ksh.meeting.control.hst.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 好视通用户同步API
 */
@Slf4j
@RestController
@Api(description = "好视通用户同步接口", tags = "会控用户接口")
@RequestMapping(value = "/user")
public class UserController extends AppIBaseController {

    @Autowired
    private UserService userService;

    /**
     * 新增用户
     *
     * @param userInfo
     * @return
     */
    @ApiOperation(value = "新增用户")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public void addUserinfo(@RequestBody UserinfoRequestDTO userInfo) throws Exception {
        userService.addUserinfo(userInfo, userInfo.getOrgId());
    }


    /**
     * 修改用户
     *
     * @param userInfo
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "修改用户")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public void editRoominfo(@RequestBody UserinfoRequestDTO userInfo) throws Exception {
        userService.editUser(userInfo, userInfo.getOrgId());
    }

    /**
     * 删除用户
     *
     * @param userName
     */
    @ApiOperation(value = "根据用户名，删除用户")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public void delUser(String userName) throws Exception {
        userService.delUser(userName);
    }

    /**
     * 获取用户信息
     *
     * @param userName
     */
    @ApiOperation(value = "根据用户名，获取用户信息")
    @RequestMapping(value = "get", method = RequestMethod.POST)
    public ResponseEntity<UserResponseDTO> get(String userName) throws Exception {
        return Result.success(userService.getUserInfo(userName));
    }

    /**
     * 获取用户信息
     *
     */
    @ApiOperation(value = "查询用户信息")
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public ResponseEntity<List<Map>> getUserinfoList(String searchKey,
                                                     SerachType serachType,
                                                     Integer currPage,
                                                     Integer pageSize) throws Exception {
        return Result.success(userService.getUserinfoList(searchKey, serachType, currPage, pageSize));
    }


}
