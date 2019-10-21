package com.fosung.ksh.organization.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.ksh.organization.ezb.constant.AttachmentType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 会议记录文件实体对象
 * @author wangyh
 */
@Entity
@Table(name="org_life_attachment")
@Setter
@Getter
public class OrgLifeAttachment extends AppJpaBaseEntity {

    /**
	 * 会议记录ID
	 */
	@ApiModelProperty("组织生活ID")
	@Column(name="org_life_id")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long orgLifeId ;


	/**
	 * 名称
	 */
	@ApiModelProperty("附件名称")
	@Column(name="attachment_real_name")
	private String attachmentRealName ;


	/**
	 * 附件下载地址
	 */
	@ApiModelProperty("附件地址")
	@Column(name="download_path")
	private String downloadPath ;


	/**
	 * 文件类型0
	 */
	@ApiModelProperty("附件类型")
	@Enumerated(EnumType.STRING)
	@Column(name="attachment_type")
	private AttachmentType attachmentType ;

}
