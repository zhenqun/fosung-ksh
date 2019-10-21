package com.fosung.ksh.meeting.entity;


import com.fosung.framework.common.support.dao.entity.AppJpaIdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统与好视通进行同步的党组织根节点
 * 建议根节点为县级数据
 * 如果有新增的地市需要上可视化系统，可直接在此处添加对应的党组织
 */
@Data
@Entity
@Table(name = "meeting_root_org")
public class MeetingRootOrg extends AppJpaIdEntity {

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
}
