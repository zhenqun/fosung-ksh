package com.fosung.ksh.duty.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.duty.entity.DutyPeople;

public interface DutyPeopleService extends AppBaseDataService<DutyPeople,Long> {
    /**
     * 根据身份证号获取人员数据
     * @param idCard
     * @return
     */
    public DutyPeople getByIdCard(String idCard);

    /**
     * 根据身份证号获取人员数据
     *
     * @param idCard
     * @return
     */
    public DutyPeople getByHumanId(String humanId);
    /**
     * 人脸采集
     *
     * @param sysFace
     */
    public DutyPeople collect(DutyPeople dutyPeople);
}
