package com.fosung.ksh.meeting.control.hst.service.impl;

import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilString;
import com.fosung.ksh.common.response.ResponseResult;
import com.fosung.ksh.meeting.control.hst.config.HstProperties;
import com.fosung.ksh.meeting.control.hst.config.constant.SerachType;
import com.fosung.ksh.meeting.control.hst.request.user.UserinfoRequestDTO;
import com.fosung.ksh.meeting.control.hst.response.UserResponseDTO;
import com.fosung.ksh.meeting.control.hst.service.DepartService;
import com.fosung.ksh.meeting.control.hst.service.UserService;
import com.fosung.ksh.meeting.control.hst.util.HstWebserviceUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * 同步好视通用户数据
 *
 * @author wangyh
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * 配置项
     */
    @Autowired
    private HstProperties hstProperties;

    @Autowired
    private DepartService departService;


    /**
     * 修改用户
     *
     * @param user
     * @param orgId
     * @return
     */
    @Override
    public void editUser(UserinfoRequestDTO user, String orgId) throws Exception {
        String method = hstProperties.getMethod().getUser().getEditUser();
        user.setDepartID(this.getDepartId(orgId));
        addOrEditUserInfo(method, user);
    }

    /**
     * 获取好视通组织ID
     *
     * @param orgId
     * @return
     * @throws Exception
     */
    private Integer getDepartId(String orgId) throws Exception {
        Assert.notNull(orgId, "党组织Id不能为空");

        Integer departId = departService.getDepartinfo(0, "", orgId, 0, 20);
        Assert.notNull(departId, "未找到对应的党组织");
        return departId;

    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @Override
    public void addUserinfo(UserinfoRequestDTO user, String orgId) throws Exception {
        if (UtilString.isBlank(user.getPasspwd())) {
            user.setPasswordType(1);
            String encodeStr = DigestUtils.md5Hex(hstProperties.getDefaultPwd());
            user.setPasspwd(encodeStr);
        }
        user.setDepartID(this.getDepartId(orgId));
        String method = hstProperties.getMethod().getUser().getAddUserinfo();
        addOrEditUserInfo(method, user);
    }

    /**
     * 新增或者修改用户
     *
     * @param method
     * @param user
     * @return
     * @throws Exception
     */
    private void addOrEditUserInfo(String method, UserinfoRequestDTO user) throws Exception {
        String url = hstProperties.getWebServiceUrl();

        user.setKeyCode(hstProperties.getKeyCode());
        Object[] params = user.getValues();
        HstWebserviceUtil.execute(url, method, params);
    }

    /**
     * 删除用户
     *
     * @param userName
     */
    @Override
    public void delUser(String userName) throws Exception {
        String url = hstProperties.getWebServiceUrl();
        String method = hstProperties.getMethod().getUser().getDelUser();
        Object[] args = new Object[]{userName, hstProperties.getKeyCode()};

        HstWebserviceUtil.execute(url, method, args);
    }

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
                                     Integer pageSize) throws Exception {
        String url = hstProperties.getWebServiceUrl();
        String method = hstProperties.getMethod().getUser().getGetUserinfoList();
        Object[] args = new Object[]{serachKey, serachType.getCode(), currPage, pageSize, hstProperties.getKeyCode()};
        ResponseResult responseParam = HstWebserviceUtil.execute(url, method, args);
        List<Map> list = (List<Map>) responseParam.getData();
        return list;
    }

    /**
     * 通过 userName获取用户信息
     *
     * @param userName
     * @return
     * @throws Exception
     */
    public UserResponseDTO getUserInfo(String userName) throws Exception {
        List<Map> list = getUserinfoList(userName, SerachType.LR, 0, 10);
        if (UtilCollection.isNotEmpty(list)) {
            Map<String, Object> map = list.get(0);
            UserResponseDTO user = JsonMapper.parseObject(JsonMapper.toJSONString(map), UserResponseDTO.class);
            return user;
        }
        return null;

    }
}
