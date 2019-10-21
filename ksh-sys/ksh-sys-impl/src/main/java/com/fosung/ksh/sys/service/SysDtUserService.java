package com.fosung.ksh.sys.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.sys.entity.SysDtUser;

import java.util.Date;
import java.util.List;

public interface SysDtUserService extends AppBaseDataService<SysDtUser, Long> {

    /**
     * 同步前批量禁用
     *
     * @param orgId
     * @param date
     */
    public void batchCloseUse(List<String> userIdList, Date date);


    /**
     * 根据组织机构id查询详情
     *
     * @return
     */
    SysDtUser getSysDtUser(String userId);

    /**
     * 查询目录下当前时间以后同步的数据
     *
     * @param orgId
     * @param date
     * @return
     */
    public List<SysDtUser> queryUpdateList(String orgId, Date date);

}
