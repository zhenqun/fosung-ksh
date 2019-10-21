package com.fosung.ksh.organization.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.framework.common.support.dao.entity.AppJpaSoftDelEntity;
import com.fosung.ksh.organization.constant.PersonnelType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 组织生活人员实体对象
 * @author wangyh
 */
@Entity
@Table(name="org_life_people")
@Setter
@Getter
public class OrgLifePeople extends AppJpaBaseEntity {

	/**
	 * 会议记录ID
	 */
	@Column(name="org_life_id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long orgLifeId ;

    /**
	 * 人员类型 1：参加人员列表 2：缺席人员列表
	 */
    @ApiModelProperty("人员状态")
	@Enumerated(EnumType.STRING)
	@Column(name="personnel_type")
	private PersonnelType personnelType = PersonnelType.NOT_JOIN;


	/**
	 * 人员ID ,不能为空，组织生活用到
	 */
	// @ApiModelProperty("人员Id，基本已弃用")
	@ApiModelProperty("人员Id")
	@Column(name="personnel_id")
	private String personnelId ;

	/**
	 * 人员ID
	 */
	@ApiModelProperty("人员所属党组织ID")
	@Column(name="branch_id")
	private String branchId;

	@ApiModelProperty("人员所属党组织名称")
	@Transient
	private String branchName;


	/**
	 * 人员hash
	 */
	@ApiModelProperty("人员hash，用户唯一标志")
	@Column(name="personnel_hash")
	private String personnelHash ;


	/**
	 * 人员名称
	 */
	@ApiModelProperty("真实姓名")
	@Column(name="personnel_name")
	private String personnelName ;


	/**
	 * 互联网名称
	 */
	@ApiModelProperty("互联网姓名")
	@Column(name="inter_name")
	private String interName ;

	/**
	 * 手机号
	 */
	@ApiModelProperty("人员手机号")
	@Column(name="telephone")
	private String telephone ;

	/**
	 * 手机号
	 */
	@Column(name="del")
	private Boolean del = false;
}
