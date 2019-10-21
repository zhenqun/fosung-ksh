package com.fosung.ksh.meeting.entity;


import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 党员同步到好视通中的结果
 * 该表会在每天临晨3点定时同步
 */
@Data
@Entity
@Table(name = "sync_user")
public class MeetingUserSync extends AppJpaBaseEntity {


    /**
     * 人员HASH
     */
    @Column(name = "user_hash")
    private String userHash;

    /**
     * 昵称（支持空格（纯空格）、中文字符（全角），数字、字母、特殊字符;昵称为空时，则昵称与用户名相同）
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 昵称（支持空格（纯空格）、中文字符（全角），数字、字母、特殊字符;昵称为空时，则昵称与用户名相同）
     */
    @Column(name = "org_id")
    private String orgId;

}
