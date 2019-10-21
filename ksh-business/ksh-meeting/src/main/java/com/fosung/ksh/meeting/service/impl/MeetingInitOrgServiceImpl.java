package com.fosung.ksh.meeting.service.impl;

import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.meeting.control.client.OrgClient;
import com.fosung.ksh.meeting.control.dto.org.AddOrgRequestDTO;
import com.fosung.ksh.meeting.dao.MeetingInitOrgDao;
import com.fosung.ksh.meeting.entity.MeetingInitOrg;
import com.fosung.ksh.meeting.service.MeetingInitOrgService;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MeetingInitOrgServiceImpl extends AppJPABaseDataServiceImpl<MeetingInitOrg, MeetingInitOrgDao>
        implements MeetingInitOrgService {

    @Autowired
    private OrgClient orgClient;


    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {

    };


    /**
     * 初始化好视通
     */
    public void sync() {
        List<MeetingInitOrg> list = queryAll(Maps.newHashMap());
        list = list.stream().sorted(Comparator.comparingInt(org -> org.getOrgCode().length())).collect(Collectors.toList());
        for (MeetingInitOrg meetingInitOrg : list) {
            Integer deptId = orgClient.getByOrgId(meetingInitOrg.getOrgId());
            // 如果未经初始化，则初始化好视通党组织数据
            if (deptId == null || deptId == 0) {
                deptId = orgClient.create(syncSysOrg2AddOrgRequestDTO(meetingInitOrg));
            }
            meetingInitOrg.setDeptId(deptId);
            update(meetingInitOrg, Sets.newHashSet("deptId"));

        }
    }

    @Override
    public Map<String, String> getQueryExpressions() {
        return this.expressionMap;
    }

    @SuppressWarnings("all")
    private AddOrgRequestDTO syncSysOrg2AddOrgRequestDTO(MeetingInitOrg syncSysOrg) {
        AddOrgRequestDTO addOrgRequestDTO = new AddOrgRequestDTO();

        addOrgRequestDTO.setAuthDepartID(syncSysOrg.getOrgId());
        addOrgRequestDTO.setAuthParentDepartID(syncSysOrg.getParentId());
        addOrgRequestDTO.setDepartName(syncSysOrg.getOrgName());
        String parentId = syncSysOrg.getParentId();
        if (!Strings.isNullOrEmpty(parentId)) {
            Integer parentDeptid = orgClient.getByOrgId(parentId);
            addOrgRequestDTO.setParentDepartID(parentDeptid == null ? 0 : parentDeptid);
        } else {
            addOrgRequestDTO.setParentDepartID(0);
        }
        return addOrgRequestDTO;
    }

}
