package com.fosung.ksh.duty.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilDate;
import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.common.constant.DateTrunc;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.common.util.UtilDateTime;
import com.fosung.ksh.common.util.UtilEasyPoi;
import com.fosung.ksh.common.util.UtilPage;
import com.fosung.ksh.duty.dto.DutyArea;
import com.fosung.ksh.duty.entity.DutyPeople;
import com.fosung.ksh.duty.entity.DutyRecord;
import com.fosung.ksh.duty.entity.DutySchedule;
import com.fosung.ksh.duty.entity.DutyVillageRecord;
import com.fosung.ksh.duty.service.DutyPeopleService;
import com.fosung.ksh.duty.service.DutyRecordService;
import com.fosung.ksh.duty.service.DutyScheduleService;
import com.fosung.ksh.duty.service.DutyVillageRecordService;
import com.fosung.ksh.sys.client.SysAreaClient;
import com.fosung.ksh.sys.dto.SysArea;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangyh
 */
@Api(description = "值班人员设定", tags = {"3、值班人员设定"})
@RestController
@RequestMapping(value = DutyVillageRecordController.BASE_PATH)
public class DutyVillageRecordController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/duty-village-record";

    @Autowired
    private SysAreaClient areaClient;

    @Autowired
    private DutyVillageRecordService villageRecordService;
    @Autowired
    private DutyRecordService dutyRecordService;
    /**
     * 值班服务
     */
    @Autowired
    private DutyScheduleService dutyScheduleService;
    @Autowired
    private DutyPeopleService dutyPeopleService;

    /**
     * 记录分页查询
     *
     * @param pageNum  分页号，由0开始
     * @param pageSize 每页记录数，默认为10
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询人员签到详情")
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public ResponseEntity<Page<DutyVillageRecord>> query(
            @ApiParam("行政区域编码") @RequestParam Long areaId,
            @ApiParam("是否在线，0：离线 1：在线") @RequestParam(required = false) Boolean isSign,
            @ApiParam("签到日期") @RequestParam(name = "signTime", required = false) String signTime,
            @ApiParam("是否需要月度排名") @RequestParam(name = "isNeedMonthRank", defaultValue = "false") Boolean isNeedMonthRank,
            @ApiParam("是否需要当日值班人和所有签到人") @RequestParam(name = "isNeedSignAndSchedulePeople", defaultValue = "false") Boolean isNeedSignAndSchedulePeople,
            @ApiParam("是否需要值班时长") @RequestParam(name = "isNeedOnSignTime", defaultValue = "false") Boolean isNeedOnSignTime,
            @ApiParam(value = "页码", defaultValue = "0")
            @RequestParam(required = false, value = "pagenum",
                    defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @ApiParam(value = "每页显示条数", defaultValue = "0")
            @RequestParam(required = false, value = "pagesize",
                    defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize
    ) {
        if (UtilString.isEmpty(signTime)) {
            signTime = UtilDate.getCurrentDateFormatStr(AppProperties.DATE_PATTERN);
        }
        Page<DutyVillageRecord> page = villageRecordService.queryRecordList(areaId, isSign, signTime, pageNum, pageSize);
        // 获取所有签到人和今日值班人
        getSignAndSchedulePeople(page, isNeedSignAndSchedulePeople, isNeedOnSignTime, signTime);


        if (isNeedMonthRank) {
            List<DutyArea> list = Lists.newArrayList();
            try {
                list = villageRecordService.queryChildrenRateList(DateTrunc.MONTH, areaId, new SimpleDateFormat("yyyy-MM").parse(signTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (UtilCollection.isNotEmpty(list)) {
                Map<Long, Integer> collect = list.stream().collect(Collectors.toMap(DutyArea::getAreaId, DutyArea::getRanking));

                page.forEach(dutyVillageRecord -> {
                    Long villageId = dutyVillageRecord.getVillageId();
                    dutyVillageRecord.setMonthRank(collect.get(villageId));
                });
            }

        }
        return Result.success(page);
    }

    /**
     * 导出功能
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "导出人员签到详情")
    @RequestMapping(value = "importRecord")
    public ResponseEntity importRecord(
            @ApiParam("行政区域编码") @RequestParam Long areaId,
            @ApiParam("是否在线，0：离线 1：在线") @RequestParam(required = false) Boolean isSign,
            @ApiParam("签到日期") @RequestParam(name = "signTime", required = false) String signTime,
            @ApiParam("是否需要月度排名") @RequestParam(name = "isNeedMonthRank", defaultValue = "true") Boolean isNeedMonthRank,
            @ApiParam("是否需要当日值班人和所有签到人") @RequestParam(name = "isNeedSignAndSchedulePeople", defaultValue = "true") Boolean isNeedSignAndSchedulePeople,
            @ApiParam("是否需要值班时长") @RequestParam(name = "isNeedOnSignTime", defaultValue = "true") Boolean isNeedOnSignTime,
            @ApiParam(value = "页码", defaultValue = "0")
            @RequestParam(required = false, value = "pagenum",
                    defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @ApiParam(value = "每页显示条数", defaultValue = "1500")
            @RequestParam(required = false, value = "pagesize",
                    defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize
    ) {

        if (UtilString.isEmpty(signTime)) {
            signTime = UtilDate.getCurrentDateFormatStr(AppProperties.DATE_PATTERN);
        }

        List<SysArea> areas = areaClient.queryBranchList(areaId);
        List<DutyVillageRecord> content = Lists.newArrayList();
        if (UtilCollection.isNotEmpty(areas)) {
            String finalSignTime = signTime;
            areas.forEach(SysArea -> {
                Page<DutyVillageRecord> page = villageRecordService.queryRecordList(SysArea.getAreaId(), isSign, finalSignTime, pageNum, pageSize);
                // 获取所有签到人和今日值班人
                getSignAndSchedulePeople(page, isNeedSignAndSchedulePeople, isNeedOnSignTime, finalSignTime);
                if (isNeedMonthRank) {
                    List<DutyArea> list = Lists.newArrayList();
                    try {
                        list = villageRecordService.queryChildrenRateList(DateTrunc.MONTH, areaId, new SimpleDateFormat("yyyy-MM").parse(finalSignTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (UtilCollection.isNotEmpty(list)) {
                        Map<Long, Integer> collect = list.stream().collect(Collectors.toMap(DutyArea::getAreaId, DutyArea::getRanking));

                        page.forEach(dutyVillageRecord -> {
                            Long villageId = dutyVillageRecord.getVillageId();
                            dutyVillageRecord.setMonthRank(collect.get(villageId));
                        });
                    }

                }
                content.addAll(page.getContent());
            });

        }

        UtilEasyPoi.exportExcel(content, "考勤记录", "考勤记录", DutyVillageRecord.class, "文件名称.xls", getHttpServletResponse());

        return Result.success();
    }


    /**
     * 记录分页查询
     *
     * @param pageNum  分页号，由0开始
     * @param pageSize 每页记录数，默认为10
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "值班记录查询")
    @RequestMapping(value = "record-list", method = RequestMethod.POST)
    public ResponseEntity<Page<DutyVillageRecord>> queryRecordList(
            @ApiParam("行政区域编码") @RequestParam Long areaId,
            @ApiParam("年度") @RequestParam String year,
            @ApiParam("周期") @RequestParam(required = false) String quarter,
            @ApiParam("类型,year 年度，month 月度，quarter 季度") @RequestParam String type,
            @ApiParam(value = "页码", defaultValue = "0")
            @RequestParam(required = false, value = "pagenum",
                    defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @ApiParam(value = "每页显示条数", defaultValue = "0")
            @RequestParam(required = false, value = "pagesize",
                    defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize
    ) {

        Date time = null;
        Date start = null;
        Date end = null;

        if ("month".equals(type)) {
            time = UtilDate.parse(year + "/" + quarter + "/01");
            start = UtilDateTime.getMonthStartTime(time);
            end = UtilDateTime.getMonthEndTime(time);
        } else if ("quarter".equals(type)) {
            switch (Integer.parseInt(quarter)) {
                case 1:
                    time = UtilDate.parse(year + "/01/01");
                    break; //可选
                case 2:
                    time = UtilDate.parse(year + "/04/01");
                    break; //可选
                case 3:
                    time = UtilDate.parse(year + "/07/01");
                    break; //可选
                case 4:
                    time = UtilDate.parse(year + "/10/01");
                    break; //可选

            }
            start = UtilDateTime.getQuarterStartTime(time);
            end = UtilDateTime.getQuarterEndTime(time);
        } else if ("year".equals(type)) {
            time = UtilDate.parse(year + "/01/01");
            start = UtilDateTime.getYearStartTime(time);
            end = UtilDateTime.getYearEndTime(time);
        }


        Page<DutyVillageRecord> page = villageRecordService.queryRecordList(areaId,
                true,
                UtilDate.getDateFormatStr(start, AppProperties.DATE_PATTERN),
                UtilDate.getDateFormatStr(end, AppProperties.DATE_PATTERN),
                pageNum,
                pageSize);
        return Result.success(page);
    }


    @ApiOperation(value = "获取日度人员签到率")
    @RequestMapping(value = "/day-rate", method = RequestMethod.POST)
    public ResponseEntity<DutyArea> dayRate(
            @ApiParam("查询时间，格式yyyy-MM-dd") @DateTimeFormat(pattern = AppProperties.DATE_PATTERN) @RequestParam(required = false) Date signTime,
            @ApiParam("行政区域代码") @RequestParam Long areaId) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        signTime = signTime == null ? new Date() : signTime;
        return Result.success(villageRecordService.countSelfRate(DateTrunc.DAY, areaId, new Date()));
    }


    @ApiOperation(value = "获取周人员签到率")
    @RequestMapping(value = "/week-rate", method = RequestMethod.POST)
    public ResponseEntity<DutyArea> weekRate(
            @ApiParam("查询时间，格式yyyy-MM-dd")
            @DateTimeFormat(pattern = AppProperties.DATE_PATTERN)
            @RequestParam(required = false) Date signTime,
            @ApiParam("行政区域代码") @RequestParam Long areaId) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        signTime = signTime == null ? new Date() : signTime;
        return Result.success(villageRecordService.countSelfRate(DateTrunc.WEEK, areaId, new Date()));
    }

    @ApiOperation(value = "获取月人员签到率")
    @RequestMapping(value = "/month-rate", method = RequestMethod.POST)
    public ResponseEntity<DutyArea> monthRate(
            @ApiParam("年度") @RequestParam(required = false) String year,
            @ApiParam("月度") @RequestParam(required = false) String month,
            @ApiParam("行政区域代码") @RequestParam Long areaId) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Date signTime = null;
        if (UtilString.isEmpty(year)) {
            signTime = new Date();
        } else {
            signTime = UtilDateTime.parse(year + "-" + month + "-01");
        }

        return Result.success(villageRecordService.countSelfRate(DateTrunc.MONTH, areaId, new Date()));
    }


    @ApiOperation(value = "获取年人员签到率")
    @RequestMapping(value = "/year-rate", method = RequestMethod.POST)
    public ResponseEntity<DutyVillageRecord> YearRate(
            @ApiParam("年度") @RequestParam(required = false) String year,
            @ApiParam("行政区域代码") @RequestParam Long areaId) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Date signTime = null;
        if (UtilString.isEmpty(year)) {
            signTime = new Date();
        } else {
            signTime = UtilDateTime.parse(year + "-01-01");
        }

        return Result.success(villageRecordService.countSelfRate(DateTrunc.YEAR, areaId, new Date()));
    }


    @ApiOperation(value = "获取签到日排名")
    @RequestMapping(value = "/ranking-day-list", method = RequestMethod.POST)
    public ResponseEntity<Page<DutyArea>> rankingDayList(
            @ApiParam("行政区域代码") @RequestParam Long areaId,
            @ApiParam("查询时间，格式yyyy-MM-dd") @DateTimeFormat(pattern = AppProperties.DATE_PATTERN) @RequestParam(required = false) Date signTime,
            @ApiParam("是否需要值班人数量") @RequestParam(required = false,defaultValue = "false") Boolean isNeedDutyPeopleNumber,
            @ApiParam(value = "页码", defaultValue = "0")
            @RequestParam(required = false, value = "pagenum",
                    defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @ApiParam(value = "每页显示条数", defaultValue = "0")
            @RequestParam(required = false, value = "pagesize",
                    defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        signTime = signTime == null ? new Date() : signTime;
        List<DutyArea> list = villageRecordService.queryChildrenRateList(DateTrunc.DAY, areaId, signTime);
        if (isNeedDutyPeopleNumber) {
            list.forEach(dutyArea -> {
                List<DutySchedule> dutySchedules = dutyScheduleService.queryAll(Maps.newHashMap("villageId", dutyArea.getAreaId()));
                dutyArea.setDutySchedulePeopeleNumber(dutySchedules.size());
            });
        }
        Page<DutyArea> page = UtilPage.page(list, pageNum, pageSize);
        return Result.success(page);
    }


    @ApiOperation(value = "获取签到周排名")
    @RequestMapping(value = "/ranking-week-list", method = RequestMethod.POST)
    public ResponseEntity<Page<Map<String, Object>>> rankingWeekList(
            @ApiParam("行政区域代码") @RequestParam Long areaId,
            @ApiParam("查询时间，格式yyyy-MM-dd") @DateTimeFormat(pattern = AppProperties.DATE_PATTERN) @RequestParam(required = false) Date signTime,
            @ApiParam(value = "页码", defaultValue = "0")
            @RequestParam(required = false, value = "pagenum",
                    defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @ApiParam(value = "每页显示条数", defaultValue = "0")
            @RequestParam(required = false, value = "pagesize",
                    defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        signTime = signTime == null ? new Date() : signTime;
        List<DutyArea> list = villageRecordService.queryChildrenRateList(DateTrunc.WEEK, areaId, signTime);
        Page<DutyArea> page = UtilPage.page(list, pageNum, pageSize);
        return Result.success(page);
    }


    @ApiOperation(value = "获取签到月排名")
    @RequestMapping(value = "/ranking-month-list", method = RequestMethod.POST)
    public ResponseEntity<Page<Map<String, Object>>> rankingMonthList(
            @ApiParam("行政区域代码") @RequestParam Long areaId,
            @ApiParam("查询时间，格式yyyy-MM") @DateTimeFormat(pattern = "yyyy-MM") @RequestParam(required = false) Date signTime,
            @ApiParam(value = "页码", defaultValue = "0")
            @RequestParam(required = false, value = "pagenum",
                    defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @ApiParam(value = "每页显示条数", defaultValue = "0")
            @RequestParam(required = false, value = "pagesize",
                    defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (signTime == null) {
            signTime = new Date();
        }

        List<DutyArea> list = villageRecordService.queryChildrenRateList(DateTrunc.MONTH, areaId, signTime);
        Page<DutyArea> page = UtilPage.page(list, pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation(value = "获取签到年度排名")
    @RequestMapping(value = "/ranking-year-list", method = RequestMethod.POST)
    public ResponseEntity<Page<Map<String, Object>>> rankingYearList(
            @ApiParam("行政区域代码") @RequestParam Long areaId,
            @ApiParam("查询时间，格式yyyy") @DateTimeFormat(pattern = "yyyy") @RequestParam(required = false) Date signTime,
            @ApiParam(value = "页码", defaultValue = "0")
            @RequestParam(required = false, value = "pagenum",
                    defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @ApiParam(value = "每页显示条数", defaultValue = "0")
            @RequestParam(required = false, value = "pagesize",
                    defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (signTime == null) {
            signTime = new Date();
        }

        List<DutyArea> list = villageRecordService.queryChildrenRateList(DateTrunc.YEAR, areaId, signTime);
        Page<DutyArea> page = UtilPage.page(list, pageNum, pageSize);
        return Result.success(page);
    }


    /**
     * 根据Id，获取本日第一个签到人信息
     *
     * @param areaId
     * @return
     */
    @ApiOperation(value = "根据村名称，获取今日值班数据")
    @RequestMapping(value = "/get-city-code", method = RequestMethod.POST)
    public ResponseEntity<DutyVillageRecord> getByareaId(@RequestParam Long areaId) {
        return Result.success(villageRecordService.getTodayRecord(areaId));
    }

    public void getSignAndSchedulePeople(Page<DutyVillageRecord> page, Boolean isNeedSignAndSchedulePeople, Boolean isNeedOnSignTime, String signTime) {
        if (isNeedSignAndSchedulePeople) {
            // 主要分为两步1. 获取今日的值班人员 2. 获取今日所有的签到人员
            page.forEach(dutyVillageRecord -> {
                // 设置在岗时长
                if (isNeedOnSignTime) {
                    dutyVillageRecord.setOnSignTime(onSingTime(dutyVillageRecord));
                }
                // 首先查询该村下所有的值班人员
                List<DutySchedule> dutySchedules = dutyScheduleService.queryAll(Maps.newHashMap("villageId", dutyVillageRecord.getVillageId()));
                // 根据日期进行筛选今天的值班人员
                int value = LocalDate.parse(signTime).getDayOfWeek().getValue();
                // 过滤出今日的所有的值班人姓名
                dutySchedules.forEach(DutySchedule -> {
                    DutyPeople dutyPeople = dutyPeopleService.get(DutySchedule.getDutyPeopleId());
                    DutySchedule.setDutyPeopleName(dutyPeople.getPeopleName());
                });
                List<String> schedulePeople = dutySchedules.stream().filter(dutySchedule -> UtilString.equals(dutySchedule.getCycle(), value + "")).map(DutySchedule::getDutyPeopleName).collect(Collectors.toList());
                dutyVillageRecord.setSchedulePeople(schedulePeople);

                // 获取该村下面所人员的id
                List<DutyPeople> dutyPeoples = dutyPeopleService.queryAll(Maps.newHashMap("villageId", dutyVillageRecord.getVillageId()));
                List<Long> dutyPeopleIds = dutyPeoples.stream().map(DutyPeople::getId).collect(Collectors.toList());
                // 根据人员id获取今日签到数据
                Map<String, Object> searchMap = Maps.newHashMap("dutyPeopleIds", dutyPeopleIds);
                Date startTime = UtilDate.startTimeInDay(signTime);
                Date endTime = UtilDate.endTimeInDay(signTime);
                searchMap.put("dutyStartSignTime", startTime);
                searchMap.put("dutyEndSignTime", endTime);
                List<DutyRecord> dutyRecords = dutyRecordService.queryAll(searchMap);
                List<String> collect = dutyRecords.stream().map(DutyRecord::getDutyPeopleId).collect(Collectors.toList());
                // 对所有的值班人员进行过滤
                if (UtilCollection.isNotEmpty(dutyRecords)) {
                    List<String> peopleNames = dutyPeoples.stream().filter(dutyPeople -> collect.contains(dutyPeople.getId().toString())).map(DutyPeople::getPeopleName).distinct().collect(Collectors.toList());
                    dutyVillageRecord.setAllSignPeople(peopleNames);
                }

            });

        }

    }

    /**
     * @Description: 获取在岗时长
     * @author LZ
     * @date 2019-07-05 08:57
     */
    public String onSingTime(DutyVillageRecord dutyVillageRecord) {
        if (dutyVillageRecord != null) {

            Date amSignTime = dutyVillageRecord.getAmSignTime();
            if (amSignTime == null) {
                return "0时0分";
            }
            if (dutyVillageRecord.getPmSignOffTime() != null) {
                return getDatePoor(amSignTime, dutyVillageRecord.getPmSignOffTime());
            }
            if (dutyVillageRecord.getPmSignTime() != null) {
                return getDatePoor(amSignTime, dutyVillageRecord.getPmSignTime());
            }
            if (dutyVillageRecord.getAmSignOffTime() != null) {
                return getDatePoor(amSignTime, dutyVillageRecord.getAmSignOffTime());
            }
        }
        return "0时0分";

    }

    /**
     * 计算两个时间段的时长
     *
     * @param endDate
     * @param nowDate
     * @return
     */
    public static String getDatePoor(Date startDate, Date endDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startDate.getTime();
        // 计算差多少天
//        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
//        return day + "天" + hour + "小时" + min + "分钟";
        return hour + "时" + min + "分";
    }
}
