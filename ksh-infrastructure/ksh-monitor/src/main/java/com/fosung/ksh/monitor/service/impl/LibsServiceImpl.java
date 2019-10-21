package com.fosung.ksh.monitor.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.ksh.monitor.http.HikResponse;
import com.fosung.ksh.monitor.util.HikVisionArtemisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 海康人脸库接口
 */
@Slf4j
@Service
public class LibsServiceImpl implements com.fosung.ksh.monitor.service.LibsService {


    /**
     * 创建人脸库接口
     *
     * @param listLibName 人脸库名称
     * @param describe    描述
     * @return
     */
    public String addLibs(String listLibName, String describe) {
        String str = HikVisionArtemisUtil.callApiPostCreatListLb(listLibName, 2, describe);
        HikResponse response = JsonMapper.parseObject(str, HikResponse.class);
        if (response.ok()) {
            String listLibId = response.data(JSONObject.class).getString("listLibId");
            return listLibId;
        } else {
            log.error("海康接口请求失败：{}", str);
            throw new AppException(response.getMsg());
        }
    }


    /**
     * 修改人脸库接口
     *
     * @param listLibName 人脸库名称
     * @param describe    描述
     * @return
     */
    public void modifyLibs(String listLibId, String listLibName, String describe) {
        String str = HikVisionArtemisUtil.callApiPostModifyListLb(listLibId, listLibName, describe);
        HikResponse response = JsonMapper.parseObject(str, HikResponse.class);
        if (!response.ok()) {
            log.error("海康接口请求失败：{}", str);
            throw new AppException(response.getMsg());
        }
    }


    /**
     * 删除人脸库接口
     *
     * @return
     */
    public void deleteLibs(String listLibId) {
        String str = HikVisionArtemisUtil.callApiPostDeleteListLb(listLibId);
        HikResponse response = JsonMapper.parseObject(str, HikResponse.class);
        if (!response.ok()) {
            log.error("海康接口请求失败：{}", str);
            throw new AppException(response.getMsg());
        }
    }


}
