package com.fosung.ksh.monitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.ksh.monitor.dto.PersonInfo;
import com.fosung.ksh.monitor.http.HikResponse;
import com.fosung.ksh.monitor.service.LibsPeopleService;
import com.fosung.ksh.monitor.util.HikVisionArtemisUtil;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 人脸采集接口
 *
 * @author wangyihua
 * @date 2019-05-30 14:33
 */
@Slf4j
@Service
public class LibsPeopleServiceImpl  implements LibsPeopleService {

    /**
     * 新增黑名单
     *
     * @param humanName 人员姓名
     * @param picBase64 图片base64编码
     * @param listLibId
     * @return
     */
    @Override
    public PersonInfo addPersonInfo(String humanName, String picBase64, String listLibId, String credentialsNum) {
        log.info("新增人员姓名{}，身份证号：{}",humanName,credentialsNum);
        String resultStr = HikVisionArtemisUtil.callApiPostAddRecord(humanName, picBase64, listLibId, credentialsNum);
        HikResponse response = JsonMapper.parseObject(resultStr, HikResponse.class);
        if (response.ok()) {
            return response.data(PersonInfo.class);
        } else {
            log.error("海康接口请求失败：{}", resultStr);
            throw new AppException(response.getMsg());
        }
    }

    /**
     * 修改黑明单信息
     *
     * @param humanId
     * @param picBase64
     * @param listLibId
     * @return
     */
    @Override
    public PersonInfo editPersonInfo(String humanId, String picBase64, String listLibId) {
        String resultStr = HikVisionArtemisUtil.callApiPostEditRecord(humanId, picBase64, listLibId);
        HikResponse response = JsonMapper.parseObject(resultStr, HikResponse.class);
        if (response.ok()) {
            return response.data(PersonInfo.class);
        } else {
            log.error("海康接口请求失败：{}", resultStr);
            throw new AppException(response.getMsg());
        }
    }

    /**
     * 查询黑名单信息
     *
     * @param credentialsNum
     * @param listLibIds
     * @return
     */
    @Override
    public List<PersonInfo> getPersonInfo(String credentialsNum, String listLibIds) {
        ArrayList<PersonInfo> personInfos = Lists.newArrayList();
        String resultStr = HikVisionArtemisUtil.callApiGetRecords(credentialsNum, listLibIds);
        HikResponse response = JsonMapper.parseObject(resultStr, HikResponse.class);

        if (response.ok()) {
            List<JSONObject> list = (List<JSONObject>) ((Map) response.getData()).get("list");
            if (UtilCollection.isEmpty(list)) {
                return null;
            }

            list.forEach(jsonObject->{
                PersonInfo personInfo = JsonMapper.toJavaObject(jsonObject, PersonInfo.class);
                personInfos.add(personInfo);
            });

            return personInfos;
        } else {
            log.error("海康接口请求失败：{}", resultStr);
            throw new AppException(response.getMsg());
        }
    }


    /**
     * 查询黑名单信息
     *
     * @param credentialsNum
     * @param listLibIds
     * @return
     */
    @Override
    public List<PersonInfo> queryPersonInfo(String listLibIds) {
        String resultStr = HikVisionArtemisUtil.callApiGetRecords(null, listLibIds);
        HikResponse response = JsonMapper.parseObject(resultStr, HikResponse.class);
        if (response.ok()) {
            List<JSONObject> list = (List<JSONObject>) ((Map) response.getData()).get("list");
            if (UtilCollection.isEmpty(list)) {
                return new ArrayList<PersonInfo>();
            }
            List<PersonInfo> transformList = Lists.transform(list, new Function<JSONObject, PersonInfo>() {
                @Override
                public PersonInfo apply(JSONObject json) {
                    return JsonMapper.toJavaObject(json, PersonInfo.class);
                }
            });
            List<PersonInfo> recordInfoList = Lists.newArrayList(transformList);
            return recordInfoList;
        } else {
            log.error("海康接口请求失败：{}", resultStr);
            throw new AppException(response.getMsg());
        }
    }


    /**
     * 删除黑名单
     *
     * @param humanId
     * @return
     */
    @Override
    public void deleteBlackPerson(String humanId) {

        String resultStr = HikVisionArtemisUtil.callApiDeleteRecords(humanId);
        HikResponse response = JsonMapper.parseObject(resultStr, HikResponse.class);
        if (!response.ok()) {
            log.error("海康接口请求失败：{}", resultStr);
            throw new AppException(response.getMsg());
        }
    }
}
