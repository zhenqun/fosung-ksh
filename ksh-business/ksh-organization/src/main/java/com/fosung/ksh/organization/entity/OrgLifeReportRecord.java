package com.fosung.ksh.organization.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.ksh.organization.constant.OrgLifeReportStatus;
import com.fosung.ksh.organization.constant.OrgLifeStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 组织生活报备记录实体对象
 *
 * @author wangyh
 */
@Entity
@Table(name = "org_life_report_record")
@Setter
@Getter
public class OrgLifeReportRecord extends AppJpaBaseEntity {

    /**
     * 报备ID
     */
    @ApiModelProperty("报备ID")
    @Column(name = "report_id")
    private Long reportId;

    @ApiModelProperty("组织生活ID")
    @Column(name = "org_life_id")
    private Long orgLifeId;

    @ApiModelProperty("组织ID")
    @Column(name = "org_id")
    private String orgId;

    @ApiModelProperty("支部ID")
    @Column(name = "branch_id")
    private String branchId;

    @ApiModelProperty("支部Code")
    @Column(name = "branch_code")
    private String branchCode;
    /**
     * 计划召开时间
     */
    @ApiModelProperty("计划召开时间")
    @Temporal(TemporalType.DATE)
    @Column(name = "hope_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = AppProperties.DATE_PATTERN)
    private Date hopeDate;


    @ApiModelProperty("关联组织生活")
    @Transient
    private OrgLife orgLife;

    /**
     * 关联组织生活召开状态
     */
    @ApiModelProperty("组织生活报备召开状态")
    @Transient
    @Excel(name = "组织生活报备召开状态", orderNum = "3")
    private OrgLifeReportStatus orgLifeReportStatus;

    /**
     * 未召开数量 正在召开数量 召开中数量
     */
    @ApiModelProperty("实际召开时间")
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = AppProperties.DATE_PATTERN)
    private Date realTime;

    /**
     * 未召开数量 正在召开数量 召开中数量
     */
    @ApiModelProperty("未召开数量")
    @Transient
    private Integer unCount;

    @ApiModelProperty("正在召开数量")
    @Transient
    private Integer ingCount;

    @ApiModelProperty("召开中数量")
    @Transient
    private Integer endCount;


    @ApiModelProperty("时间")
    @Transient
    private String dateTime;


    @ApiModelProperty("组织名称")
    @Transient
    private String orgName;


    @ApiModelProperty("组织名称")
    @Transient
    private String branchName;
}
