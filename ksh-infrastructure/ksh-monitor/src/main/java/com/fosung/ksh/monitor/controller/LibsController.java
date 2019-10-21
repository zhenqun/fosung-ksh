package com.fosung.ksh.monitor.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.monitor.service.LibsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping(value = LibsController.BASE_PATH)
public class LibsController extends AppIBaseController {

    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/lib";

    @Resource
    private LibsService libsService;

    /**
     * 新增黑名单
     *
     * @param humanName 人员姓名
     * @param picBase64 图片base64编码
     * @param listLibId
     * @return
     */
    @ApiOperation("新建人脸库")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<String> addLibs(@RequestParam String listLibName,
                                          @RequestParam String describe) {
        return Result.success(libsService.addLibs(listLibName, describe));
    }

    /**
     * 修改黑明单信息
     *
     * @param humanId
     * @param picBase64
     * @param listLibId
     * @return
     */
    @ApiOperation("修改人脸")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public void modifyLibs(@RequestParam String listLibId,
                           @RequestParam String listLibName,
                           @RequestParam String describe) {
        libsService.modifyLibs(listLibId, listLibName, describe);
    }


    /**
     * 修改黑明单信息
     *
     * @param humanId
     * @param picBase64
     * @param listLibId
     * @return
     */
    @ApiOperation("删除人脸库")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public void deleteLibs(@RequestParam String listLibId) {
        libsService.deleteLibs(listLibId);
    }

}
