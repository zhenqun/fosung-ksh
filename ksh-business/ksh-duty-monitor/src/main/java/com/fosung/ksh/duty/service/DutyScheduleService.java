package com.fosung.ksh.duty.service;


import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.duty.entity.DutySchedule;
import com.fosung.ksh.duty.vo.DutyScheduleVo;

import java.util.List;

/**
 * @author LZ
 * @Description:人员排班设置
 * @date 2019-05-14 15:26
 */
public interface DutyScheduleService extends AppBaseDataService<DutySchedule, Long> {
    /**
     * 导入值班列表
     *
     * @param dutyPeoples
     * @param areaId
     */
    void importTemplate(List<DutySchedule> dutyPeoples, Long areaId);

    /**
     * 根据村进行分组查询已经导入的值班列表
     *
     * @return
     */
    public List<DutyScheduleVo> queryGroupSchedul(Long areaId);
}
