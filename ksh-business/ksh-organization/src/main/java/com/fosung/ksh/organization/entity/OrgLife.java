package com.fosung.ksh.organization.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.framework.common.support.dao.entity.AppJpaSoftDelEntity;
import com.fosung.ksh.organization.constant.OrgLifeStatus;
import com.fosung.ksh.organization.constant.PushStatus;
import com.fosung.ksh.organization.constant.Sign;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 组织生活实体对象
 *
 * @author wangyh
 */
@Entity
@Table(name = "org_life")
@Setter
@Getter
public class OrgLife extends AppJpaBaseEntity implements AppJpaSoftDelEntity {


    /**
     * 组织生活对应会议室ID
     */
    @ApiModelProperty("组织生活会议室ID")
    @Column(name = "meeting_room_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long meetingRoomId;


    /**
     * 每隔X分钟抓拍会场图片
     */
    @ApiModelProperty("每隔多久抓拍一次图片")
    @Column(name = "interval_time")
    private Integer intervalTime;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    @Column(name = "meeting_name")
    private String meetingName;


    /**
     * 主持人
     */
    @ApiModelProperty("主持人")
    @Column(name = "speaker")
    private String speaker;


    /**
     * 记录人
     */
    @ApiModelProperty("记录人")
    @Column(name = "recorder")
    private String recorder;


    /**
     * 活动地址
     */
    @ApiModelProperty("活动地址")
    @Column(name = "meeting_address")
    private String meetingAddress;


    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间，默认为当前创建时间")
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    @Column(name = "start_date")
    private Date startDate;


    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    @Column(name = "end_date")
    private Date endDate;


    /**
     * 主要内容
     */
    @ApiModelProperty("主要内容，富文本")
    @Column(columnDefinition = "text", name = "meeting_content")
    private String meetingContent;


    /**
     * 0:否,1:是
     */
    @ApiModelProperty("发布状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "push_status")
    private PushStatus pushStatus = PushStatus.NOTPUSH;

    /**
     * 发布时间
     */
    @ApiModelProperty("发布时间")
    @Column(name = "publish_time")
    private String publishTime;

    @ApiModelProperty("填写当前信息所属的党组织")
    @Column(name = "org_id")
    private String orgId;

    @ApiModelProperty("组织生活进行状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "org_life_status")
    private OrgLifeStatus orgLifeStatus = OrgLifeStatus.GOING;


    /**
     * 0:否,1:是
     */
    @Column(name = "del")
    private Boolean del = false;


    @ApiModelProperty("会议是否签到 sign 1 是 2否")
    @Column(name = "sign")
    @Enumerated(EnumType.STRING)
    private Sign sign= Sign.ENABLE;

    @ApiModelProperty("签到时长")
    @Column(name = "sign_duration")
    private Integer signDuration;


    @Column(name = "repair_sign")
    @Enumerated(EnumType.STRING)
    private Sign repairSign=Sign.ENABLE;

    @ApiModelProperty("补签时长")
    @Column(name = "repair_duration")
    private Integer repairDuration;

    @ApiModelProperty("党组织名称")
    @Transient
    private String branchNames;

    /**
     * 关联主题党日报备ID
     */
    @ApiModelProperty("关联主题党日报备ID")
    @Transient
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long reportRecordId;


    @ApiModelProperty("组织生活类型名称")
    @Transient
    private String typesName;

    @ApiModelProperty("组织生活开始结束时间")
    @Transient
    private String startAndEndTime;

    /**
     * 人员hash
     */
    @ApiModelProperty("应参会人数")
    @Transient
    private Integer meetingNum ;

    /**
     * 缺席人数
     */
    @ApiModelProperty("缺席人数")
    @Transient
    private Integer absentNum;

    /**
     * 实到人数
     */
    @ApiModelProperty("实到人数")
    @Transient
    private Integer actualNum;

    /**
     * 组织生活类型
     */
    @ApiModelProperty("组织生活类型列表")
    @Transient
    private List<OrgLifeType> types = Lists.newArrayList();

    /**
     * 人员列表
     * todo 将要扩展支持多个支部同时召开组织生活
     */
    @ApiModelProperty("参会的组织党支部列表")
    @Transient
    private List<OrgLifeBranch> branches = Lists.newArrayList();

    /**
     * 图片列表
     */
    @ApiModelProperty("图片列表")
    @Transient
    private List<OrgLifeAttachment> images = Lists.newArrayList();

    /**
     * 文件列表
     */
    @ApiModelProperty("文件列表")
    @Transient
    private List<OrgLifeAttachment> files = Lists.newArrayList();

    /**
     * 文件列表
     */
    @ApiModelProperty("文件列表")
    @Transient
    private List<OrgLifeAttachment> attachments = Lists.newArrayList();


    /**
     * 文件列表
     */
    @ApiModelProperty("参会人员列表，用于统一保存")
    @Transient
    private List<OrgLifePeople> peoples = Lists.newArrayList();

    /**
     * 手机TV，传输数据ID
     */
    @Transient
    private String dropId;

}
