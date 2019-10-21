package com.fosung.ksh.organization.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.ksh.organization.entity.OrgLifePeople;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author wangyh
 */
public interface OrgLifePeopleDao extends AppJPABaseDao<OrgLifePeople, Long> {

    /**
     * 根据组织生活id，删除所有数据
     *
     * @param orgLifeId 组织生活
     * @return 删除数据量
     */
    @Query("update OrgLifePeople set del = true where orgLifeId = ?1 ")
    @Modifying
    public int deleteByOrgLifeId(Long orgLifeId);

    /**
     * 查询当前用户是否存在
     * @param orgLifeId
     * @param userHash
     * @return
     */
    @Query("from OrgLifePeople where orgLifeId = ?1 and  personnelHash = ?2")
    public OrgLifePeople get(Long orgLifeId,String userHash);

}