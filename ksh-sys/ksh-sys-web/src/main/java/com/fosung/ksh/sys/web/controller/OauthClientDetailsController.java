package com.fosung.ksh.sys.web.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.sys.entity.OauthClientDetails;
import com.fosung.ksh.sys.service.OauthClientDetailsService;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangyh
 */
@Api(description = "OAuth", tags = {"2、OAuth"})
@RestController
@RequestMapping(value = OauthClientDetailsController.BASE_PATH)
public class OauthClientDetailsController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/oauth-client-details";

    @Autowired
    private OauthClientDetailsService oauthClientDetailsService;

    @ApiOperation(value = "OAuth配置列表")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseEntity<OauthClientDetails> query() {
        return Result.success(oauthClientDetailsService.queryAll(Maps.newHashMap()));
    }


}
