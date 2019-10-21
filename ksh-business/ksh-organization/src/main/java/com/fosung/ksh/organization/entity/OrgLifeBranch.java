package com.fosung.ksh.organization.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.framework.common.support.dao.entity.AppJpaSoftDelEntity;
import com.fosung.ksh.organization.constant.PushStatus;
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
@Table(name = "org_life_branch")
@Setter
@Getter
public class OrgLifeBranch extends AppJpaBaseEntity  {


    /**
     * 组织生活对应会议室ID
     */
    @ApiModelProperty("组织生活ID")
    @Column(name = "org_life_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orgLifeId;


    /**
     * 组织生活所属支部
     */
    @ApiModelProperty("创建当前组织生活的支部")
    @Column(name = "branch_id")
    private String branchId;

    /**
     * 支部CODE
     */
    @ApiModelProperty("创建当前组织生活的支部CODE")
    @Column(name = "branch_code")
    private String branchCode;

    /**
     * 支部会议所属支部
     */
    @ApiModelProperty("创建当前组织生活的支部名称")
    @Column(name = "branch_name")
    private String branchName;


    /**
     * 参会人数
     */
    @ApiModelProperty("应参会人数")
    @Transient
    private String meetingNum;


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
     * 人员列表
     * todo 将要扩展支持多个支部同时召开组织生活
     */
    @ApiModelProperty("人员列表")
    @Transient
    private List<OrgLifePeople> peoples = Lists.newArrayList();

}
