package com.fosung.ksh.meeting.control.hst.config.constant;

import com.fosung.framework.common.support.AppRuntimeDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PC端页面布局方式
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum PCLayoutCode implements AppRuntimeDict {
    W1("切换到标准布局"),
    W2("切换到培训布局"),
    W3("切换到视频布局"),
    WF("切换到全屏模式"),
    WV1("切换到1分屏模式"),
    WV2("切换到2分屏模式"),
    WV3("切换到画中画分屏模式"),
    WV4("切换到4分屏模式"),
    WV6("切换到6分屏模式"),
    WV9("切换到9分屏模式"),
    WV12("切换到12分屏模式"),
    WV16("切换到16分屏模式"),
    WV25("切换到25分屏模式");
    String remark;
}

