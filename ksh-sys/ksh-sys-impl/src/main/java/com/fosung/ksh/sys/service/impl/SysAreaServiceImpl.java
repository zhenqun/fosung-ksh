package com.fosung.ksh.sys.service.impl;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.common.util.UtilTree;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.common.cache.RedisCacheable;
import com.fosung.ksh.sys.dao.SysAreaDao;
import com.fosung.ksh.sys.entity.SysArea;
import com.fosung.ksh.sys.entity.constant.AreaType;
import com.fosung.ksh.sys.service.SysAreaService;
import com.mzlion.core.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author wangyh
 */
@Slf4j
@Service
public class SysAreaServiceImpl extends AppJPABaseDataServiceImpl<SysArea, SysAreaDao>
        implements SysAreaService {


    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("areaCode", "areaCode:EQ");
            put("parentId", "parentId:EQ");
            put("areaType", "areaType:EQ");
            put("rootAreaCode", "areaCode:RLIKE");
            put("isUse", "isUse:EQ");
        }
    };


    /**
     * 根据组织机构id查询详情
     *
     * @param areaCode 组织机构id
     * @return
     */
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public SysArea getAreaInfo(String areaCode) {
        if (UtilString.isBlank(areaCode)) {
            return null;
        }
        Map<String, Object> searchParam = Maps.newHashMap("areaCode", areaCode);
        List<SysArea> list = queryAll(searchParam);
        SysArea sysArea = null;
        // 获取父级的行政名称
        if (UtilCollection.isNotEmpty(list)) {
            sysArea = list.get(0);
            Long parentId = sysArea.getParentId();
            if (parentId != null && parentId != 0) {
                SysArea parent = get(parentId);
                if (parent != null) {
                    String cityName = parent.getAreaName();
                    sysArea.setParentName(cityName);
                }
            }

        }
        return sysArea;
    }

    /**
     * 通过areaCode获取行政区域详情
     *
     * @param id 行政区域ID
     * @return
     */
    @Override
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public SysArea getAreaInfo(Long id) {
        SysArea sysArea = get(id);
        if (sysArea != null) {
            Long parentId = sysArea.getParentId();
            // 获取父级的行政名称
            if (parentId != null && parentId != 0) {
                SysArea parent = get(parentId);
                String cityName = parent.getAreaName();
                sysArea.setParentName(cityName);
            }
        }
        return sysArea;
    }


    /**
     * 根据ID查询全部下级行政区域
     *
     * @param id 组织机构id
     * @return
     */
    @Override
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public List<SysArea> queryAreaList(Long id) {
        Map<String, Object> searchParam = Maps.newHashMap("parentId", id);
        searchParam.put("isUse", true);
        List<SysArea> list = queryAll(searchParam);
        return list;
    }


    /**
     * 根据id查询全部村节点
     *
     * @param id 组织机构id
     * @return
     */
    @Override
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public List<SysArea> queryAreaBranchInfo(Long id) {
        SysArea sysArea = get(id);
        Map<String, Object> searchParam = Maps.newHashMap("rootAreaCode", sysArea.getAreaCode());
        searchParam.put("isUse", true);
        searchParam.put("areaType", AreaType.VILLAGE);
        List<SysArea> list = queryAll(searchParam);
        return list;
    }


    /**
     * 获取全部下级节点
     */
    @Override
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public List<SysArea> queryAreaAllChild(Long id) {
        SysArea sysArea = get(id);
        Map<String, Object> searchParam = Maps.newHashMap("rootAreaCode", sysArea.getAreaCode());
        searchParam.put("isUse", true);
        List<SysArea> list = queryAll(searchParam);
        return list;
    }


    /**
     * 根据父级ID获取所有直接、简介简项库组织数据,不包含根节点
     * 如果存在子集并且不是当前组织ID，则继续递归去获取数据
     */
    @Override
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public List<Map<String, Object>> queryAreaAllChild(Long id, String areaName) {
        List<SysArea> list = queryAreaAllChild(id);
        final List<Map<String, Object>> listMap = Lists.newArrayList();

        list.stream()
                .filter(area -> area != null
                        && UtilString.isNotEmpty(area.getAreaName())
                        && area.getAreaName().contains(areaName))
                .forEach(area -> {
                    Map<String, Object> treeMap = BeanUtils.toMapAsValueObject(area);
                    listMap.add(treeMap);
                });


        //添加父级节点
        List<Map<String, Object>> target = Lists.newArrayList(listMap);
        for (Map<String, Object> treeMap : listMap) {
            addParentList((Long) treeMap.get("parentId"), list, target);
        }

        //对象去重
        target = removeRepeatMapByKey(target, "id");
        List<Map<String, Object>> treeList = UtilTree.getTreeData(target, "id", "parentId", "children", false);
        return treeList;

    }

    /**
     * 获取过滤出来的全部父节点
     *
     * @param id
     * @param source
     * @param target
     */
    private void addParentList(Long id, List<SysArea> source, List<Map<String, Object>> target) {
        if (id != null && id != 0) {
            source.stream().filter(sysArea ->
                    sysArea != null && id == sysArea.getId()).forEach(sysArea -> {

                Map<String, Object> treeMap = BeanUtils.toMapAsValueObject(sysArea);
                target.add(treeMap);

                if (sysArea.getParentId() != null && sysArea.getParentId() != 0) {
                    addParentList(sysArea.getParentId(), source, target);
                }
            });
        }
    }

    /**
     * 根据map中的某个key 去除List中重复的map
     *
     * @param list
     * @param mapKey
     * @return
     * @author shijing
     */
    public static List<Map<String, Object>> removeRepeatMapByKey(List<Map<String, Object>>
                                                                         list, String mapKey) {
        if (UtilCollection.isEmpty(list)) {
            return null;
        }

        // 把list中的数据转换成msp,去掉同一id值多余数据，保留查找到第一个id值对应的数据
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Map> msp = new HashMap<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            Map map = list.get(i);
            String id = map.get(mapKey).toString();
            map.remove(mapKey);
            msp.put(id, map);
        }
        //把msp再转换成list,就会得到根据某一字段去掉重复的数据的List<Map>
        Set<String> mspKey = msp.keySet();
        for (String key : mspKey) {
            Map newMap = msp.get(key);
            newMap.put(mapKey, key);
            listMap.add(newMap);
        }
        return listMap;
    }


    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }
}
