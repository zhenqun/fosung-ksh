package com.fosung.ksh.organization.dao;

import com.fosung.framework.dao.config.mybatis.page.MybatisPage;
import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.framework.dao.jpa.annotation.MybatisQuery;
import com.fosung.ksh.organization.constant.PushStatus;
import com.fosung.ksh.organization.entity.OrgLife;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangyh
 */
public interface OrgLifeDao extends AppJPABaseDao<OrgLife, Long> {


    /**
     * 自定义组织生活分页查询
     * @param classificationId   组织生活类型
     * @param startDate 开始时间
     * @param endDate  结束时间
     * @param pushStatus  发布状态
     * @param meetingName  组织生活标题
     * @param pageRequest 分页
     * @return
     */
    @MybatisQuery
    public MybatisPage<OrgLife> queryPageList(
            @Param("classificationId") String classificationId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("pushStatus") String pushStatus,
            @Param("meetingName") String meetingName,
            @Param("branchIdList") List<String> branchIdList,
            Pageable pageRequest);

    @MybatisQuery
    public List<String> orgLifeIds(@Param("paramSearch")  Map<String,Object> paramSearch);
    @MybatisQuery
    public List<String> orgLifeReportIds(@Param("paramSearch")  Map<String,Object> paramSearch);
}
