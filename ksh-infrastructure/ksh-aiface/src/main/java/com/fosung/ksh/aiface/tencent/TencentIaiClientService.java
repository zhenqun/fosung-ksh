package com.fosung.ksh.aiface.tencent;

import com.fosung.ksh.aiface.config.AiFaceProperties;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.iai.v20180301.IaiClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@Service
public class TencentIaiClientService {

    @Resource
    private AiFaceProperties appAiFaceProperties;



    public IaiClient getIaiClient() {
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
        Credential cred = new Credential(appAiFaceProperties.getSecretId(), appAiFaceProperties.getSecretKey());
        return new IaiClient(cred, appAiFaceProperties.getRegion());
    }
}
