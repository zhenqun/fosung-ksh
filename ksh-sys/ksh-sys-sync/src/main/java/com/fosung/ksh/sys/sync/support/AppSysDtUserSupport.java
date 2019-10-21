package com.fosung.ksh.sys.sync.support;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilString;
import com.fosung.ksh.common.cache.RedisCacheable;
import com.fosung.ksh.sys.config.SysProperties;
import com.fosung.ksh.sys.entity.SysDtUser;
import com.google.common.collect.Lists;
import com.mzlion.easyokhttp.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 获取简项库党组织支持类
 *
 * @author wangyihua
 * @date 2019-06-07 12:12
 */
@Slf4j
@Component
public class AppSysDtUserSupport {

    @Autowired
    private SysProperties sysProperties;

    /**
     * 根据支部ID,获取支部所有用户
     *
     * @param orgId 组织机构id
     * @return
     */
    @RedisCacheable(expired = 12, timeUnit = TimeUnit.HOURS)
    public List<SysDtUser> queryUserAllByOrgId(String orgId) {
        if (UtilString.isBlank(orgId)) {
            return Lists.newArrayList();
        }

        List<SysDtUser> users = Lists.newArrayList();
        JSONObject json = HttpClient.post(sysProperties.getUrls().getSimpleData() + "/queryUserAllByOrgId/" + orgId).asBean(JSONObject.class);
        JSONArray array = json.getJSONArray("users");

        log.debug("简项库党员信息查询结果:orgId={}, data={}", orgId, array);
        if (UtilCollection.isNotEmpty(array)) {
            array.stream().forEach(obj -> {
                JSONObject jsonObj = (JSONObject) obj;
                SysDtUser sysUser = JsonMapper.toJavaObject(jsonObj, SysDtUser.class);
                sysUser.setRealName(jsonObj.getString("param1"));
                users.add(sysUser);
            });
        }

        return users;
    }

}
