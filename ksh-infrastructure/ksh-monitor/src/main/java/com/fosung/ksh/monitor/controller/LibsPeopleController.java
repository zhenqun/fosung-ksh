package com.fosung.ksh.monitor.controller;

import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.monitor.dto.PersonInfo;
import com.fosung.ksh.monitor.service.LibsPeopleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@RestController
@RequestMapping(value = LibsPeopleController.BASE_PATH)
public class LibsPeopleController extends AppIBaseController {

    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/libs-people";

    @Resource
    private LibsPeopleService libsPeopleService;

    /**
     * 新增黑名单
     *
     * @param humanName 人员姓名
     * @param picBase64 图片base64编码
     * @param listLibId
     * @return
     */
    @ApiOperation("新增人脸")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<PersonInfo> addPersonInfo(@RequestBody PersonInfo personInfo) {
        log.info("新增人脸信息{}",personInfo.toString());
        return Result.success(libsPeopleService.addPersonInfo(personInfo.getHumanName(),
                personInfo.getPicBase64(),
                personInfo.getListLibId(),
                personInfo.getCredentialsNum()));
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
    public ResponseEntity<PersonInfo> editPersonInfo(@RequestBody PersonInfo personInfo) {
        log.info("修改人脸信息{}",personInfo.toString());
        return Result.success(libsPeopleService.editPersonInfo(
                personInfo.getHumanId(),
                personInfo.getPicBase64(),
                personInfo.getListLibId()));

    }

    /**
     * 根据证件号码，查询人员在人脸库中的信息
     *
     * @param credentialsNum
     * @param listLibIds
     * @return
     */
    @ApiOperation("根据证件号，获取人员在人脸库信息")
    @RequestMapping(value = "get", method = RequestMethod.POST)
    public ResponseEntity<PersonInfo> getPersonInfo(@ApiParam("人员证件号码，现只支持身份证") @RequestParam String credentialsNum,
                                                    @ApiParam("人脸库Id,多个人脸库通过','分割") @RequestParam String listLibIds) {
        if (UtilString.isIdCard(credentialsNum)) {
            return Result.success(libsPeopleService.getPersonInfo(credentialsNum, listLibIds));
        }
        return null;
    }

    /**
     * 查询黑名单信息
     *
     * @param credentialsNum
     * @param listLibIds
     * @return
     */
    @ApiOperation("查询人脸库中的人脸")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<List<PersonInfo>> queryPersonInfo(
            @ApiParam("人脸库Id,多个人脸库通过','分割") @RequestParam String listLibIds) {
        return Result.success(libsPeopleService.queryPersonInfo(listLibIds));
    }

    /**
     * 删除黑名单
     *
     * @param humanId
     * @return
     */
    @ApiOperation("删除人脸")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public void deleteBlackPerson(@ApiParam("人员在人脸库ID") @RequestParam String humanId) {
        libsPeopleService.deleteBlackPerson(humanId);
    }

}
