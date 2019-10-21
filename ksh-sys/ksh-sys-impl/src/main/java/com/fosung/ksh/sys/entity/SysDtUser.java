package com.fosung.ksh.sys.entity;

import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Objects;


/**
 * 灯塔用户
 */
@Entity
@Table(name = "sys_dt_user", indexes = {
        @Index(columnList = "org_id"),
        @Index(columnList = "org_code"),
        @Index(columnList = "hash")
})
@Getter
@Setter
public class SysDtUser extends AppJpaBaseEntity {

    /**
     * 组织ID
     */
    @Column(name = "org_id")
    private String orgId;


    /**
     * 组织编码
     */
    @Column(name = "org_code")
    private String orgCode;

    /**
     * 组织名称
     */
    @Column(name = "org_name")
    private String orgName;


    /**
     * 用户HASH
     */
    @Column(name = "hash")
    private String hash;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;


    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 手机号 脱敏后
     */
    @Column(name = "telephone")
    private String telephone;


    /**
     * 身份证号 脱敏后
     */
    @Column(name = "id_card")
    private String idCard;

    /**
     * 用户名
     */
    @Column(name = "real_name")
    private String realName;

    @Column(name = "is_use")
    private Boolean isUse;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysDtUser appDTUser = (SysDtUser) o;
        return orgId.equals(appDTUser.orgId) &&
                orgCode.equals(appDTUser.orgCode) &&
                orgName.equals(appDTUser.orgName) &&
                idCard.equals(appDTUser.getIdCard()) &&
                hash.equals(appDTUser.hash) &&
                userName.equals(appDTUser.userName) &&
                (
                        (telephone == null && appDTUser.getTelephone() == null) ||
                                (telephone != null && telephone.equals(appDTUser.telephone))
                ) &&
                realName.equals(appDTUser.realName);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orgId, orgCode, orgName, hash, userName, telephone, realName);
    }
}
