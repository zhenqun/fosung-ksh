package com.fosung.ksh.meeting.entity;


import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 党组织同步到好视通中的结果
 * 该表会在每天凌晨1点定时更新
 */
@Data
@Entity
@Table(name = "sync_org")
public class MeetingOrgSync extends AppJpaBaseEntity {

    /**
     * 灯塔党组织ID
     */
    @Column(name = "org_id")
    private String orgId;

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

    /**
     * 好视通单位ID
     */
    @Column(name = "dept_id")
    private Integer deptId;

}
