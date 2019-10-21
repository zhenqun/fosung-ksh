package com.fosung.ksh.duty.service.impl;

import com.fosung.framework.common.util.UtilNumber;
import com.fosung.ksh.common.constant.DateTrunc;
import com.fosung.ksh.duty.dto.DutyArea;
import com.fosung.ksh.duty.service.DutyVillageRecordService;
import com.fosung.ksh.monitor.service.MonitorCameraService;
import com.fosung.ksh.sys.client.SysAreaClient;
import com.fosung.ksh.sys.dto.SysArea;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapServiceImpl implements com.fosung.ksh.duty.service.MapService {


    @Autowired
    private SysAreaClient sysAreaClient;

    @Autowired
    private MonitorCameraService monitorCameraService;


    @Autowired
    private DutyVillageRecordService villageRecordService;

    /**
     * 获取当前节点设备数据与签到数据
     *
     * @param areaId
     */
    @Override
    public List<DutyArea> queryList(Long areaId) {
        List<DutyArea> dutyArealist = Lists.newArrayList();

        List<SysArea> areaList = sysAreaClient.queryAreaList(areaId, "");
        for (SysArea sysArea : areaList) {
            DutyArea dutyArea = new DutyArea(sysArea);
            DutyArea recordDutyArea = villageRecordService.countSelfRate(DateTrunc.DAY, sysArea.getAreaId(), new Date());
            dutyArea.setSignNumber(recordDutyArea.getSignNumber());
            dutyArea.setNotSignNumber(recordDutyArea.getNotSignNumber());
            if (recordDutyArea != null && recordDutyArea.getAllSignNumber() != 0) {
                DutyArea cameraArea = monitorCameraService.countSelfRate(sysArea.getAreaId());
                dutyArea.setOnlineNumber(cameraArea.getOnlineNumber());
                dutyArea.setOfflineNumber(cameraArea.getOfflineNumber());
                dutyArealist.add(dutyArea);
            }
        }


        for (int i = 0; i < dutyArealist.size(); i++) {
            dutyArealist.get(i).setRanking(i + 1);
        }
        return dutyArealist;
    }

}
