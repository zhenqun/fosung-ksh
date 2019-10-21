package com.fosung.ksh.monitor.dao;

import com.fosung.framework.dao.config.mybatis.page.MybatisPage;
import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.framework.dao.jpa.annotation.MybatisQuery;
import com.fosung.ksh.monitor.entity.MonitorCamera;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MonitorCameraDao extends AppJPABaseDao<MonitorCamera, Long> {

    /**
     * 查询设备信息
     *
     * @param areaIdList
     * @return
     */
    @MybatisQuery
    public List<MonitorCamera> queryList(@Param("areaIdList") List<Long> areaIdList);

    /**
     * 查询设备信息
     *
     * @param areaIdList
     * @return
     */
    @MybatisQuery
    public MybatisPage<MonitorCamera> queryPageList(@Param("areaIdList") List<Long> areaIdList,
                                                    @Param("monitorStatus") String monitorStatus,
                                                    Pageable pageRequest);


}
