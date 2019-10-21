package com.fosung.ksh.organization.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.framework.common.config.AppProperties;

/**
 * sysOrgIngo实体对象
 */
@Entity
@Table(name="sys_orgInfo")
@Setter
@Getter
public class SysOrgInfo extends AppJpaBaseEntity {

	/**
	 * v
	 */
	@Column(name="org_name")
	private String orgName ;


	/**
	 * b
	 */
	@Column(name="org_id")
	private String orgId ;


	//使用设备参加组织生活数
	@Transient
	private String allNum;
	//应参加人数
	@Transient
	private String ycjNum;
	//实际参加人数
	@Transient
	private String scjNum;
	//参会率
	@Transient
	private String rate;

	@Transient
	private Boolean hasChildren;

	@Transient
	private String parentId;
}