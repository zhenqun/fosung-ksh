package com.fosung.ksh.monitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.ksh.monitor.dto.MonitorInfo;
import com.fosung.ksh.monitor.http.HikResponse;
import com.fosung.ksh.monitor.util.HikVisionArtemisUtil;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 海康人脸库接口
 */
@Slf4j
@Service
public class MonitorServiceImpl implements com.fosung.ksh.monitor.service.MonitorService {


    /**
     * 查询黑名单信息
     *
     * @param credentialsNum
     * @param listLibIds
     * @return
     */
    @Override
    public List<MonitorInfo> queryMonitorList() {
        String resultStr = HikVisionArtemisUtil.callApiGetDeviceList();

        HikResponse response = JsonMapper.parseObject(resultStr, HikResponse.class);
        if ("200".equals(response.getCode())) {
            List<JSONObject> list = (List<JSONObject>) response.getData();

            if (UtilCollection.isEmpty(list)) {
                return new ArrayList<MonitorInfo>();
            }

            List<MonitorInfo> transformList = Lists.transform(list, new Function<JSONObject, MonitorInfo>() {
                @Override
                public MonitorInfo apply(JSONObject json) {
                    return JsonMapper.toJavaObject(json, MonitorInfo.class);
                }
            });
            List<MonitorInfo> recordInfoList = Lists.newArrayList(transformList);
            return recordInfoList;
        } else {
            log.error("海康接口请求失败：{}", resultStr);
            throw new AppException(response.getMsg());
        }
    }

}
