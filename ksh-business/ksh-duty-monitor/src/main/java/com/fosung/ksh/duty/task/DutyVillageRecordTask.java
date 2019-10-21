package com.fosung.ksh.duty.task;

import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.task.AppTaskCluster;
import com.fosung.ksh.common.util.UtilDateTime;
import com.fosung.ksh.duty.config.constant.DutyType;
import com.fosung.ksh.duty.entity.DutyShift;
import com.fosung.ksh.duty.entity.DutyVillageRecord;
import com.fosung.ksh.duty.service.DutyShiftService;
import com.fosung.ksh.duty.service.DutyVillageRecordService;
import com.fosung.ksh.monitor.service.MonitorCameraService;
import com.fosung.ksh.sys.dto.SysArea;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 定时生成值班记录
 *
 * @author wangyihua
 * @date 2019-05-09 10:28
 */
@Slf4j
@Component
public class DutyVillageRecordTask {


    @Resource
    private AppTaskCluster appTaskCluster;

    @Autowired
    private DutyVillageRecordService villageRecordService;

    @Autowired
    private MonitorCameraService monitorCameraService;

    @Autowired
    private DutyShiftService dutyShiftService;

    /**
     * 定时同步,每天凌晨0点10分定时生成数据
     */
    @Scheduled(cron = "0 10 0 * * ?")
//    @Scheduled(cron = "0 0/5 * * * ?")
//    @Scheduled(fixedDelay = 1000L * 60 * 2)
    public void syncRecord() {
        //只在集群中的第一台机器中执行
        if (appTaskCluster.needRunTask()) {
            Date shiftDate = new Date();
            int i = 0;


            // 获取所有的班次
            List<DutyShift> shiftList = dutyShiftService.queryAll(Maps.newHashMap());

            for (DutyShift dutyShift : shiftList) {
                // 判定区域当日是否需要值班
                Boolean onDuty = dutyShiftService.onDuty(dutyShift.getAreaId(), shiftDate);
                if (onDuty) {
                    Date amStart = getTime(shiftDate, dutyShift.getMorningStartTime());
                    Date amEnd = getTime(shiftDate, dutyShift.getMorningStartTime());
                    Date pmStart = getTime(shiftDate, dutyShift.getAfternoonStartTime());
                    Date pmEnd = getTime(shiftDate, dutyShift.getAfternoonEndTime());

                    List<SysArea> sysAreaList = monitorCameraService.queryVillageHasCameraList(dutyShift.getAreaId());
                    for (SysArea sysArea : sysAreaList) {
                        DutyVillageRecord villageRecord = new DutyVillageRecord();
                        villageRecord.setVillageId(sysArea.getAreaId());
                        villageRecord.setHopeAmSignTime(amStart);
                        villageRecord.setHopeAmSignOffTime(amEnd);
                        villageRecord.setHopePmSignTime(pmStart);
                        villageRecord.setHopePmSignOffTime(pmEnd);
                        villageRecord.setDutyType(DutyType.DUTY);
                        try {
                            villageRecordService.save(villageRecord);
                            i++;
                        }catch (Exception e){
                            log.error("\n签到记录添加失败");
                            log.error("\n" + ExceptionUtils.getStackTrace(e));
                        }


                    }

                }
            }
            log.info("值班记录添加成功，共{}条", i);
        }
    }


    /**
     * 获取时间
     *
     * @param date
     * @param time
     * @return
     */
    private Date getTime(Date date, String time) {
        String dateTime = UtilDateTime.getDateFormatStr(date, AppProperties.DATE_PATTERN);
        dateTime += " " + time + ":00";
        return UtilDateTime.parse(dateTime);
    }
}

