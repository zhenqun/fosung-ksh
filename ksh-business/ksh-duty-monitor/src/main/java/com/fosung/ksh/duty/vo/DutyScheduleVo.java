package com.fosung.ksh.duty.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: fosung-ksh
 * @description: 根据人员排班进行分组查询返回数据
 * @author: LZ
 * @create: 2019-05-15 13:53
 **/
@Builder
@Data
@AllArgsConstructor
public class DutyScheduleVo implements Serializable {

    /**
     * 镇级的城市代码
     */

    private String townCityCode;
    /**
     * 镇级的城市名称
     */

    private String townCityName;
    /**
     * 村级的城市代码
     */

    private String cityCode;
    /**
     * 村级行政id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long areaId;



    private String cityName;

    public DutyScheduleVo(String townCityCode, String cityCode, String cityName) {
        this.townCityCode = townCityCode;
        this.cityCode = cityCode;
        this.cityName = cityName;
    }

}
