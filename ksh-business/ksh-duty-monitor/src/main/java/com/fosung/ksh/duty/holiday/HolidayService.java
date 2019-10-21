package com.fosung.ksh.duty.holiday;

import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilDate;
import com.fosung.ksh.duty.config.constant.HolidayType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 获取指定日期的节假日信息
 * <p>
 * 每年更新一次，如果中间修改了，则需要手动处理
 */
@Service
public class HolidayService {
    @Lazy
    @Autowired
    private HolidayApi holidayApi;

    /**
     * 获取指定日期的节假日信息
     * 日期格式必须为yyyyMMdd
     *
     * @param date
     * @return
     */
    public HolidayResponse getHoliday(String date) {
        Assert.isTrue(UtilDate.isValidDate(date, "yyyyMMdd"), "日期格式必须为yyyyMMdd");
        String holiday = holidayApi.holiday(date);
        HolidayResponse holidayResponse = JsonMapper.parseObject(holiday, HolidayResponse.class);

        for (HolidayType holidayType : HolidayType.values()) {
            if (holidayResponse.getData() == holidayType.getIndex()) {
                holidayResponse.setHolidayType(holidayType);
            }
        }

        return holidayResponse;
    }

}
