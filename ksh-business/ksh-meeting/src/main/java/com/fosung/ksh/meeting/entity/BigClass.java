package com.fosung.ksh.meeting.entity;

import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.framework.common.config.AppProperties;

/**
 * BigClass实体对象
 */
@Entity
@Table(name="big_class")
@Setter
@Getter
public class BigClass extends AppJpaBaseEntity {

	/**
	 * 标题
	 */
	@Column(name="title")
	private String title ;


	/**
	 * 所属党组织
	 */
	@Column(name="org_id")
	private String orgId ;


	/**
	 * 视频id
	 */
	@Column(name="vadioId")
	private String vadioId ;

	@Column(name="imageId")
	private String imageId ;

	@Column(name="org_name")
	private String orgName;

	@Column(name = "org_code")
	private String orgCode;

	@Transient
	private String userHash;

	@Transient
	private JSONArray files;

	@Transient
	private String createTime;

	@Transient
	private  JSONArray images;

}
