package com.fosung.ksh.organization.ezb.dto;


import com.google.common.collect.Lists;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author wangyh
 */
@Data
public class OrgLifeMeetingDTO implements Serializable {

    private static final long serialVersionUID = 2177207316973859987L;

    /**
     * 主键ID
     */
    private String meetingId;

    /**
     * 支部ID
     */
    @NotBlank
    private String branchId;
    /**
     * 支部名称
     */
    @NotBlank
    private String branchName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 地点
     */
    private String meetingAddress;

    /**
     * 内容
     */
    private String meetingContent;

    /**
     * 组织生活名称
     */
    private String meetingName;

    /**
     * 会议人数
     */
    private Integer meetingNum;

    /**
     * 活动记录
     */
    private String meetingLog;

    /**
     * 组织编码
     */
    @NotBlank
    private String orgCode;


    /**
     * 实到人数
     */
    private Integer actualNum;

    /**
     * 缺席人数
     */
    private Integer absentNum;

    /**
     * 主讲人
     */
    private String speaker;

    /**
     * 记录人
     */
    private String recorder;

    /**
     * 发布状态，过期字段
     */
    @Deprecated
    private String  publishFlag  = "1";

    /**
     * 图片
     */
    private List<AttachmentDTO> attachmentList = Lists.newArrayList();

    /**
     * 文件
     */
    private List<AttachmentDTO> uploadFileList = Lists.newArrayList();

    /**
     * 党员
     */
    private List<MeetingPersonDTO> userlist = Lists.newArrayList();

    /**
     * 组织生活类型
     */
    private List<MeetingClassInficationDTO> meetingUnClassInficationList = Lists.newArrayList();

}
