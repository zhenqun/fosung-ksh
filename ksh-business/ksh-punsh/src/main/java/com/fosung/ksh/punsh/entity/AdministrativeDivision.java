package com.fosung.ksh.punsh.entity;

import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 电子地图实体对象
 */
@Entity
@Table(name="administrative_division")
@Setter
@Getter
public class AdministrativeDivision extends AppJpaBaseEntity {

	/**
	 * 城市代码
	 */
	@Column(name="city_code")
	private String cityCode ;


	/**
	 * 城市名称
	 */
	@Column(name="city_name")
	private String cityName ;


	/**
	 * 父级城市代码
	 */
	@Column(name="parent_code")
	private String parentCode ;


	/**
	 * 合并城市名称
	 */
	@Column(name="merger_name")
	private String mergerName ;


	/**
	 * 级别（1省；2市；3县；4镇；5村；））
	 */
	@Column(name="depth")
	private Integer depth ;


	/**
	 * 经度
	 */
	@Column(name="longitude")
	private String longitude ;


	/**
	 * 维度
	 */
	@Column(name="latitude")
	private String latitude ;


	/**
	 * 是否使用
	 */
	@Column(name="is_use")
	private Boolean isUse ;


	/**
	 * 海康的设备id
	 */
	@Transient
	private String cameraIndexCode;
}