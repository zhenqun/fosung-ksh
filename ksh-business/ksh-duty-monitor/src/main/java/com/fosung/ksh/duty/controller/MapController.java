package com.fosung.ksh.duty.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.duty.dto.DutyArea;
import com.fosung.ksh.duty.service.MapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wangyihua
 * @date 2019-05-10 18:01
 */
@Slf4j
@Api(description = "首页地图所需数据接口", tags = {"3、地图接口"})
@RestController
@RequestMapping(value = MapController.BASE_PATH)
public class MapController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/maps";

    @Autowired
    private MapService mapService;

    @ApiOperation(value = "获取各个区域数据")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<List<DutyArea>> list(
            @ApiParam("行政区域代码") @RequestParam Long areaId) {
        return Result.success(mapService.queryList(areaId));
    }


}
