package com.fosung.ksh.duty.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.common.constant.DateTrunc;
import com.fosung.ksh.duty.dto.DutyArea;
import com.fosung.ksh.duty.entity.DutyVillageRecord;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface DutyVillageRecordService extends AppBaseDataService<DutyVillageRecord, Long> {
    /**
     * 镇级及以上统计自身签到率，日期为本日
     *
     * @return
     */
    DutyArea countSelfRate(DateTrunc type, Long areaId, Date signTime);

    /**
     * 计算签到率
     *
     * @param type     类型， 年月周日
     * @param areaId
     * @param signTime
     * @return
     */
    List<DutyArea> queryChildrenRateList(DateTrunc type, Long areaId, Date signTime);

    /**
     * 根据传入日期，获取签到数据
     *
     * @return
     */
    Page<DutyVillageRecord> queryRecordList(Long areaId, Boolean isSign, String signTime, int pageNum, int pageSize);

    /**
     * 根据开始时间结束时间，获取当前时间段内签到数据
     *
     * @return
     */
    Page<DutyVillageRecord> queryRecordList(Long areaId, Boolean isSign, String startTime, String endTime, int pageNum, int pageSize);

    /**
     * 根据Id，获取本日第一个签到人信息
     *
     * @return
     */
    DutyVillageRecord getTodayRecord(Long areaId);

    /**
     * 查询今天是否已经存在签到记录
     *
     * @param areaId
     * @param signTime
     * @return
     */
    List<DutyVillageRecord> queryTodayRecord(Long areaId, Date signTime);

    /**
     * 获取今天的签到记录
     *
     * @param areaId
     * @return
     */
    DutyVillageRecord getByVillageId(Long areaId, Date signTime);

    List<String> getAllRecordDate();

    /**
     * 获取
     *
     * @param areaId
     * @return
     */
    public DutyVillageRecord getByVillageIdAll(Long areaId, Date signTime);
}
