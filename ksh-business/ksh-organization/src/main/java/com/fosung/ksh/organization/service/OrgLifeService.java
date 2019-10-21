package com.fosung.ksh.organization.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.organization.constant.PushStatus;
import com.fosung.ksh.organization.entity.OrgLife;
import com.fosung.ksh.organization.ezb.dto.ClassificationDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface OrgLifeService extends AppBaseDataService<OrgLife,Long> {


    /**
     * 通过类型ID获取本月组织生活
     * @param classificationId 类型ID
     */
    List<OrgLife> findThisMouthOrgLifeByTypeId(String classificationId);

    /**
     * 获取组织生活类型
     * @param orgCode
     * @throws IOException
     */
    public List<ClassificationDTO> queryClassificationList(String orgCode) throws IOException;
    /**
     * 自定义组织生活分页查询
     *
     * @param classificationId 组织生活类型
     * @param startDate        开始时间
     * @param endDate          结束时间
     * @param pushStatus       发布状态
     * @param meetingName      组织生活标题
     * @param branchId         党支部ID
     * @param orgId            党组织ID
     * @param pageRequest      分页
     * @return
     */
    Page<OrgLife> queryPageList(
            String classificationId,
            Date startDate,
            Date endDate,
            PushStatus pushStatus,
            String meetingName,
            String orgId,
            int pageNum,
            int pageSize);

    /**
     * 获取组织生活详情
     *
     * @param id
     * @return
     */
    OrgLife getInfo(Long id);

    /**
     * 自定义保存
     *
     * @param orgLife
     */
    OrgLife customSave(OrgLife orgLife) throws Exception;

    /**
     * 结束组织生活，并上传附件
     *
     * @param orgLife
     * @return
     */
    public OrgLife finish(OrgLife orgLife);

    /**
     * 组织生活发布
     *
     * @param id
     */
    void push(Long id) throws InterruptedException, ExecutionException, IOException;


    public List<String> getOrgListIds( Map<String,Object> paramSearch);


}
