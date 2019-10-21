package com.fosung.ksh.duty.service.impl;

import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilDate;
import com.fosung.framework.common.util.UtilString;
import com.fosung.ksh.duty.config.constant.WeekType;
import com.fosung.ksh.duty.entity.DutyHoliday;
import com.fosung.ksh.duty.entity.DutyShift;
import com.fosung.ksh.duty.entity.DutySpecialDay;
import com.fosung.ksh.duty.entity.DutyWorkDay;
import com.fosung.ksh.duty.holiday.HolidayResponse;
import com.fosung.ksh.duty.holiday.HolidayService;
import com.fosung.ksh.duty.service.*;

import com.fosung.ksh.duty.vo.DutyDate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class DutyDateServiceImpl implements DutyDateService {
//    /**
//     * 班次设置
//     */
//    @Autowired
//    private DutyShiftService dutyShiftService;
//    /**
//     * 节假日设置服务
//     */
//    @Autowired
//    private DutyHolidayService dutyHolidayService;
//    /**
//     * 特殊日期设置
//     */
//    @Autowired
//    private DutySpecialDayService dutySpecialDayService;
//    /**
//     * 工作日设置
//     */
//    @Autowired
//    private DutyWorkDayService dutyWorkDayService;
//    /**
//     * 获取节假日服务
//     */
//    @Autowired
//    private HolidayService holidayService;
//
//
//    /**
//     * 获取工作日设置详情
//     *
//     * @return
//     */
//    @Override
//    public DutyDate getDutySet(String cityCode) {
//        DutyDate dutyDaySet = new DutyDate();
//        dutyDaySet.setCityCode(cityCode);
//
//        HashMap<String, Object> searchMap = Maps.newHashMap();
//        searchMap.put("cityCode", cityCode);
//        // 获取工作日
//        List<DutyWorkDay> dutyWorkDays = dutyWorkDayService.queryAll(searchMap);
//        if (UtilCollection.sizeIsEmpty(dutyWorkDays)){
//            // 如果为空，默认添加
//            dutyWorkDays = dutyWorkDayService.setDutyWorkDayList(cityCode);
//        }else {
//            dutyWorkDays.forEach(dutyWorkDay -> dutyWorkDay.setWorkDayName(dutyWorkDay.getWorkDay().getRemark()));
//        }
//        dutyDaySet.setDutyWorkDays(dutyWorkDays);
//
//        // 获取班次信息
//        List<DutyShift> dutyShifts = dutyShiftService.queryAll(searchMap);
//        // 如果为空的话，前端设置一个空值的值班时间。
//        DutyShift dutyShift = new DutyShift();
//        if (UtilCollection.isNotEmpty(dutyShifts)) {
//            dutyShift = dutyShifts.get(0);
//        }
//        dutyDaySet.setDutyShift(dutyShift);
//
//
//        // 获取特殊日期
//        List<DutySpecialDay> dutySpecialDays = dutySpecialDayService.queryAll(searchMap);
//        dutyDaySet.setDutySpecialDays(dutySpecialDays);
//        return dutyDaySet;
//    }
//
//
//
//
//    /**
//     * 添加工作日设置
//     *
//     * @param dutyDaySet
//     */
//    @Transactional
//    public void setDutySet(DutyDate dutyDaySet) {
//        String cityCode = dutyDaySet.getCityCode();
//        List<String> updateFile = Lists.newArrayList();
//        // 设置班次和法定节假日是否需要排班
//        DutyShift dutyShift = dutyDaySet.getDutyShift();
//        if (null != dutyShift.getId()) {
////            Field[] fields = DutyShift.class.getFields();
////            updateFile = Arrays.stream(fields).map(Field:: getName).collect(Collectors.toList());
//            dutyShiftService.update(dutyShift, updateFile);
//        } else {
//            dutyShiftService.save(dutyShift);
//        }
//        // 设置特殊日期，首选本镇下全部的日期，然后在添加
//        List<DutySpecialDay> dutySpecialDays = dutyDaySet.getDutySpecialDays();
//        HashMap<String, Object> searchMap = Maps.newHashMap();
//        searchMap.put("cityCode", cityCode);
//        List<DutySpecialDay> deleteSpecialDays = dutySpecialDayService.queryAll(searchMap);
//
//        List<Long> collect = deleteSpecialDays.stream().map(DutySpecialDay::getId).collect(Collectors.toList());
//        dutySpecialDayService.delete(collect);
//        if (UtilCollection.isNotEmpty(dutySpecialDays)) {
//            dutySpecialDays.forEach(dutySpecialDay -> {
//                dutySpecialDay.setId(null);
//            });
//            dutySpecialDayService.saveBatch(dutySpecialDays);
//        }
//        // 设置工作日
//        List<DutyWorkDay> dutyWorkDays = dutyDaySet.getDutyWorkDays();
//        if (UtilCollection.isNotEmpty(dutyWorkDays)){
//            dutyWorkDayService.update(dutyWorkDays, updateFile);
//        }
//        log.info("值班时间设置成功");
//    }
//
//
//    /**
//     * 根据日期判是否需要执勤
//     *
//     * @param dateStr  字符串日期
//     * @param cityCode 行政编码
//     * @return
//     */
//    public Boolean isDuty(String dateStr, String cityCode) {
//
//        Assert.isTrue(UtilString.isNotEmpty(cityCode) , cityCode + "行政编码错误！");
//        // 日期校验
//        Assert.isTrue(UtilDate.isValidDate(dateStr, "yyyy-MM-dd"), "日期格式不符合yyyy-MM-dd格式");
//
//        Date date = null;
//        try {
//            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
//        } catch (ParseException e) {
//            throw new AppException(dateStr + "日期转换错误");
//        }
//
//        // 判断是否是特殊日期
//        HashMap<String, Object> searchMap = Maps.newHashMap();
//        searchMap.put("date", date);
//        searchMap.put("cityCode", cityCode);
//        List<DutySpecialDay> dutySpecialDays = dutySpecialDayService.queryAll(searchMap);
//        if (UtilCollection.isNotEmpty(dutySpecialDays)) {
//            DutySpecialDay dutySpecialDay = dutySpecialDays.get(0);
//            return dutySpecialDay.getIsDuty();
//        }
//
//        // 中间变更的特殊日期进行手动处理，优选级大于第三方接口的查询
//        HashMap<String, Object> changeSearchMap = Maps.newHashMap();
//        searchMap.put("date", date);
//        List<DutyHoliday> dutyHolidays = dutyHolidayService.queryAll(changeSearchMap);
//        if (UtilCollection.isNotEmpty(dutyHolidays)){
//            DutyHoliday dutyHoliday = dutyHolidays.get(0);
//            return dispatchHandle(dutyHoliday.getHolidayType().getIndex(), dateStr, cityCode);
//        }
//
//        // 获取今天是否是节假日
//        String now = new SimpleDateFormat("yyyyMMdd").format(date);
//        HolidayResponse holiday = holidayService.getHoliday(now);
//        return dispatchHandle(holiday.getData(), dateStr, cityCode);
//
//    }
//
//    /**
//     * 不同工作日类型进行派发处理
//     *
//     * @param dataTYpe
//     * @param dateStr
//     * @param cityCode
//     * @return
//     */
//    public Boolean dispatchHandle(Integer dataTYpe, String dateStr, String cityCode) {
//        switch (dataTYpe) {
//            case 0:  // 工作日
//                return getIsDutyOnWork(dateStr, cityCode);
//            case 1: // 法定节假日
//                return getIsDutyOnHoliday(dateStr, cityCode);
//            case 2: // 节假日调休补班
//                return getIsDutyOnHolidayWork(dateStr, cityCode);
//            case 3: // 休息日
//                return getIsDutyOnWork(dateStr, cityCode);
//        }
//        // 如果请求错误，或者未查询到信息，则需要执勤
//        return true;
//    }
//
//    /**
//     * 获取节假日调休补班是否需要执勤
//     *
//     * @return
//     */
//    public Boolean getIsDutyOnHolidayWork(String dateStr, String cityCode) {
//        getIsDutyOnNoHoliday(dateStr, cityCode);
//        // 如果法定节假日休息，则直接返回需要执勤
//        return true;
//    }
//
//    /**
//     * 获取法定节假日是否需要执勤
//     *
//     * @return
//     */
//    public Boolean getIsDutyOnHoliday(String dateStr, String cityCode) {
//        getIsDutyOnNoHoliday(dateStr, cityCode);
//        // 如果法定节假日休息，则直接返回不需要执勤
//        return false;
//    }
//
//    /**
//     * 如果没有数据或者不需要法定节假日自动排休的，则是需要根据周几判断
//     *
//     * @return
//     */
//    public void getIsDutyOnNoHoliday(String dateStr, String cityCode) {
//        HashMap<String, Object> searchMap = Maps.newHashMap();
//        searchMap.put("cityCode", cityCode);
//        List<DutyShift> dutyShifts = dutyShiftService.queryAll(searchMap);
//        // 如果没有数据或者不需要法定节假日自动排休的，则是需要根据周几判断
//        if (UtilCollection.isEmpty(dutyShifts) || !dutyShifts.get(0).getExcludeHoliday()) {
//            getIsDutyOnWork(dateStr, cityCode);
//        }
//    }
//
//    /**
//     * 获取工作日是否需要执勤
//     *
//     * @return
//     */
//    public Boolean getIsDutyOnWork(String dateStr, String cityCode) {
//        // 根据日期判断今天是周几？
//        LocalDate parse = LocalDate.parse(dateStr);
//        DayOfWeek dayOfWeek = parse.getDayOfWeek();
//        WeekType weekType = WeekType.valueOf(UtilString.substring(dayOfWeek.toString(), 0, 3));
//
//        HashMap<String, Object> searchMap = Maps.newHashMap();
//        searchMap.put("cityCode", cityCode);
//        searchMap.put("workDay", weekType);
//
//        List<DutyWorkDay> dutyWorkDays = dutyWorkDayService.queryAll(searchMap);
//        // 如果没有数据直接返回，不需要执勤
//        if (UtilCollection.isEmpty(dutyWorkDays)) {
//            return false;
//        }
//        DutyWorkDay dutyWorkDay = dutyWorkDays.get(0);
//        return dutyWorkDay.getIsEnable();
//    }


}
