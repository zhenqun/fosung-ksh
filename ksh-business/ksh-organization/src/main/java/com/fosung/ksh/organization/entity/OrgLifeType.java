package com.fosung.ksh.organization.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 组织生活与类型关系实体对象
 * @author wangyh
 */
@Entity
@Table(name="org_life_type")
@Setter
@Getter
public class OrgLifeType extends AppJpaBaseEntity {


    /**
	 * 生活ID
	 */
	@Column(name="org_life_id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long orgLifeId ;


	/**
	 * 类型Id
	 */
	@Column(name="classification_id")
	private String classificationId ;


	/**
	 * name
	 */
	@Column(name="type_name")
	private String typeName ;


}
