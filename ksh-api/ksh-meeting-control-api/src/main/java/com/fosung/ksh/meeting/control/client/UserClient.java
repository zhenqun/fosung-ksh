package com.fosung.ksh.meeting.control.client;

import com.fosung.ksh.meeting.control.constant.SerachType;
import com.fosung.ksh.meeting.control.dto.user.UserResponseDTO;
import com.fosung.ksh.meeting.control.dto.user.UserinfoRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 好视通视频会议用户模块接口
 *
 * @author wangyh
 */
@FeignClient(name = "ksh-meeting-control", path = "/user")
public interface UserClient {


    /**
     * 根据username修改用户
     *
     * @param user
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public void editUser(@RequestBody UserinfoRequestDTO user);

    /**
     * 新增用户
     *
     * @param user
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addUserinfo(@RequestBody UserinfoRequestDTO user);


    /**
     * 根据用户名删除用户
     *
     * @param userName
     * @throws Exception
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delUser(@RequestParam("userName") String userName);

    /**
     * 获取用户信息
     *
     * @param userName
     */
    @RequestMapping(value = "get", method = RequestMethod.POST)
    public UserResponseDTO get(@RequestParam("userName") String userName);


    @RequestMapping(value = "query", method = RequestMethod.POST)
    public List<Map> query(@RequestParam("searchKey") String searchKey,
                           @RequestParam("serachType") SerachType serachType,
                           @RequestParam("currPage") Integer currPage,
                           @RequestParam("pageSize") Integer pageSize);
}
