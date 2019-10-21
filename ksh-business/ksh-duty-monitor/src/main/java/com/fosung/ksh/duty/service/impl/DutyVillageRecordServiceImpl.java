package com.fosung.ksh.duty.service.impl;

import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilDate;
import com.fosung.framework.common.util.UtilNumber;
import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.dao.config.mybatis.page.MybatisPageRequest;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.common.constant.DateTrunc;
import com.fosung.ksh.duty.config.constant.DutyType;
import com.fosung.ksh.duty.dao.DutyVillageRecordDao;
import com.fosung.ksh.duty.dto.DutyArea;
import com.fosung.ksh.duty.entity.DutyPeople;
import com.fosung.ksh.duty.entity.DutyVillageRecord;
import com.fosung.ksh.duty.service.DutyPeopleService;
import com.fosung.ksh.duty.service.DutyVillageRecordService;
import com.fosung.ksh.monitor.service.MonitorCameraService;
import com.fosung.ksh.sys.client.SysAreaClient;
import com.fosung.ksh.sys.dto.SysArea;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * todo 该类中的统计结果需进行优化，需要考虑一个村存在多个人员签到的情况
 * todo 需对没有设备的区域进行过滤
 * 值班记录
 */
@Slf4j
@Service
public class DutyVillageRecordServiceImpl
        extends AppJPABaseDataServiceImpl<DutyVillageRecord, DutyVillageRecordDao> implements DutyVillageRecordService {

    @Autowired
    private SysAreaClient areaClient;

    @Autowired
    private DutyPeopleService peopleService;

    @Autowired
    private MonitorCameraService monitorCameraService;


    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("villageId", "villageId:EQ");

        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }


    /**
     * 镇级及以上统计自身签到率，日期为本日
     *
     * @return
     */
    @Override
    public DutyArea countSelfRate(DateTrunc type, Long areaId, Date signTime) {
        List<SysArea> areas = areaClient.queryBranchList(areaId);
        List<Long> idList = areas.stream().map(SysArea::getAreaId).collect(Collectors.toList());

        Integer allNumber = this.entityDao.countRecord(type.name(), signTime, idList, DutyType.DUTY.name(), null);
        Integer signNumber = this.entityDao.countRecord(type.name(), signTime, idList, DutyType.DUTY.name(), true);
//        SysArea sysArea = areaClient.getAreaInfo(areaId);
        DutyArea areaCamera = new DutyArea();

        areaCamera.setSignNumber(signNumber);
        areaCamera.setNotSignNumber(allNumber - signNumber);

        return areaCamera;
    }


    /**
     * 计算签到率
     *
     * @param type     类型， 年月周日
     * @param areaId
     * @param signTime
     * @return
     */
    @Override
    public List<DutyArea> queryChildrenRateList(DateTrunc type, Long areaId, Date signTime) {

        List<DutyArea> dutyArealist = Lists.newArrayList();

        List<SysArea> areaList = areaClient.queryAreaList(areaId, "");
        for (SysArea sysArea : areaList) {
            DutyArea dutyArea = new DutyArea(sysArea);
            // 设置下属村数量
            dutyArea.setLeaveNum(0);
            DutyArea recordDutyArea = countSelfRate(type, sysArea.getAreaId(), signTime);
            dutyArea.setSignNumber(recordDutyArea.getSignNumber());
            dutyArea.setNotSignNumber(recordDutyArea.getNotSignNumber());
            if (recordDutyArea != null && recordDutyArea.getAllSignNumber() != 0) {
                dutyArealist.add(dutyArea);
            }
        }

        dutyArealist = dutyArealist.stream()
                .sorted(Comparator.comparingDouble(dutyArea -> (0 - UtilNumber.createDouble(dutyArea.getSignRate()))))
                .collect(Collectors.toList());

        for (int i = 0; i < dutyArealist.size(); i++) {
            dutyArealist.get(i).setRanking(i + 1);
        }
        return dutyArealist;
    }


    /**
     * 根据传入日期，获取签到数据
     *
     * @return
     */
    @Override
    public Page<DutyVillageRecord> queryRecordList(Long areaId, Boolean isSign, String signTime, int pageNum, int pageSize) {
        return queryRecordList(areaId, isSign, signTime, signTime, pageNum, pageSize);
    }


    /**
     * 根据开始时间结束时间，获取当前时间段内签到数据
     *
     * @return
     */
    @Override
    public Page<DutyVillageRecord> queryRecordList(Long areaId, Boolean isSign, String startTime, String endTime, int pageNum, int pageSize) {
        Date startDate = UtilDate.parse(startTime);
        Date endDate = UtilDate.parse(endTime);
        List<SysArea> areas = areaClient.queryBranchList(areaId);
        Map<Long, SysArea> areaMap = areas.stream().collect(Collectors.toMap(SysArea::getAreaId, sysArea -> sysArea));
        List<Long> idList = areas.stream().map(SysArea::getAreaId).collect(Collectors.toList());
        Page<DutyVillageRecord> page = this.entityDao.queryRecordList(idList, startDate, endDate, isSign, MybatisPageRequest.of(pageNum, pageSize));
        for (DutyVillageRecord dutyVillageRecord : page.getContent()) {

            SysArea sysArea = areaMap.get(dutyVillageRecord.getVillageId());
            dutyVillageRecord.setCityName(sysArea.getAreaName());

            SysArea townArea = areaClient.getAreaInfo(sysArea.getParentId());
            dutyVillageRecord.setTownName(townArea.getAreaName());

            if (UtilString.isNotEmpty(dutyVillageRecord.getDutyPeopleId())) {
                DutyPeople people = peopleService.get(Long.parseLong(dutyVillageRecord.getDutyPeopleId()));
                dutyVillageRecord.setPeopleName(people.getPeopleName());
            }
            if (UtilString.isNotEmpty(dutyVillageRecord.getDutyPmPeopleId())) {
                DutyPeople people = peopleService.get(Long.parseLong(dutyVillageRecord.getDutyPmPeopleId()));
                dutyVillageRecord.setPmPeopleName(people.getPeopleName());
            }

        }
        return page;
    }


    /**
     * 根据Id，获取本日第一个签到人信息
     *
     * @return
     */
    @Override
    public DutyVillageRecord getTodayRecord(Long areaId) {
        Date today = new Date();
        return getByVillageId(areaId, today);
    }


    /**
     * 查询今天是否已经存在签到记录
     *
     * @param areaId
     * @param signTime
     * @return
     */
    @Override
    public List<DutyVillageRecord> queryTodayRecord(Long areaId, Date signTime) {
        List<Long> cityCodeList = Lists.newArrayList(areaId);
        return this.entityDao.queryRecordList(cityCodeList, signTime, signTime, true);
    }

    /**
     * 获取今天的签到记录
     *
     * @param areaId
     * @return
     */
    @Override
    public DutyVillageRecord getByVillageId(Long areaId, Date signTime) {
        List<DutyVillageRecord> list = queryTodayRecord(areaId, signTime);
        if (UtilCollection.isEmpty(list)) {
            return null;
        } else {
            DutyVillageRecord record = list.get(0);
            DutyPeople people = peopleService.get(UtilNumber.createLong(record.getDutyPeopleId()));
            record.setPeopleName(people.getPeopleName());
            if (record.getDutyPmPeopleId() != null) {
                DutyPeople pmPeople = peopleService.get(UtilNumber.createLong(record.getDutyPmPeopleId()));
                record.setPmPeopleName(pmPeople.getPeopleName());
            }
            return record;
        }
    }


    /**
     * 获取
     *
     * @param areaId
     * @return
     */
    @Override
    public DutyVillageRecord getByVillageIdAll(Long areaId, Date signTime) {
        List<Long> cityCodeList = Lists.newArrayList(areaId);
        List<DutyVillageRecord> list = this.entityDao.queryRecordList(cityCodeList, signTime, signTime, null);
        if (UtilCollection.isEmpty(list)) {
            return null;
        } else {
            DutyVillageRecord record = list.get(0);
            return record;
        }
    }

    /**
     * 如果该村下面当天的记录已经生成，将不会在重复生成
     * @param entity
     */
    @Override
    public void preSaveHandler(DutyVillageRecord entity) {
        List<Long> dutyVillageRecords = entityDao.queryAtSameDay(entity.getHopeAmSignTime(), entity.getVillageId());
        if (UtilCollection.isNotEmpty(dutyVillageRecords)){
            log.error("行政id：{}，在当天：{}已经生成，无法重复添加",entity.getVillageId(),entity.getHopeAmSignTime());
            throw new AppException("行政id：{"+ entity.getVillageId() +"}，在当天已经生成，无法重复添加");
//            delete(dutyVillageRecords);
        }

        super.preSaveHandler(entity);
    }

    @Override
    public List<String> getAllRecordDate() {
        List<String> allRecordDate = this.entityDao.getAllRecordDate();
        return allRecordDate;

    }
}
