package com.fosung.ksh.meeting.control.hst.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.ksh.common.response.ResponseResult;
import com.fosung.ksh.meeting.control.hst.config.HstProperties;
import com.fosung.ksh.meeting.control.hst.request.org.AddOrgRequestDTO;
import com.fosung.ksh.meeting.control.hst.request.org.EditOrgRequestDTO;
import com.fosung.ksh.meeting.control.hst.service.DepartService;
import com.fosung.ksh.meeting.control.hst.util.HstWebserviceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 组织接口调用
 */
@Slf4j
@Service
public class DepartServiceImpl implements DepartService {

    /**
     * 配置项
     */
    @Autowired
    private HstProperties hstProperties;


    /**
     * 修改
     */
    @Override
    public void editOrg(EditOrgRequestDTO org) throws Exception {
        String method = hstProperties.getMethod().getDepart().getEditDepartinfo();
        String url = hstProperties.getWebServiceUrl();

        org.setKeyCode(hstProperties.getKeyCode());
        Object[] params = org.getValues();
        HstWebserviceUtil.execute(url, method, params);
    }

    /**
     * 新增
     */
    @Override
    public Integer addOrg(AddOrgRequestDTO org) throws Exception {
        String method = hstProperties.getMethod().getDepart().getAddDepartinfo();
        String url = hstProperties.getWebServiceUrl();

        org.setKeyCode(hstProperties.getKeyCode());
        Object[] params = org.getValues();
        ResponseResult responseParam = HstWebserviceUtil.execute(url, method, params);
        return ((JSONArray) ((JSONArray) responseParam.getData()).get(0)).getInteger(0);
    }

    /**
     * 获取当前用户信息列表
     *
     * @param departId   组织Id,可以为空
     * @param departName 可以为空
     * @param currPage   当前页码
     * @param pageSize   每页显示条数
     * @return
     */
    @Override
    public Integer getDepartinfo(Integer departId,
                                 String departName,
                                 String orgId,
                                 Integer currPage,
                                 Integer pageSize) throws Exception {
        String url = hstProperties.getWebServiceUrl();
        String method = hstProperties.getMethod().getDepart().getGetDepartinfo();
        Object[] args = new Object[]{departId, departName, orgId, currPage, pageSize, hstProperties.getKeyCode()};
        ResponseResult responseResult = HstWebserviceUtil.execute(url, method, args);
        List<Map> list = (List<Map>) responseResult.getData();

        if (UtilCollection.isEmpty(list)) {
            return null;
        }
        Map<String, Object> res = list.get(0);
        return Integer.parseInt((String) res.get("departID"));
    }

    /**
     * 获取当前用户信息列表
     *
     * @param departId   组织Id,可以为空
     * @param departName 可以为空
     * @param currPage   当前页码
     * @param pageSize   每页显示条数
     * @return
     */
    @Override
    public List<Map> queyOrgList(Integer departId,
                                 String departName,
                                 String orgId,
                                 Integer currPage,
                                 Integer pageSize) throws Exception {
        String url = hstProperties.getWebServiceUrl();
        String method = hstProperties.getMethod().getDepart().getGetDepartinfo();
        Object[] args = new Object[]{departId, departName, orgId, currPage, pageSize, hstProperties.getKeyCode()};
        ResponseResult responseResult = HstWebserviceUtil.execute(url, method, args);
        List<Map> list = (List<Map>) responseResult.getData();
        return list;
    }
}
