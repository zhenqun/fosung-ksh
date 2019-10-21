package com.fosung.ksh.sys.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.sys.entity.SysArea;
import com.fosung.ksh.sys.entity.SysPermission;

import java.util.List;
import java.util.Map;

public interface SysAreaService extends AppBaseDataService<SysArea, Long> {

    /**
     * 通过cityCode获取行政区域详情
     *
     * @param cityCode 组织机构id
     * @return
     */
    public SysArea getAreaInfo(String cityCode);


    /**
     * 通过cityCode获取行政区域详情
     *
     * @param id 组织机构id
     * @return
     */
    SysArea getAreaInfo(Long id);

    /**
     * 根据ID查询全部下级行政区域
     *
     * @param id 组织机构id
     * @return
     */
    List<SysArea> queryAreaList(Long id);

    /**
     * 根据id查询全部村节点
     *
     * @param id 组织机构id
     * @return
     */
    List<SysArea> queryAreaBranchInfo(Long id);

    /**
     * 获取全部下级节点
     */
    List<SysArea> queryAreaAllChild(Long id);

    /**
     * 根据父级ID获取所有直接、简介简项库组织数据,不包含根节点
     * 如果存在子集并且不是当前组织ID，则继续递归去获取数据
     */
    List<Map<String, Object>> queryAreaAllChild(Long id, String areaName);
}
