package com.fosung.ksh.meeting.controller;

import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.meeting.entity.dto.ResDataWareItem;
import com.fosung.ksh.meeting.service.MeetingRoomPersonService;
import com.fosung.ksh.meeting.service.MeetingRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 大屏展示相关接口
 *
 * @author toquery
 * @version 1
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/big-data")
public class BigDataController extends AppIBaseController {

    @Resource
    private MeetingRoomService meetingRoomService;

    @Resource
    private MeetingRoomPersonService meetingRecordPersonService;

    /*
     * 正在召开会议数量
     */
    @RequestMapping("/meeting/ing")
    public ResDataWareItem meetingsIng(@RequestParam(required = false, defaultValue = "${app.dt.sync.rootId:}") String orgId) {
        Map<String, Integer> result = meetingRoomService.countMeetingType(orgId,"GENERAL");
        return fillData(result.get("allNum"));
    }

    // 本年度会议数量
    @RequestMapping("/meeting/year")
    public ResDataWareItem meetingsAllYear(@RequestParam(required = false, defaultValue = "${app.dt.sync.rootId:}") String orgId) {
        int num = meetingRoomService.meetingsAllYear(orgId);
        return fillData(num);
    }

    // 正在接入站点数量
    @RequestMapping("/site/ing")
    public ResDataWareItem sitesIng(@RequestParam(required = false, defaultValue = "${app.dt.sync.rootId:}") String orgId) {
        long num = meetingRecordPersonService.sitesIng(orgId);
        return fillData(String.valueOf(num));
    }

    // 本年度接入站点数量
    @RequestMapping("/site/year")
    public ResDataWareItem sitesAllYear(@RequestParam(required = false, defaultValue = "${app.dt.sync.rootId:}") String orgId) {
        long num = meetingRecordPersonService.sitesAllYear(orgId);
        return fillData(String.valueOf(num));
    }


    private ResDataWareItem fillData(Object value) {
        ResDataWareItem resDataWareItem = new ResDataWareItem();
        resDataWareItem.setResult(true);
        resDataWareItem.setItemValue(value);
        resDataWareItem.setType("DATA_ITEM");
        return resDataWareItem;
    }
}
