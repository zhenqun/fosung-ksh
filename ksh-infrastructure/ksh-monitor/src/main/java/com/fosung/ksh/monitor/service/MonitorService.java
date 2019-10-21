package com.fosung.ksh.monitor.service;

import com.fosung.ksh.monitor.dto.MonitorInfo;

import java.util.List;

public interface MonitorService {
    /**
     * 查询黑名单信息
     *
     * @param credentialsNum
     * @param listLibIds
     * @return
     */
    List<MonitorInfo> queryMonitorList();
}
