package com.fosung.ksh.duty.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.dto.DtoUtil;
import com.fosung.ksh.common.dto.handler.KshDTOCallbackHandler;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.duty.entity.DutyPeople;
import com.fosung.ksh.duty.service.DutyPeopleService;
import com.fosung.ksh.sys.client.SysAreaClient;
import com.fosung.ksh.sys.dto.SysArea;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author wangyh
 */
@Api(description = "值班人员设定", tags = {"3、值班人员设定"})
@RestController
@RequestMapping(value = DutyPeopleController.BASE_PATH)
public class DutyPeopleController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/duty-people";
    /**
     * 行政区域
     */
    @Autowired
    private SysAreaClient sysAreaClient;

    @Autowired
    private DutyPeopleService peopleService;


    @ApiOperation(value = "根据人员Id，获取人员信息")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity<DutyPeople> get(@RequestParam Long id) {
        return Result.success(peopleService.get(id));
    }


    @ApiOperation(value = "人脸采集")
    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    public ResponseEntity<DutyPeople> create(@RequestBody DutyPeople dutyPeople) {
        return Result.success(peopleService.collect(dutyPeople));
    }

    @ApiOperation(value = "人脸删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity delete(@RequestBody DutyPeople dutyPeople) {
        peopleService.delete(dutyPeople);
        return Result.success();
    }

    @ApiOperation(value = "查询用户人脸采集列表")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseEntity<List<DutyPeople>> query(@ApiParam("行政编码") @RequestParam("areaId") Long areaId,
                                                  @ApiParam("身份证号") @RequestParam(name = "idCard", required = false, defaultValue = "") String idCard,
                                                  @ApiParam("姓名") @RequestParam(required = false, defaultValue = "") String peopleName,
                                                  @ApiParam(value = "页码", defaultValue = "0")
                                                  @RequestParam(required = false, value = "pagenum",
                                                          defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
                                                  @ApiParam(value = "每页显示条数", defaultValue = "0")
                                                  @RequestParam(required = false, value = "pagesize",
                                                          defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) {
        Map<String, Object> searchParam = Maps.newHashMap();
        List<Long> areaIdList = sysAreaClient.queryAreaAllChildId(areaId);
        searchParam.put("villageIdList", areaIdList);
        searchParam.put("peopleName", peopleName);
        searchParam.put("idCardLike", idCard);
        Page<DutyPeople> dutyPeople = peopleService.queryByPage(searchParam, pageNum, pageSize);
        DtoUtil.handler(dutyPeople.getContent(), new KshDTOCallbackHandler<DutyPeople>() {
            @Override
            public void doHandler(DutyPeople dto) {
                Long villageId = dto.getVillageId();
                if (villageId != null) {
                    SysArea areaInfo = sysAreaClient.getAreaInfo(villageId);
                    dto.setCityName(areaInfo.getAreaName());
                    dto.setTownCode(areaInfo.getAreaCode());
                    dto.setTownName(areaInfo.getParentName());
                }
            }

        });
        return Result.success(dutyPeople);
    }
//
//    @ApiOperation(value = "获取用户采集信息")
//    @RequestMapping(value = "/get", method = RequestMethod.POST)
//    public ResponseEntity<SysFace> query(@ApiParam("用户hash") @RequestParam String hash) {
//        return Result.success(sysFaceService.get(hash));
//    }
//
//
//    @ApiOperation(value = "导出人脸采集结果")
//    @RequestMapping(value = "/export", method = RequestMethod.GET)
//    public void export(@ApiParam("要导出的ID集合，通过','进行连接") @RequestParam String ids,
//                       @ApiParam("查询类型") @RequestParam(required = false) Boolean result) throws Exception {
//        String id[] = ids.split(",");
//
//        List<String> idList = Lists.newArrayList();
//        for (String s : id) {
//            idList.add(s);
//        }
//        Map<String, Object> param = Maps.newHashMap();
//        param.put("ids", idList);
//        List<SysFace> sysFaces = sysFaceService.queryAll(param);
//
//        UtilEasyPoi.exportExcel(sysFaces, "人员导出结果", "人员导出结果", SysFace.class, "人员导出结果.xls", getHttpServletResponse());
//    }

}
