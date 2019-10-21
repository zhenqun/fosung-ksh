package com.fosung.ksh.duty.service.impl;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.duty.dao.DutyRecordDao;
import com.fosung.ksh.duty.entity.DutyPeople;
import com.fosung.ksh.duty.entity.DutyRecord;
import com.fosung.ksh.duty.service.DutyPeopleService;
import com.fosung.ksh.duty.service.DutyRecordService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DutyRecordServiceImpl extends AppJPABaseDataServiceImpl<DutyRecord, DutyRecordDao>
        implements DutyRecordService {


    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("alarmId","alarmId:EQ");
            put("dutyStartSignTime","dutySignTime:GTEDATE");
            put("dutyEndSignTime","dutySignTime:LTEDATE");
            put("humanId","humanId:LLIKE");
            put("dutyPeopleId","dutyPeopleId:EQ");
            put("dutyPeopleIds","dutyPeopleId:IN");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }


    /**
     * 根据身份证号获取人员数据
     * @param idCard
     * @return
     */
    public DutyRecord getByAlarmId(String alarmId,String humanId){
        Map<String,Object> searchParam = Maps.newHashMap("alarmId",alarmId);
        searchParam.put("humanId",humanId);
        List<DutyRecord> list = queryAll(searchParam);
        if(UtilCollection.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }


    /**
     * 获取上次同步时间
     * @return
     */
    public Date queryMaxTime(){
        return this.entityDao.queryMaxTime();
    }

}
