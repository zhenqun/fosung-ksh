package com.fosung.ksh.duty.service.impl;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.duty.dao.DutyShiftDao;
import com.fosung.ksh.duty.entity.DutyShift;
import com.fosung.ksh.duty.entity.DutySpecialDay;
import com.fosung.ksh.duty.entity.DutyWorkDay;
import com.fosung.ksh.duty.service.DutyShiftService;
import com.fosung.ksh.duty.service.DutySpecialDayService;
import com.fosung.ksh.duty.service.DutyWorkDayService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

@Service
public class DutyShiftServiceImpl extends AppJPABaseDataServiceImpl<DutyShift, DutyShiftDao>
        implements DutyShiftService {


    @Autowired
    private DutySpecialDayService dutySpecialDayService;

    @Autowired
    private DutyWorkDayService dutyWorkDayService;
    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("areaId", "areaId:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }


    /**
     * 获取班次详情
     *
     * @param areaid
     * @return
     */
    public DutyShift getShiftInfo(Long areaid) {
        DutyShift dutyShift = getShiftByAreaId(areaid);
        if (dutyShift == null) {
            dutyShift = getDutyShift(areaid);
        } else {
            Map<String, Object> searchParam = Maps.newHashMap();
            searchParam.put("shiftId", dutyShift.getId());
            List<DutySpecialDay> specialDayList = dutySpecialDayService.queryAll(searchParam);
            List<DutyWorkDay> workDayList = dutyWorkDayService.queryAll(searchParam);
            dutyShift.setSpecialDayList(specialDayList);
            dutyShift.setWorkDayList(workDayList);
        }
        return dutyShift;
    }

    /**
     * 验证行政区域在当前时间是否需要值班
     *
     * @param areaId 行政区划ID
     * @param date   时间
     * @return
     */
    public Boolean onDuty(Long areaId, Date date) {
        DutyShift dutyShift = getShiftByAreaId(areaId);
        // 未设置值班班次，则不进行值班
        if (dutyShift == null) {
            return false;
        }

        // 验证今日是否为特殊值班日期
        DutySpecialDay specialDay = dutySpecialDayService.getDayInfo(dutyShift.getId(), date);
        if (specialDay != null) {
            return specialDay.getIsDuty();
        }

        // 验证是否为正常工作日
        return dutyWorkDayService.onDutyWorkDay(dutyShift.getId(), date);
    }


    /**
     * 修改值班设定
     *
     * @param dutyShift
     */
    public void edit(DutyShift dutyShift) {
        if (dutyShift.getId() == null) {
            save(dutyShift);
        } else {
            update(dutyShift, Sets.newHashSet());
        }

        dutySpecialDayService.saveList(dutyShift.getSpecialDayList(), dutyShift.getId());
        dutyWorkDayService.saveList(dutyShift.getWorkDayList(), dutyShift.getId());
    }


    @Override
    public void preSaveHandler(DutyShift entity) {
        Assert.notNull(entity.getAreaId(), "城市编码不能为空");
        Assert.notNull(entity.getShiftName(), "班次名称不能为空");
        Assert.notNull(entity.getMorningStartTime(), "上午值班开始时间不能为空");
        Assert.notNull(entity.getMorningEndTime(), "上午值班结束时间不能为空");
        Assert.notNull(entity.getAfternoonEndTime(), "下午值班开始时间不能为空");
        Assert.notNull(entity.getAfternoonStartTime(), "上午值班结束始时间不能为空");
        super.preSaveHandler(entity);
    }

    /**
     * 根据areaId获取班次设置
     *
     * @param areaId
     * @return
     */
    public DutyShift getShiftByAreaId(Long areaId) {
        HashMap<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("areaId", areaId);
        List<DutyShift> dutyShifts = queryAll(searchMap);
        if (UtilCollection.isNotEmpty(dutyShifts)) {
            return dutyShifts.get(0);
        }
        return null;
    }


    @Override
    public void preUpdateHandler(DutyShift entity, Collection<String> updateFields) {
        updateFields.clear();
        updateFields.add("shiftName");
        updateFields.add("morningStartTime");
        updateFields.add("morningEndTime");
        updateFields.add("afternoonStartTime");
        updateFields.add("afternoonEndTime");
        updateFields.add("excludeHoliday");
        super.preUpdateHandler(entity, updateFields);
    }

    /**
     * 设置班次设置对象
     */
    public DutyShift getDutyShift(Long areaId) {
        DutyShift dutyShift = new DutyShift();
        dutyShift.setAreaId(areaId);
        dutyShift.setShiftName("班次A");
        dutyShift.setMorningStartTime("8:30");
        dutyShift.setMorningEndTime("11:30");
        dutyShift.setAfternoonStartTime("14:00");
        dutyShift.setAfternoonEndTime("17:30");
        return dutyShift;
    }
}
