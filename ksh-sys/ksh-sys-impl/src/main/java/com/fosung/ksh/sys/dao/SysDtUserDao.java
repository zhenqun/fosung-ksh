package com.fosung.ksh.sys.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.ksh.sys.entity.SysDtUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author wangyihua
 * @date 2019-06-07 14:12
 */
public interface SysDtUserDao extends AppJPABaseDao<SysDtUser, Long> {


    /**
     * 设置全部节点为未启用
     *
     * @param orgId
     */
    @Query("update SysDtUser set isUse = false,lastUpdateDatetime = ?2 where userId in ?1 ")
    @Modifying
    void batchCloseUse(List<String> userIdList, Date date);
}
