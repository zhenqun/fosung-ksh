package com.fosung.ksh.punsh.entity;

import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lqyu
 * @date 2019-4-1 11:37
 */
@Entity
@Table(name="user_info")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo extends AppJpaBaseEntity {

    /**
     *序列号
     */
    private String sn;

    /**
     *工号
     */
    private String pin;

    /**
     *姓名
     */
    private String name;

    /**
     *密码
     */
    private String password;

    /**
     *
     */
    private String pri;

    /**
     *卡号
     */
    private String card;

    /**
     *
     */
    private String grp;

    /**
     *用户使用的时间段编号信息
     */
    private String tz;



}
