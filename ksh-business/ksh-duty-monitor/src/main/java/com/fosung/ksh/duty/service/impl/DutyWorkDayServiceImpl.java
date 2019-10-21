package com.fosung.ksh.duty.service.impl;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.common.util.UtilDateTime;
import com.fosung.ksh.duty.config.constant.WeekType;
import com.fosung.ksh.duty.dao.DutyWorkDayDao;
import com.fosung.ksh.duty.entity.DutyWorkDay;
import com.fosung.ksh.duty.service.DutyShiftService;
import com.fosung.ksh.duty.service.DutyWorkDayService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DutyWorkDayServiceImpl extends AppJPABaseDataServiceImpl<DutyWorkDay, DutyWorkDayDao>
        implements DutyWorkDayService {


    /**
     * 班次设置服务
     */
    @Autowired
    private DutyShiftService dutyShiftService;
    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("shiftId", "shiftId:EQ");
            put("workDay", "workDay:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }


    @Override
    public void preUpdateHandler(DutyWorkDay entity, Collection<String> updateFields) {
        updateFields.clear();
        updateFields.add("isEnable");
        super.preUpdateHandler(entity, updateFields);
    }


    /**
     * 批量保存特殊日期
     *
     * @param shiftId
     */
    public void saveList(List<DutyWorkDay> workDayList, Long shiftId) {
        this.entityDao.deleteAllByShiftId(shiftId);
        workDayList.stream().forEach(workDay -> {
            workDay.setId(null);
            workDay.setShiftId(shiftId);
        });
        this.saveBatch(workDayList);
    }


    /**
     * 获取今日是否为值班时间
     *
     * @param shiftId
     * @param date
     * @return
     */
    public boolean onDutyWorkDay(Long shiftId, Date date) {
        int weekDay = UtilDateTime.getWeekDays(date);
        WeekType weekType = null;
        switch (weekDay) {
            case 0:
                weekType = WeekType.SUN;
            case 1:
                weekType = WeekType.MON;
            case 2:
                weekType = WeekType.TUE;
            case 3:
                weekType = WeekType.WED;
            case 4:
                weekType = WeekType.THU;
            case 5:
                weekType = WeekType.FRI;
            case 6:
                weekType = WeekType.SAT;
        }

        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("shiftId", shiftId);
        searchParam.put("workDay", weekType);
        List<DutyWorkDay> dutyWorkDays = queryAll(searchParam);
        return UtilCollection.isNotEmpty(dutyWorkDays);

    }

//    /**
//     * 设置将添加的工作日
//     *
//     * @return
//     */
//    @Transactional
//    @Override
//    public List<DutyWorkDay> setDutyWorkDayList(String cityCode) {
//        ArrayList<DutyWorkDay> dutyWorkDays = Lists.newArrayList();
//        List<WeekType> enumList = UtilEnum.getEnumList(WeekType.class);
//        enumList.forEach(WeekType -> {
//            DutyWorkDay dutyWorkDay = new DutyWorkDay();
//            dutyWorkDay.setIsEnable(true);
//            dutyWorkDay.setWorkDayName(WeekType.getRemark());
//            // 如果班次没有设置过，添加班次的默认设置
////            DutyShift dutyShift = dutyShiftService.getDutyShiftByCitycode(cityCode);
//            DutyShift dutyShift = new DutyShift();
//            if (null == dutyShift) {
//                dutyShift = dutyShiftService.save(dutyShiftService.setDutyShift(cityCode));
//            }
//            dutyWorkDay.setShiftId(dutyShift.getId());
//            dutyWorkDay.setWorkDay(WeekType);
//            save(dutyWorkDay);
//            dutyWorkDays.add(dutyWorkDay);
//        });
//        return dutyWorkDays;
//    }

}
