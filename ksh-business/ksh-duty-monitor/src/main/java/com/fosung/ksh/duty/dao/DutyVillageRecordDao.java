package com.fosung.ksh.duty.dao;

import com.fosung.framework.dao.config.mybatis.page.MybatisPage;
import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.framework.dao.jpa.annotation.MybatisQuery;
import com.fosung.ksh.duty.entity.DutyVillageRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DutyVillageRecordDao extends AppJPABaseDao<DutyVillageRecord, Long> {
    /**
     * 获取累计考勤的日期
     *
     * @return
     */
    @Query("SELECT date_trunc('day',dvr.amSignTime) from DutyVillageRecord dvr GROUP BY date_trunc('day',amSignTime)")
    List<String> getAllRecordDate();

    /**
     * 查询当天的村级记录是否已经生成
     *
     * @return
     */
    @Query("SELECT dvr.id from DutyVillageRecord dvr where date_trunc('day',hopeAmSignTime) = date_trunc('day',DATE(?1)) and dvr.villageId = ?2"  )
//    @Query(value = "SELECT count(*) from duty_village_record dvr where date_trunc('day',hope_am_sign_time) = date_trunc('day',DATE(?1)) and dvr.village_id=?2", nativeQuery = true  )
    List<Long> queryAtSameDay(Date hopeAmSignTime,Long villageId);
    /**
     * 根据开始时间和结束时间查询签到记录
     *
     * @param areaIdList
     * @param startTime
     * @return
     */
    @MybatisQuery
    public List<DutyVillageRecord> queryRecordList(@Param("areaIdList") List<Long> areaIdList,
                                                   @Param("startTime") Date startTime,
                                                   @Param("endTime") Date endTime,
                                                   @Param("isSign") Boolean isSign);

    /**
     * 根据开始时间和结束时间查询签到记录
     *
     * @param areaIdList
     * @param startTime
     * @return
     */
    @MybatisQuery
    public MybatisPage<DutyVillageRecord> queryRecordList(@Param("areaIdList") List<Long> areaIdList,
                                                          @Param("startTime") Date startTime,
                                                          @Param("endTime") Date endTime,
                                                          @Param("isSign") Boolean isSign,
                                                          Pageable pageRequest);

    /**
     * 查询当前时间签到数据统计
     *
     * @param type       统计类型 month day
     * @param signTime
     * @param areaIdList
     * @return
     */
    @MybatisQuery
    public Integer countRecord(@Param("type") String type,
                               @Param("signTime") Date signTime,
                               @Param("areaIdList") List<Long> areaIdList,
                               @Param("dutyType") String dutyType,
                               @Param("isSign") Boolean isSign);

}
