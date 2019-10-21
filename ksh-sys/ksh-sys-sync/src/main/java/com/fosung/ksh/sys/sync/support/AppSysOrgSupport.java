package com.fosung.ksh.sys.sync.support;

import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilString;
import com.fosung.ksh.common.cache.RedisCacheable;
import com.fosung.ksh.sys.config.SysProperties;
import com.fosung.ksh.sys.dto.SysOrg;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mzlion.core.json.TypeRef;
import com.mzlion.easyokhttp.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 获取简项库党组织支持类
 *
 * @author wangyihua
 * @date 2019-06-07 12:12
 */
@Slf4j
@Component
public class AppSysOrgSupport {

    @Autowired
    private SysProperties sysProperties;

    /**
     * 根据组织机构Id获取下级所有组织
     *
     * @param orgId 组织机构id
     * @return
     */
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public List<SysOrg> queryOrgList(String orgId) {
        if (UtilString.isBlank(orgId)) {
            return Lists.newArrayList();
        }


        List<JSONObject> list = HttpClient.post(sysProperties.getUrls().getSimpleData() + "/queryOrgInfo/" + orgId).asBean(new TypeRef<List<JSONObject>>() {
        });

        list = list.stream().filter(
                sysOrg -> UtilString.isNotEmpty(sysOrg.getString("parentId")))
                .collect(Collectors.toList());

        log.debug("简项库党组织查询结果:parentId={}, data={}", orgId, list);

        return transform(list);
    }

    /**
     * 根据组织机构Id获取下级所有组织
     *
     * @param orgId 组织机构id
     * @return
     */
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public List<SysOrg> queryBranchList(String orgId) {
        if (UtilString.isBlank(orgId)) {
            return Lists.newArrayList();
        }

        List<JSONObject> list = HttpClient.post(sysProperties.getUrls().getSimpleData() + "/queryOrgBranchInfo/" + orgId).asBean(new TypeRef<List<JSONObject>>() {
        });

        log.debug("简项库党组织查询结果:parentId={}, data={}", orgId, list);

        return transform(list);
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


    /**
     * json对象转换sysorg对象
     *
     * @param jsonObject
     * @return
     */
    public SysOrg transform(JSONObject jsonObject) {
        String hashasChildren = jsonObject.getString("hasChildren");
        jsonObject.remove("jsonObject");
        SysOrg sysOrg = JsonMapper.toJavaObject(jsonObject, SysOrg.class);
        sysOrg.setOrgCode(jsonObject.getString("code"));
        sysOrg.setHasChildren("1".equals(hashasChildren));
//        sysOrg.setOrderNumber(jsonObject.getInteger("orderId"));
        return sysOrg;
    }
}
