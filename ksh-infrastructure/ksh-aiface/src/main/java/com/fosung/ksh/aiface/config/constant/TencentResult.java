package com.fosung.ksh.aiface.config.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum TencentResult {
    ErrorPersonNotExisted("个体不存在。");
    String remark;
}
