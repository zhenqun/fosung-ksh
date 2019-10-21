package com.fosung.ksh.duty.entity;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 人员排班
 * todo 人员排班暂时未和业务挂钩，只作为人员库的导入作用，后期根据需求可能会对此进行调整
 */
@Entity
@Table(name = "duty_schedule")
@Data
@ToString
public class DutySchedule extends AppJpaBaseEntity {


    /**
     * 人员所属的村
     */
    @Column(name = "village_id")
    private Long villageId;


    /**
     * 值班人
     */
    @Column(name = "duty_people_id")
    private Long dutyPeopleId;

    /**
     * 值班周期
     */
    @Excel(name = "值班周期（星期）")
    @Column(name = "cycle")
    private String cycle;


    /**
     * 电话号码
     */
    @Excel(name = "电话号码")
    @Transient
    private String phoneNum;


    /**
     * 身份证号
     */
    @Excel(name = "身份证号")
    @Transient
    private String idCard;


    @Excel(name = "村级名称")
    @Transient
    private String cityName;

    /**
     * 值班人
     */
    @Excel(name = "值班人姓名")
    @Transient
    private String dutyPeopleName;


}
