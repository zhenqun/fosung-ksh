package com.fosung.ksh.meeting.control.hst.service;

import com.fosung.ksh.meeting.control.hst.config.constant.SerachType;
import com.fosung.ksh.meeting.control.hst.request.user.UserinfoRequestDTO;
import com.fosung.ksh.meeting.control.hst.response.UserResponseDTO;

import java.util.List;
import java.util.Map;

/**
 * 好视通用户同步接口
 *
 * @author wangyh
 */
public interface UserService {

    /**
     * 根据用户名修改用户
     *
     * @param user
     * @param orgId
     * @return
     * @throws Exception
     */
    public void editUser(UserinfoRequestDTO user, String orgId) throws Exception;

    /**
     * 新增用户
     *
     * @param user
     * @param orgId
     * @return
     * @throws Exception
     */
    public void addUserinfo(UserinfoRequestDTO user, String orgId) throws Exception;


    /**
     * 删除用户
     *
     * @param userName
     * @throws Exception
     */
    public void delUser(String userName) throws Exception;

    /**
     * 通过 userName获取用户信息
     *
     * @param userName
     * @return
     * @throws Exception
     */
    public UserResponseDTO getUserInfo(String userName) throws Exception;

    /**
     * 获取当前用户信息列表
     *
     * @param serachKey  查询关键字
     * @param serachType 类型值 0为前后模糊搜索，1全匹配
     * @param currPage   当前页码
     * @param pageSize   每页显示条数
     * @return
     */
    public List<Map> getUserinfoList(String serachKey,
                                     SerachType serachType,
                                     Integer currPage,
                                     Integer pageSize) throws Exception;

}
