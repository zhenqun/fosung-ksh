package com.fosung.ksh.duty.vo;



import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

/**
 * 大数据中心返回实体对象
 */
@ToString
@Data
public class DataCenterVo  implements Serializable {
    /**
     * 行政编码
     */
    private String cityCode;
    /**
     * 考勤点数量
     */
    private Integer attendancePointsNumber;

    /**
     * 设备正常运行百分比
     */
    private String runPercentage;

    /**
     * 累计考勤天数
     */
    private Integer attendanceDays;

    /**
     * 月度出勤率
     */
    private String attendanceRateMonth;

    /**
     * 今日已经签到的数量
     */
    private Integer isSignNumToday;

    /**
     * 今日出勤率
     */
    private String attendanceRateToday;

}
