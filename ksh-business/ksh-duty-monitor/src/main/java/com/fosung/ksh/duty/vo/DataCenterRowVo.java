package com.fosung.ksh.duty.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @program: visual_nofeelattendance
 * @description: 大数据中心数据返回多条数据结构域
 * @author: LZ
 * @create: 2019-04-26 10:41
 **/

@Data
public class DataCenterRowVo {
    private  Boolean result = true;
    private List<DataCenterRowItemVo> data;
    private String type =  "ROW";
}
