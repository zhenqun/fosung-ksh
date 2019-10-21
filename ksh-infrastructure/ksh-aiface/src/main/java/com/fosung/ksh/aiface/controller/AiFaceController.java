package com.fosung.ksh.aiface.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.aiface.service.AiFaceService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping(value = AiFaceController.BASE_PATH)
public class AiFaceController extends AppIBaseController {

    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/face";

    @Resource
    private AiFaceService aiFaceService;

    @ApiOperation("人脸采集接口")
    @RequestMapping(value = "create")
    public void create(
            @ApiParam("人员姓名") @RequestParam String personName,
            @ApiParam("人员hash，唯一标志") @RequestParam String userHash,
            @ApiParam("人脸照片路径") @RequestParam String url) throws TencentCloudSDKException {
        aiFaceService.create(personName, userHash, url);
    }


}
