package com.fosung.ksh.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.common.util.UtilTree;
import com.fosung.ksh.common.cache.RedisCacheable;
import com.fosung.ksh.sys.config.SysProperties;
import com.fosung.ksh.sys.dto.SysOrg;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mzlion.core.json.TypeRef;
import com.mzlion.core.utils.BeanUtils;
import com.mzlion.easyokhttp.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysOrgServiceImpl implements com.fosung.ksh.sys.service.SysOrgService {


    @Autowired
    private SysProperties sysProperties;

    /**
     * 根据组织机构id查询详情
     *
     * @param orgId 组织机构id
     * @return
     */

    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public SysOrg getParentInfo(String orgId) {
        if (UtilString.isBlank(orgId)) {
            return null;
        }

        JSONObject json = HttpClient.post(sysProperties.getUrls().getSimpleData() + "/queryParentOrg/" + orgId).asBean(JSONObject.class);

        SysOrg sysOrg = json == null ? null : transform(json);
        if (sysOrg != null) {
            String parentId = sysOrg.getParentId();
            String orgCode = sysOrg.getOrgCode();
            if (UtilString.isNotBlank(parentId)) {
                List<SysOrg> list = queryOrgList(parentId);
                sysOrg = list.stream()
                        .filter(org -> org.getOrgCode().equals(orgCode))
                        .collect(Collectors.toList()).get(0);
            }

        }

        return sysOrg;
    }


    /**
     * 根据组织机构id查询详情
     *
     * @param orgId 组织机构id
     * @return
     */
    @Override
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public SysOrg getOrgInfo(String orgId) {
        if (UtilString.isBlank(orgId)) {
            return null;
        }

        List<JSONObject> list = HttpClient.post(sysProperties.getUrls().getSimpleData() + "/queryOrgInfo/" + orgId).asBean(new TypeRef<List<JSONObject>>() {
        });

        if (UtilCollection.isEmpty(list)) {
            return null;
        }
        SysOrg sysOrg = transform(list.get(0));

        SysOrg parent = getParentInfo(sysOrg.getOrgId());
        if (parent != null) {
            sysOrg.setParentId(parent.getOrgId());
        }
        return sysOrg;
    }

    /**
     * 根据组织机构Id获取下级所有组织
     *
     * @param orgId 组织机构id
     * @return
     */
    @Override
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public List<SysOrg> queryOrgList(String orgId) {
        if (UtilString.isBlank(orgId)) {
            return Lists.newArrayList();
        }

        List<JSONObject> list = HttpClient.post(sysProperties.getUrls().getSimpleData() + "/queryOrgInfo/" + orgId).asBean(new TypeRef<List<JSONObject>>() {
        });


        list = list.stream().filter(sysOrg -> UtilString.isNotEmpty(sysOrg.getString("parentId"))).collect(Collectors.toList());
        return transform(list);
    }

    /**
     * 根据组织机构Id获取下级所有党支部
     *
     * @param orgId 组织机构id
     * @return
     */
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public List<SysOrg> queryOrgBranchInfo(String orgId) {
        if (UtilString.isBlank(orgId)) {
            return Lists.newArrayList();
        }
        List<JSONObject> list = HttpClient.post(sysProperties.getUrls().getSimpleData() + "/queryOrgBranchInfo/" + orgId).asBean(new TypeRef<List<JSONObject>>() {
        });
        return transform(list);
    }


    /**
     * 根据父级ID获取所有直接、简介简项库组织数据,不包含根节点
     * 如果存在子集并且不是当前组织ID，则继续递归去获取数据
     */
    @Override
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public List<SysOrg> queryOrgAllChild(String rootOrgId) {
        List<SysOrg> appDTOrgList = this.queryOrgList(rootOrgId);
        //结果集
        List<SysOrg> result = appDTOrgList.parallelStream().filter(item -> item != null && !Strings.isNullOrEmpty(item.getOrgId())).collect(Collectors.toList());
        appDTOrgList.parallelStream()
                .filter(item -> item.getHasChildren() && !rootOrgId.equalsIgnoreCase(item.getOrgId()))
                .forEach(item -> result.addAll(this.queryOrgAllChild(item.getOrgId())));
        return result;
    }


    /**
     * 根据父级ID获取所有直接、简介简项库组织数据,不包含根节点
     * 如果存在子集并且不是当前组织ID，则继续递归去获取数据
     */
    @Override
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public List<Map<String, Object>> queryOrgAllChild(String rootOrgId, String orgName) {
        List<SysOrg> list = queryOrgAllChild(rootOrgId);
        final List<Map<String, Object>> listMap = Lists.newArrayList();
        SysOrg sysOrg = getOrgInfo(rootOrgId);
        list.add(sysOrg);
        list.stream()
                .filter(org -> org != null
                        && UtilString.isNotEmpty(org.getOrgName())
                        && org.getOrgName().contains(orgName))
                .forEach(org -> {
                    Map<String, Object> treeMap = BeanUtils.toMapAsValueObject(org);
                    listMap.add(treeMap);
                });

        //添加父级节点
        List<Map<String, Object>> target = Lists.newArrayList(listMap);
        for (Map<String, Object> treeMap : listMap) {
            addParentList((String) treeMap.get("parentId"), list, target);
        }

        //对象去重
        target = removeRepeatMapByKey(target, "orgId");
        List<Map<String, Object>> treeList = UtilTree.getTreeData(target, "orgId", "parentId", "children", false);
        return treeList;

    }

    /**
     * 获取过滤出来的全部父节点
     *
     * @param orgId
     * @param source
     * @param target
     */
    private void addParentList(String orgId, List<SysOrg> source, List<Map<String, Object>> target) {
        if (UtilString.isNotEmpty(orgId)) {
            source.stream().filter(sysOrg ->
                    sysOrg != null && orgId.equals(sysOrg.getOrgId())).forEach(sysOrg -> {
                Map<String, Object> treeMap = BeanUtils.toMapAsValueObject(sysOrg);
                target.add(treeMap);
                if (UtilString.isNotEmpty(sysOrg.getParentId())) {
                    addParentList(sysOrg.getParentId(), source, target);
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
        if (UtilCollection.isEmpty(list)) return null;

        //把list中的数据转换成msp,去掉同一id值多余数据，保留查找到第一个id值对应的数据
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Map> msp = new HashMap<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            Map map = list.get(i);
            String id = (String) map.get(mapKey);
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


    /**
     * 对象转换
     *
     * @param list
     * @return
     */
    public List<SysOrg> transform(List<JSONObject> list) {
        List<SysOrg> transformList = Lists.transform(list, new Function<JSONObject, SysOrg>() {
            @Override
            public SysOrg apply(JSONObject json) {
                return transform(json);
            }
        });
        List sysOrgs = Lists.newArrayList(transformList);
        return sysOrgs;
    }

    public SysOrg transform(JSONObject jsonObject) {

        String hasChildren = jsonObject.getString("hasChildren");
        jsonObject.remove("jsonObject");
        SysOrg sysOrg = JsonMapper.toJavaObject(jsonObject, SysOrg.class);
        sysOrg.setOrgCode(jsonObject.getString("code"));
        sysOrg.setHasChildren("1".equals(hasChildren));
        sysOrg.setOrderNumber(jsonObject.getInteger("orderId"));
        return sysOrg;
    }

}

