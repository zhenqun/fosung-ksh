package com.fosung.ksh.duty.service.impl;

import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.duty.dao.DutySpecialDayDao;
import com.fosung.ksh.duty.entity.DutySpecialDay;
import com.fosung.ksh.duty.service.DutySpecialDayService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 特殊日期设置
 */
@Service
public class DutySpecialDayServiceImpl extends AppJPABaseDataServiceImpl<DutySpecialDay, DutySpecialDayDao>
        implements DutySpecialDayService {


    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("shiftId", "shiftId:EQ");
            put("specialDate", "specialDate:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }


    /**
     * 批量保存特殊日期
     *
     * @param specialDayList
     * @param shiftId
     */
    public void saveList(List<DutySpecialDay> specialDayList, Long shiftId) {
        this.entityDao.deleteAllByShiftId(shiftId);
        specialDayList.stream().forEach(specialDay -> {
            specialDay.setId(null);
            specialDay.setShiftId(shiftId);
        });
        this.saveBatch(specialDayList);
    }

    /**
     * 获取特殊值班日期
     *
     * @param shiftId
     * @param specialDate
     * @return
     */
    public DutySpecialDay getDayInfo(Long shiftId, Date specialDate) {
        return this.entityDao.getDayInfo(shiftId, specialDate);
    }


    @Override
    public void preSaveHandler(DutySpecialDay entity) {
        Assert.notNull(entity.getSpecialDate(), "日期不能不能为空");
        Assert.notNull(entity.getIsDuty(), "是否打卡不能为空");
        Assert.notNull(entity.getShiftId(), "班次不能为空");
        HashMap<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("shiftId", entity.getShiftId());
        searchMap.put("specialDate", entity.getSpecialDate());
        Assert.isTrue(count(searchMap) == 0, "当前特殊日期已经设置！");
        super.preSaveHandler(entity);
    }
}
