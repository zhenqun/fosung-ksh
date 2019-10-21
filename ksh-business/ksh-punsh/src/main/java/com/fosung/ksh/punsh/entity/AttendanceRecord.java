package com.fosung.ksh.punsh.entity;

import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @date 2019-3-29 16:00
 */
@Entity
@Table(name="attendance_record")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecord extends AppJpaBaseEntity {

    /**
     * 工号
     */
    private String pin;

    /**
     *打卡时间
     */
    private Date time;

    /**
     *状态
     */
    private String status;

    /**
     *
     */
    private String verify;

    /**
     *
     */
    @Column(name = "work_code")
    private String workCode;

    /**
     *
     */
    private String reserved;
    
    /**
     *验证方式
     * 1.指纹
     * 2.人脸
     * 3.密码
     */
    private Integer verification;

    /**
     *客户端的序列号
     */
    private String sn;


    /**
     * 打卡日期
     */
 /*   @Column(name = "open_date")
    private String openDate;*/


}
