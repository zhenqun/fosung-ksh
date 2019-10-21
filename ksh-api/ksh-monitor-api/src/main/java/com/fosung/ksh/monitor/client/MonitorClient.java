package com.fosung.ksh.monitor.client;

import com.fosung.ksh.monitor.dto.MonitorInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 人脸采集接口
 *
 * @author wangyh
 */
@FeignClient(name = "ksh-monitor", path = "/monitor")
public interface MonitorClient {


    /**
     * 查询设备信息
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public List<MonitorInfo> queryMonitorList();

}
