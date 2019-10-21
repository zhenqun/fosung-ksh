package com.fosung.ksh.aiface.service;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;

/**
 * @author wangyh
 */
public interface AiFaceService {
    public void create(String personName,  String userHash, String url) throws TencentCloudSDKException;
}
