package com.fosung.ksh.organization.ezb.constant;


import com.fosung.framework.common.support.AppRuntimeDict;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 附件类型
 *
 * @author wangyh
 * @date 2018/12/01
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum AttachmentType implements AppRuntimeDict {
    /**
     * 图片
     */
    IMAGE("3"),

    /**
     * 文件
     */
    FILE("8");

    String remark;
}
