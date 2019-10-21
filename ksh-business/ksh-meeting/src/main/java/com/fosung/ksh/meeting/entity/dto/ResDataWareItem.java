package com.fosung.ksh.meeting.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 行数据类型响应数据结构
 * @author : yanxm
 * @date : 2019-04-10 09:12
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class ResDataWareItem extends ResDataWareBase{


    /**
     * itemValue : 123
     */

    private Object itemValue;
}
