package com.fosung.ksh.organization.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 组织生活报备实体对象
 * @author wangyh
 */
@Entity
@Table(name="org_life_report")
@Setter
@Getter
public class OrgLifeReport extends AppJpaBaseEntity {

	/**
	 * 分支ID
	 */
	@ApiModelProperty("分支ID")
	@Excel(name = "组织ID")
	@Column(name="org_id")
	private String orgId ;

	/**
	 * code
	 */
	@ApiModelProperty("code")
	@Excel(name = "组织Code")
	@Column(name="org_code")
	private String orgCode ;

	/**
	 * 报备所属分支ID
	 */
	@Excel(name = "报备所属党支部ID")
	@ApiModelProperty("报备所属党支部ID")
	@Column(name="branch_id")
	private String branchId;

	@Transient
	@Excel(name = "报备所属党支部名称")
	@ApiModelProperty("报备所属分支名称")
	private String branchName;


	/**
	 * 召开日,每月几号
	 */
	@Excel(name = "每月召开日")
	@ApiModelProperty("召开日,每月几号")
	@Column(name="hope_day")
	private Integer hopeDay ;


	/**
	 * 召开地点
	 */
	@Excel(name = "召开地点")
	@ApiModelProperty("召开地点")
	@Column(name="address")
	private String address ;

	/**
	 * 内容摘要
	 */
	@Lob
	@Lazy(value = false)
	@Excel(name = "内容摘要")
	@ApiModelProperty("内容摘要")
	@Column(columnDefinition="text",name="content")
	private String content ;


}