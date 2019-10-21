package com.fosung.ksh.duty.vo;

import lombok.Data;

import java.util.List;

/**
 * @program: visual_nofeelattendance
 * @description: 大数据中心数据返回多条数据的单条数据结构
 * @author: LZ
 * @create: 2019-04-26 10:41
 **/

@Data
public class DataCenterRowItemVo {
    private  String itemName;
    private String itemValue;
    private List<DataCenterRowItemPropertieVo> properties;
    private String type =  "ROW";

}
