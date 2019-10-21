package com.fosung.ksh.duty.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: visual_nofeelattendance
 * @description: 大数据中心数据返回单条数据结构
 * @author: LZ
 * @create: 2019-04-26 10:41
 **/

@Getter
@Setter
public class DataCenterSingleVo {
    private  Boolean result = true;
    private String itemValue;
    private String type =  "DATA_ITEM";

}
