package com.fosung.ksh.sys.web.controller;

import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.sys.entity.SysArea;
import com.fosung.ksh.sys.service.SysAreaService;
import com.google.common.collect.Lists;
import com.mzlion.core.utils.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wangyh
 */
@Api(description = "行政区域接口", tags = {"2、行政区划接口"})
@RestController
@RequestMapping(value = SysAreaController.BASE_PATH)
public class SysAreaController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/area";

    @Autowired
    private SysAreaService sysAreaService;

    @ApiOperation(value = "获取行政区域详情")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity<SysArea> getAreaInfo(
            @ApiParam("行政区域ID") @RequestParam Long id) {
        return Result.success(sysAreaService.getAreaInfo(id));
    }

    @ApiOperation(value = "获取行政区域详情")
    @RequestMapping(value = "/get-code", method = RequestMethod.POST)
    public ResponseEntity<SysArea> getAreaInfo(
            @ApiParam("行政区域编码") @RequestParam String areaCode) {
        return Result.success(sysAreaService.getAreaInfo(areaCode));
    }

    @ApiOperation(value = "获取下级行政区域")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<List<Map<String, Object>>> queryAreaList(
            @ApiParam("行政区域ID") @RequestParam Long id,
            @ApiParam("行政区域名称,支持模糊查询") @RequestParam(required = false) String cityName) {

        if (UtilString.isEmpty(cityName)) {
            List<SysArea> list = sysAreaService.queryAreaList(id);
            List<Map<String, Object>> areas = Lists.newArrayList();
            list.stream().forEach(sysArea -> {
                areas.add(BeanUtils.toMapAsValueObject(sysArea));
            });
            return Result.success(areas);
        }

        return Result.success(sysAreaService.queryAreaAllChild(id, cityName));
    }


    @ApiOperation(value = "获取村级行政区域")
    @RequestMapping(value = "/branch", method = RequestMethod.POST)
    public ResponseEntity<List<SysArea>> queryAreaList(@ApiParam("党组织ID") @RequestParam Long id) {
        return Result.success(sysAreaService.queryAreaBranchInfo(id));
    }


    @ApiOperation(value = "获取所有下级行政区域ID")
    @RequestMapping(value = "/all-id-list", method = RequestMethod.POST)
    public ResponseEntity<List<Long>> queryAllChrildrenIdList(@ApiParam("党组织ID") @RequestParam Long id) {
        List<SysArea> areas = sysAreaService.queryAreaAllChild(id);
        List<Long> ids = Lists.newArrayList();
        areas.stream().forEach(area -> {
            ids.add(area.getId());
        });
        return Result.success(ids);
    }

}
