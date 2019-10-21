package com.fosung.ksh.meeting.entity;


import com.fosung.framework.common.support.dao.entity.AppJpaIdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统在初始化时往好视通同步的党组织数据
 * 默认是以山东省为根节点往好视通同步数据
 */
@Data
@Deprecated
@Entity
@Table(name = "meeting_init_org")
public class MeetingInitOrg extends AppJpaIdEntity {

    /**
     * 灯塔党组织ID
     */
    @Column(name = "org_id")
    private String orgId;


    /**
     * 灯塔党组织ID
     */
    @Column(name = "org_code")
    private String orgCode;
    /**
     * 党组织名称
     */
    @Column(name = "org_name")
    private String orgName;

    /**
     * 父级节点
     */
    @Column(name = "parent_id")
    private String parentId;

    @Column(name= "dept_id")
    private Integer deptId;
}
