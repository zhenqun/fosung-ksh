package com.fosung.ksh.meeting.control.hst.config.constant;


import com.fosung.framework.common.support.AppRuntimeDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 会议室状态
 *
 * @author wangyh
 * @date 2018/12/01
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum RoomStatus implements AppRuntimeDict {
    CLOSED("0","关闭"),
    OPENED("1","开启");

    String code;
    String remark;

}
