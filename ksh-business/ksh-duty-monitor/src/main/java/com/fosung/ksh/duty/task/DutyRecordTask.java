package com.fosung.ksh.duty.task;

import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilDate;
import com.fosung.framework.task.AppTaskCluster;
import com.fosung.ksh.duty.config.DutyProperties;
import com.fosung.ksh.duty.entity.DutyPeople;
import com.fosung.ksh.duty.entity.DutyRecord;
import com.fosung.ksh.duty.entity.DutyVillageRecord;
import com.fosung.ksh.duty.service.DutyPeopleService;
import com.fosung.ksh.duty.service.DutyRecordService;
import com.fosung.ksh.duty.service.DutyVillageRecordService;
import com.fosung.ksh.monitor.client.SignRecordClient;
import com.fosung.ksh.monitor.dto.PersonInfo;
import com.fosung.ksh.monitor.dto.PersonRecordInfo;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 值班签到记录定时同步
 *
 * @author wangyihua
 * @date 2019-05-09 10:28
 */
@Slf4j
@Component
public class DutyRecordTask {

    @Resource
    private AppTaskCluster appTaskCluster;

    @Autowired
    private DutyProperties dutyProperties;

    @Autowired
    private SignRecordClient recordClient;

    @Autowired
    private DutyRecordService recordService;

    @Autowired
    private DutyPeopleService peopleService;

    @Autowired
    private DutyVillageRecordService villageRecordService;

    /**
     * 定时同步
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
   // @Scheduled(fixedDelay = 1000L * 60 * 2)
    public void syncRecord() {
        //只在集群中的第一台机器中执行
        if (appTaskCluster.needRunTask()) {
            Date endTime = new Date();
            // 获取最后一次记录的时间，作为开始时间
            Date startTime = recordService.queryMaxTime();
            String startStr = UtilDate.getDateFormatStr(startTime, AppProperties.DATE_TIME_PATTERN);
            String endStr = UtilDate.getDateFormatStr(endTime, AppProperties.DATE_TIME_PATTERN);
            // 获取所有的需要查询的人脸库id
            Set<String> libIds = dutyProperties.getLibIds();
            for (String libId : libIds) {
                // 第一步：获取上次最后一条的记录时间和现在时间段的签到记录
                List<PersonRecordInfo> list = recordClient.recordList(
                        libId,
                        startStr,
                        endStr,
                        ""
                );

                log.debug("值班签到记录同步结果:人脸库={},开始时间={},结束时间={},同步条数：{}", libId, startStr, endStr, list.size());
                for (PersonRecordInfo recordInfo : list) {
                    for (PersonInfo human : recordInfo.getHumans()) {
                        try {
                            String alarmId = recordInfo.getAlarmId();
                            String humanId = human.getHumanId();

                            // 第二步：验证当前签到人员是否为值班人员
                            DutyPeople people = peopleService.getByHumanId(humanId);
                            if (people != null) {

                                // 第三步：验证当前签到记录是否已保存
                                DutyRecord record = recordService.getByAlarmId(alarmId, humanId);
                                if (record == null) {
                                    record = new DutyRecord();
                                    record.setHumanId(people.getHumanId());
                                    record.setDutyPeopleId(people.getId().toString());
                                    record.setDutySignTime(recordInfo.getAlarmTime());
                                    record.setIndexCode(recordInfo.getIndexCode());
                                    record.setAlarmId(alarmId);
                                    recordService.save(record);

                                    // 第四步：验证本村今日的签到记录是否已经保存
                                    DutyVillageRecord villageRecord = villageRecordService.getByVillageIdAll(people.getVillageId(), recordInfo.getAlarmTime());

                                    if (villageRecord != null) {
                                        // 判断上午签到时间是否存在
                                        if (villageRecord.getAmSignTime() == null){
                                            // 添加上午签到时间
                                            villageRecord.setDutyPeopleId(people.getId() + "");
                                            villageRecord.setAmSignTime(recordInfo.getAlarmTime());
                                            villageRecord.setIndexCode(recordInfo.getIndexCode());
                                            villageRecordService.update(villageRecord,
                                                    Sets.newHashSet("dutyPeopleId",
                                                            "amSignTime",
                                                            "indexCode"));

                                        }else {
                                            // 判断签到时间是否在12点之前
                                            if (isAm(recordInfo.getAlarmTime())){
                                                // 判断上午第二次签到时间是否大于第一次
                                                if (recordInfo.getAlarmTime().getTime()>villageRecord.getAmSignTime().getTime()){
                                                    // 添加上午签退时间
                                                    villageRecord.setAmSignOffTime(recordInfo.getAlarmTime());
                                                    villageRecordService.update(villageRecord, Sets.newHashSet("amSignOffTime"));
                                                }else {
                                                    // 添加上午签退时间
                                                    villageRecord.setAmSignOffTime(villageRecord.getAmSignTime());
                                                    villageRecord.setAmSignTime(recordInfo.getAlarmTime());
                                                    villageRecordService.update(villageRecord, Sets.newHashSet("amSignOffTime","amSignTime"));
                                                }
                                            }else {
                                                // 判断下午签到时间是否存在
                                                if (villageRecord.getPmSignTime() == null){
                                                    // 添加下午签到时间
                                                    villageRecord.setDutyPmPeopleId(people.getId() + "");
                                                    villageRecord.setPmSignTime(recordInfo.getAlarmTime());
                                                    villageRecordService.update(villageRecord,
                                                            Sets.newHashSet("dutyPmPeopleId", "pmSignTime"));
                                                } else {
                                                    // 添加下午签退时间
                                                    villageRecord.setPmSignOffTime(recordInfo.getAlarmTime());
                                                    villageRecordService.update(villageRecord, Sets.newHashSet( "pmSignOffTime"));
                                                }

                                            }
                                        }







                                    }
//                                    }
                                }
                            }
                        } catch (Exception e) {
                            log.error("签到记录保存失败：失败的人员：\nrecordInfo={},\nhuman={},\n错误信息：{}",
                                    JsonMapper.toJSONString(recordInfo, true),
                                    JsonMapper.toJSONString(human, true),
                                    ExceptionUtils.getStackTrace(e));
                        }

                    }
                }
            }

        }
    }

    /**
     * 判断签到时间是否为12点前
     * @param dutySignTime
     * @return
     */
    public static Boolean isAm(Date dutySignTime) {
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String str = df.format(dutySignTime);
        int a = Integer.parseInt(str);
        if (a >= 0 && a <= 12) {
            log.info("{}当前时间是上午");
            return true;
        }
        return false;
    }
}


