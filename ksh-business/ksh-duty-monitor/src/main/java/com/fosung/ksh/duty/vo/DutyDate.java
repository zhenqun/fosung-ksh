package com.fosung.ksh.duty.vo;


import com.fosung.ksh.duty.entity.DutyShift;
import com.fosung.ksh.duty.entity.DutySpecialDay;
import com.fosung.ksh.duty.entity.DutyWorkDay;
import lombok.Data;


import java.io.Serializable;
import java.util.List;

@Data
public class DutyDate implements Serializable {
    /**
     * 镇级行政编码
     */
    private String cityCode;
    /**
     * 班次设置，包含节假日是否排班
     */
    private DutyShift dutyShift;

    /**
     * 工作日设置
     */
    private List<DutyWorkDay> dutyWorkDays;
    /**
     * 特殊日期设置
     */
    private List<DutySpecialDay> dutySpecialDays;
}
