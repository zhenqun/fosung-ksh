package com.fosung.ksh.duty.holiday;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 获取指定日期的节假日信息
 * @version
 */
@FeignClient(value = "holiday-client",url = "http://api.goseek.cn", path = "/Tools/holiday")
public interface HolidayApi {

    /**
     * 获取指定日期的节假日信息
     * 正常工作日对应结果为 0, 法定节假日对应结果为 1, 节假日调休补班对应的结果为 2，休息日对应结果为 3
     *
     */
    @GetMapping
    String holiday(@RequestParam("date") String date);



}
