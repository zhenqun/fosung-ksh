package com.fosung.ksh.duty.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @program: visual_nofeelattendance
 * @description: 大数据中心数据返回多条数据的单条数据结构中属性值结构
 * @author: LZ
 * @create: 2019-04-26 10:41
 **/
@Builder
@Data
public class DataCenterRowItemPropertieVo {
    private  String itemName;
    private String itemValue;
    private Integer num = 0;
    private String type =  "PROPERTY";

}
