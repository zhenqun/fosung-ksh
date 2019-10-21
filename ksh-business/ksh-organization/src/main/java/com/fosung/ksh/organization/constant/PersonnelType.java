package com.fosung.ksh.organization.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum PersonnelType {
    JOIN("1","参会"), NOT_JOIN("2","未参会");
    private String code;
    private String remark;
}
