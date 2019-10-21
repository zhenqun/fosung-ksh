package com.fosung.ksh.meeting.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import com.fosung.framework.common.support.anno.AppDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author toquery
 * @version 1
 */
@AppDict("person_join_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum  PersonJoinType implements AppRuntimeDict {


    JOIN("加入"), LEAVE("离开");
    private String remark;
}
