package com.fosung.ksh.monitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.ksh.monitor.dto.PersonInfo;
import com.fosung.ksh.monitor.dto.PersonRecordInfo;
import com.fosung.ksh.monitor.http.HikResponse;
import com.fosung.ksh.monitor.util.HikVisionArtemisUtil;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 海康人脸库接口
 */
@Slf4j
@Service
public class SignRecordServiceImpl implements com.fosung.ksh.monitor.service.SignRecordService {


    /**
     * 查询人员签到记录，对应海康的黑名单预警信息
     *
     * @param libIds
     * @param startAlarmTime
     * @param endAlarmTime
     * @return
     */
    @Override
    public List<PersonRecordInfo> getBlackList(String libIds, String startAlarmTime, String endAlarmTime, String indexCode) {
        String resultStr =  HikVisionArtemisUtil.callApiPostBlackList(libIds, startAlarmTime, endAlarmTime, indexCode);
        HikResponse response = JsonMapper.parseObject(resultStr, HikResponse.class);
        if (response.ok()) {
            List<JSONObject> list = (List<JSONObject>) ((Map) response.getData()).get("list");
            if (UtilCollection.isEmpty(list)) {
                return new ArrayList<PersonRecordInfo>();
            }
            List<PersonRecordInfo> transformList = Lists.transform(list, new Function<JSONObject, PersonRecordInfo>() {
                @Override
                public PersonRecordInfo apply(JSONObject json) {
                    return JsonMapper.toJavaObject(json,PersonRecordInfo.class);
                }
            });
            List<PersonRecordInfo>  recordInfoList = Lists.newArrayList(transformList);
            return recordInfoList;
        } else {
            log.error("海康接口请求失败：{}", resultStr);
            throw new AppException(response.getMsg());
        }

    }
}
