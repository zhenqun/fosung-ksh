package com.fosung.ksh.monitor.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.duty.dto.DutyArea;
import com.fosung.ksh.monitor.entity.MonitorCamera;
import com.fosung.ksh.sys.dto.SysArea;
import org.assertj.core.util.Maps;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MonitorCameraService extends AppBaseDataService<MonitorCamera, Long> {

    List<MonitorCamera> queryByVillageId(Long villageId );
    /**
     * 根据行政id获取设备详情
     *
     * @param villageId
     * @return
     */
    public MonitorCamera getByVillageId(Long villageId);


    /**
     * 获取有监控设备的村庄列表,并默认缓存10分钟
     * 防止重复查询数据库，优化查询速度
     *
     * @param villageId
     * @return
     */
    public List<SysArea> queryVillageHasCameraList(Long villageId);


    public Page<MonitorCamera> queryPageList(Long areaId, String monitorStatus, int pageNum, int pageSize);

    /**
     * 镇级及以上统计自身设备在线率，离线率
     *
     * @return
     */
    public DutyArea countSelfRate(Long areaId);

    /**
     * 镇级及以上统计下级设备在线率，离线率，设备在线数量，离线数量
     *
     * @return
     */
    public List<DutyArea> queryChildrenRateList(Long areaId);

}
