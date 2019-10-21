package com.fosung.ksh.organization.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.ksh.organization.entity.OrgLifeType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OrgLifeTypeDao extends AppJPABaseDao<OrgLifeType, Long> {
    /**
     * 根据组织生活id，删除所有数据
     *
     * @param orgLifeId 组织生活
     * @return 删除数据量
     */
    @Query("delete from OrgLifeType  where orgLifeId = ?1 ")
    @Modifying
    public int deleteByOrgLifeId(Long orgLifeId);
}