package com.fosung.ksh.duty.service;

import com.fosung.ksh.duty.dto.DutyArea;

import java.util.List;

public interface MapService {
    /**
     * 获取其他上级签到信息和设备数据
     *
     * @param areaId
     */
    List<DutyArea> queryList(Long areaId);
}
