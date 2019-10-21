package com.fosung.ksh.duty.controller;

import com.fosung.framework.web.http.AppIBaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 值班时间查询和设置
 * @author LZ
 * @date 2019-05-10 15:58
*/
@Api(description = "值班时间设定", tags = {"3、值班时间设定"})
@RestController
@RequestMapping(value = DutyDateController.BASE_PATH)
public class DutyDateController extends AppIBaseController {
//    /**
//     * 当前模块跟路径
//     */
    public static final String BASE_PATH = "/duty/date";
//
//    /**
//     * 值班时间设置
//     */
//    @Autowired
//    private DutyDateService dutyDateService;
//
//
//    @ApiOperation(value = "值班时间查询")
//    @RequestMapping(value = "/get", method = RequestMethod.POST)
//    public ResponseEntity<DutyDate> getDutyTime(@ApiParam("镇级行政编码")@RequestParam("cityCode") String cityCode) {
//        DutyDate dutyDaySet = dutyDateService.getDutySet(cityCode);
//        return Result.success(dutyDaySet);
//    }
//
//
//    @ApiOperation(value = "值班时间设置")
//    @RequestMapping(value = "/set", method = RequestMethod.POST)
//    public ResponseEntity<DutyDate> setDutyTime(@RequestBody DutyDate dutyDay) {
//       dutyDateService.setDutySet(dutyDay);
//        return Result.success();
//    }

}
