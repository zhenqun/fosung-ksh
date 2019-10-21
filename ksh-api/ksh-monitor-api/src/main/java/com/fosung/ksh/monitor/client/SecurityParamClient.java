package com.fosung.ksh.monitor.client;

import com.fosung.ksh.monitor.dto.SecurityParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: 根据appkey获取加密参数
 * @author LZ
 * @date 2019-05-13 17:07
*/
@FeignClient(name = "ksh-monitor", path = "/monitor/securityparam")
public interface SecurityParamClient {
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    SecurityParam getSecurityParam();
}
