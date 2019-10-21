package com.fosung.ksh.duty.controller;

import com.fosung.framework.common.dto.UtilDTO;
import com.fosung.framework.common.dto.support.DTOCallbackHandler;
import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.common.util.UtilEasyPoi;
import com.fosung.ksh.common.util.UtilPage;
import com.fosung.ksh.duty.entity.DutyPeople;
import com.fosung.ksh.duty.entity.DutySchedule;
import com.fosung.ksh.duty.service.DutyPeopleService;
import com.fosung.ksh.duty.service.DutyScheduleService;
import com.fosung.ksh.duty.vo.DutyScheduleVo;
import com.fosung.ksh.sys.client.SysAreaClient;
import com.fosung.ksh.sys.dto.SysArea;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.assertj.core.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: fosung-ksh
 * @description: 人员排班
 * @author: LZ
 * @create: 2019-05-14 15:22
 **/

@Api(description = "人员排班设定", tags = {"3、人员排班设定"})
@RestController
@RequestMapping(value = DutyScheduleController.BASE_PATH)
public class DutyScheduleController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/duty-schedule";

    @Autowired
    private DutyScheduleService dutyScheduleService;

    @Autowired
    private DutyPeopleService dutyPeopleService;

    @Autowired
    private SysAreaClient sysAreaClient;

    /**
     * 上传值班excel表格，然后存入数据库
     */
    @ApiOperation(value = "人员排班导入")
    @PostMapping("/importTemplate")
    public ResponseEntity importTemplate(@RequestParam(value = "file") MultipartFile file,
                                         @RequestParam("areaId") Long areaId) throws Exception {

        List<DutySchedule> dutySchedules = UtilEasyPoi.importExcel(file, 1, 1, DutySchedule.class);
        try {
            dutyScheduleService.importTemplate(dutySchedules, areaId);
        } catch (ConstraintViolationException e) {
            String erroMsg = "";
            for (ConstraintViolation item : e.getConstraintViolations()) {
                if (erroMsg.equals("")) {
                    erroMsg = item.getMessageTemplate();
                } else {
                    erroMsg = erroMsg + "," + item.getMessageTemplate().replace("请填写", "");
                }

            }
            throw new AppException(erroMsg + ";");
        }

        return Result.success();

    }

    /**
     * 根据cityCode查询人员排班详情
     */
    @ApiOperation(value = "根据村进行分组查看")
    @PostMapping("queryGroupSchedul")
    public ResponseEntity<Page<Map<String, Object>>> queryGroupSchedul(@ApiParam("行政编码") @RequestParam("areaId") Long areaId,
                                                                       @ApiParam(value = "页码", defaultValue = "0")
                                                                       @RequestParam(required = false, value = "pagenum",
                                                                               defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
                                                                       @ApiParam(value = "每页显示条数", defaultValue = "0")
                                                                       @RequestParam(required = false, value = "pagesize",
                                                                               defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) {

        List<DutyScheduleVo> dutySchedules = dutyScheduleService.queryGroupSchedul(areaId);
        dutySchedules.forEach(dutySchedule -> {
            String cityCode1 = dutySchedule.getCityCode();
            if (UtilString.isNotEmpty(cityCode1)) {
                SysArea areaInfo = sysAreaClient.getAreaInfo(cityCode1);
                dutySchedule.setTownCityName(areaInfo.getParentName());
            }
        });

        Page<DutyScheduleVo> page = UtilPage.page(dutySchedules, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 根据cityCode查询人员排班详情
     */
    @ApiOperation(value = "人员排班查看")
    @PostMapping("query")
    public ResponseEntity<Page<Map<String, Object>>> query(@ApiParam("行政编码") @RequestParam("areaId") Long areaId,
                                                           @ApiParam(value = "页码", defaultValue = "0")
                                                           @RequestParam(required = false, value = "pagenum",
                                                                   defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
                                                           @ApiParam(value = "每页显示条数", defaultValue = "0")
                                                           @RequestParam(required = false, value = "pagesize",
                                                                   defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) {
        Map<String, Object> searchMap = Maps.newHashMap("villageId", areaId);
        List<DutySchedule> dutySchedules = dutyScheduleService.queryAll(searchMap);
        dutySchedules.forEach(dutySchedule -> {
            DutyPeople dutyPeople = dutyPeopleService.get(dutySchedule.getDutyPeopleId());
            dutySchedule.setDutyPeopleName(dutyPeople.getPeopleName());
            dutySchedule.setPhoneNum(dutyPeople.getPhoneNum());
            dutySchedule.setIdCard(dutyPeople.getIdCard());
            SysArea areaInfo = sysAreaClient.getAreaInfo(dutySchedule.getVillageId());
            dutySchedule.setCityName(areaInfo.getAreaName());
        });
        Page<DutySchedule> page = UtilPage.page(dutySchedules, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 根据cityCode查询人员排班详情
     */
    @ApiOperation(value = "人员排班导入")
    @PostMapping("get")
    public ResponseEntity<List<DutySchedule>> get(@RequestParam("cityCode") String cityCode) {
        Map<String, Object> searchMap = Maps.newHashMap("cityCode", cityCode);
        List<DutySchedule> dutySchedules = dutyScheduleService.queryAll(searchMap);
        return Result.success(dutySchedules);
    }

    /**
     * 根据cityCode删除该村人员排班
     */
    @ApiOperation(value = "人员排班导入")
    @PostMapping("delete")
    public ResponseEntity delete(@RequestParam("areaId") Long areaId) {
        Map<String, Object> searchMap = Maps.newHashMap("villageId", areaId);
        List<DutySchedule> dutySchedules = dutyScheduleService.queryAll(searchMap);
        List<Long> collect = dutySchedules.stream().map(DutySchedule::getId).collect(Collectors.toList());
        dutyScheduleService.delete(collect);
        return Result.success(dutySchedules);
    }
}
