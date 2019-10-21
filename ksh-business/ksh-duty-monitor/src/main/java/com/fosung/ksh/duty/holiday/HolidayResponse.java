package com.fosung.ksh.duty.holiday;

import com.fosung.ksh.duty.config.constant.HolidayType;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 请求第三方接口查询日期类型
 * @author LZ
 * @date 2019-05-10 15:05
*/
@Getter
@Setter
public class HolidayResponse {
    /**
     * 响应码
     */
    private Integer code;
    /**
     * 响应数据
     */
    private Integer data;
    /**
     * 相应数据类型
     */
    private HolidayType holidayType;
}
