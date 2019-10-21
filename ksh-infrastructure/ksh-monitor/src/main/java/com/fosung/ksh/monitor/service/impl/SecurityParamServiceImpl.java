package com.fosung.ksh.monitor.service.impl;

import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.ksh.monitor.config.MonitorProperties;
import com.fosung.ksh.monitor.dto.SecurityParam;
import com.fosung.ksh.monitor.http.HikResponse;
import com.fosung.ksh.monitor.service.SecurityParamService;
import com.fosung.ksh.monitor.util.HikVisionArtemisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: fosung-ksh
 * @description: 根据appkey获取加密参数
 * @author: LZ
 * @create: 2019-05-13 17:04
 **/
@Service
@Slf4j
public class SecurityParamServiceImpl implements SecurityParamService {


    @Override
    public SecurityParam getSecurityParam() {
        String resultStr = HikVisionArtemisUtil.getSecurityParam();
        HikResponse response = JsonMapper.parseObject(resultStr, HikResponse.class);
        if (response.ok()) {
            return response.data(SecurityParam.class);
        } else {
            log.error("获取获取加密协议失败：{}", resultStr);
            throw new AppException(response.getMsg());
        }
    }
}
