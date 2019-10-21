package com.fosung.ksh.monitor.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.monitor.config.MonitorProperties;
import com.fosung.ksh.monitor.dto.MonitorInfo;
import com.fosung.ksh.monitor.dto.SecurityParam;
import com.fosung.ksh.monitor.service.MonitorService;
import com.fosung.ksh.monitor.service.SecurityParamService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: fosung-ksh
 * @description: 根据appkey获取加密参数
 * @author: LZ
 * @create: 2019-05-13 17:08
 **/

@RestController
@RequestMapping(value = SecurityParamController.BASE_PATH)
public class SecurityParamController extends AppIBaseController {

    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/monitor/securityparam";

    @Autowired
    private MonitorProperties monitorProperties;

    @Resource
    private SecurityParamService securityParamService;

    @ApiOperation("根据appKey获取加密协议")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity<SecurityParam> getSecurityParam() {
        SecurityParam securityParam = securityParamService.getSecurityParam();
        securityParam.setIp(monitorProperties.getHikVision().getIp());
        securityParam.setPort(monitorProperties.getHikVision().getPort());
        securityParam.setAppkey(monitorProperties.getHikVision().getConfig().getAppKey());
        return Result.success(securityParam);

    }
}