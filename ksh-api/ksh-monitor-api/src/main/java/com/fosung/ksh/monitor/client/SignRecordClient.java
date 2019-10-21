package com.fosung.ksh.monitor.client;

import com.fosung.ksh.monitor.dto.PersonRecordInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 人脸采集接口
 *
 * @author wangyh
 */
@FeignClient(name = "ksh-monitor", path = "/sign-record")
public interface SignRecordClient {


    /**
     * 人员签到记录
     *
     * @param libIds         人脸库ID
     * @param startAlarmTime
     * @param endAlarmTime
     * @param indexCode
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public List<PersonRecordInfo> recordList(
            @RequestParam("libIds") String libIds,
            @RequestParam("startAlarmTime") String startAlarmTime,
            @RequestParam("endAlarmTime") String endAlarmTime,
            @RequestParam(required = false, name = "indexCode") String indexCode);

}
