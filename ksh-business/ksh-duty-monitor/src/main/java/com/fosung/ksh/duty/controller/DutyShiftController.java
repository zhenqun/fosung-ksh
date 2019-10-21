package com.fosung.ksh.duty.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.duty.entity.DutyShift;
import com.fosung.ksh.duty.service.DutyShiftService;
import com.fosung.ksh.duty.vo.DutyDate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author LZ
 * @Description: 值班时间查询和设置
 * @date 2019-05-10 15:58
 */
@Api(description = "值班时间设定", tags = {"3、值班时间设定"})
@RestController
@RequestMapping(value = DutyShiftController.BASE_PATH)
public class DutyShiftController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/duty-shift";

    /**
     * 值班时间设置
     */
    @Autowired
    private DutyShiftService shiftService;


    @ApiOperation(value = "值班时间查询")
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public ResponseEntity<DutyShift> getDutyTime(@ApiParam("镇级行政编码") @RequestParam Long areaId) {
        DutyShift dutyShift = shiftService.getShiftInfo(areaId);
        dutyShift.getWorkDayList().forEach(dutyWorkDay ->dutyWorkDay.setWorkDayName(dutyWorkDay.getWorkDay().getRemark()));
        return Result.success(dutyShift);
    }


    @ApiOperation(value = "值班时间设置")
    @RequestMapping(value = "/set", method = RequestMethod.POST)
    public ResponseEntity<DutyDate> setDutyTime(@RequestBody DutyShift dutyShift) {
        shiftService.edit(dutyShift);
        return Result.success();
    }

}
