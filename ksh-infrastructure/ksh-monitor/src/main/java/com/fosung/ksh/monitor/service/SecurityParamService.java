package com.fosung.ksh.monitor.service;

import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilString;
import com.fosung.ksh.monitor.dto.SecurityParam;
import com.google.common.collect.Maps;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import org.springframework.util.Assert;

import java.util.HashMap;

/**
 * @program: fosung-ksh
 * @description: 根据appkey获取加密协议
 * @author: LZ
 * @create: 2019-05-13 16:55
 **/

public interface SecurityParamService {
    /**
     * 根据appkey获取加密协议
     * @return
     */
    SecurityParam getSecurityParam();
}
