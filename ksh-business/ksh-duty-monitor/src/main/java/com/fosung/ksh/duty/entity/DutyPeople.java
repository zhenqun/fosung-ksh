package com.fosung.ksh.duty.entity;

import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.framework.common.support.dao.entity.AppJpaSoftDelEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 值值班人员
 */
@Entity
@Table(name = "duty_people")
@Setter
@Getter
public class DutyPeople extends AppJpaBaseEntity implements AppJpaSoftDelEntity {


    /**
     * 签到的村
     */
    @Column(name = "village_id")
    private Long villageId;

    /**
     * 值班人姓名
     */
    @Column(name = "people_name")
    private String peopleName;

    /**
     * 值班人电话
     */
    @Column(name = "phone_num")
    private String phoneNum;

    /**
     * 对应海康人脸库照片url
     */
    @Column(name = "face_pic_url")
    private String facePicUrl;

    /**
     * 身份证号,18位，名称与简项库保持一致
     */
    @Column(name = "id_card")
    private String idCard;

    /**
     * 海康人脸库ID,暂时废弃
     */
    @Column(name = "human_id")
    private String humanId;


    /**
     * 删除标志
     */
    @Column(name = "del")
    private Boolean del = false;

    /**
     * 村级的城市名称
     */
    @Transient
    private String cityName;
    /**
     * 城镇编码
     */
    @Transient
    private String townCode;
    /**
     * 城镇名称
     */
    @Transient
    private String townName;
}
