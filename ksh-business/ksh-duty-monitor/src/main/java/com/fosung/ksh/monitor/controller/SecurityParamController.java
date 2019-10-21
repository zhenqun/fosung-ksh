package com.fosung.ksh.monitor.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.monitor.client.SecurityParamClient;
import com.fosung.ksh.monitor.dto.SecurityParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @program: fosung-ksh
 * @description: 根据appKey获取加密协议
 * @author: LZ
 * @create: 2019-05-13 17:16
 **/

@Api(description = "根据appKey获取加密协议", tags = {"3、获取加密参数"})
@RestController
@RequestMapping(value = SecurityParamController.BASE_PATH)
public class SecurityParamController extends AppIBaseController {

    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/monitor/securityparam";


    /**
     * 查询加密参数
     */
    @Autowired
    private SecurityParamClient securityParamClient;


    /**
     * 获取加密参数
     * @throws Exception
     */
    @ApiOperation("根据appKey获取加密协议")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity<SecurityParam> getSecurityParam() {
        return Result.success(securityParamClient.getSecurityParam());
    }
}
