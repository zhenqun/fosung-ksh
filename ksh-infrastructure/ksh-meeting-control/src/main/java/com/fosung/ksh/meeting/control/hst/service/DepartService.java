package com.fosung.ksh.meeting.control.hst.service;

import com.fosung.ksh.meeting.control.hst.request.org.AddOrgRequestDTO;
import com.fosung.ksh.meeting.control.hst.request.org.EditOrgRequestDTO;

import java.util.List;
import java.util.Map;

/**
 * @author wangyh
 */
public interface DepartService {
    /**
     * 修改
     */
    public void editOrg(EditOrgRequestDTO org) throws Exception;

    /**
     * 新增
     */
    public Integer addOrg(AddOrgRequestDTO org) throws Exception;


    /**
     * 获取组织ID
     *
     * @param departId   组织Id,可以为空
     * @param departName 可以为空
     * @param currPage   当前页码
     * @param pageSize   每页显示条数
     * @return
     */
    Integer getDepartinfo(Integer departId,
                          String departName,
                          String orgId,
                          Integer currPage,
                          Integer pageSize) throws Exception;

    /**
     * 获取当前用户信息列表
     *
     * @param departId   组织Id,可以为空
     * @param departName 可以为空
     * @param currPage   当前页码
     * @param pageSize   每页显示条数
     * @return
     */
    public List<Map> queyOrgList(Integer departId,
                                 String departName,
                                 String orgId,
                                 Integer currPage,
                                 Integer pageSize) throws Exception;
}
