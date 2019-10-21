package com.fosung.ksh.aiface.service.impl;

import com.fosung.ksh.aiface.config.AiFaceProperties;
import com.fosung.ksh.aiface.service.AiFaceService;
import com.fosung.ksh.aiface.tencent.PersonService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.iai.v20180301.models.GetPersonBaseInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 人脸采集
 */
@Slf4j
@Service
public class AiFaceServiceImpl implements AiFaceService {


    @Resource
    private PersonService personService;


    @Resource
    private AiFaceProperties appAiFaceProperties;


    /**
     * 在腾讯云中创建人脸信息
     *
     * @param personName
     * @param userHash
     * @param url        人脸照片
     * @throws TencentCloudSDKException
     */
    public void create(String personName, String userHash, String url) throws TencentCloudSDKException {

        GetPersonBaseInfoResponse personInfo = null;
        try {
            personService.delete(userHash);
        } catch (TencentCloudSDKException e) {
            log.error("人员删除失败:userHash = {},\nException = {}", userHash, ExceptionUtils.getStackTrace(e));
        }
        personService.create(appAiFaceProperties.getGroupId(), personName, userHash, url);
    }

}
