package com.fosung.ksh.meeting.service.impl;

import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.meeting.control.client.OrgClient;
import com.fosung.ksh.meeting.control.dto.org.AddOrgRequestDTO;
import com.fosung.ksh.meeting.control.dto.org.EditOrgRequestDTO;
import com.fosung.ksh.meeting.dao.MeetingOrgSyncDao;
import com.fosung.ksh.meeting.entity.MeetingOrgSync;
import com.fosung.ksh.meeting.service.MeetingOrgSyncService;
import com.fosung.ksh.sys.dto.SysOrg;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MeetingOrgSyncServiceImpl extends AppJPABaseDataServiceImpl<MeetingOrgSync, MeetingOrgSyncDao>
        implements MeetingOrgSyncService {

    @Autowired
    private OrgClient orgClient;


    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("orgId", "orgId:EQ");
            put("orgIdList", "orgId:IN");
        }
    };


    /**
     * 同步数据到好视通
     */
    public void batchSync(List<SysOrg> rtList, String orgId) {
        List<MeetingOrgSync> addList = Lists.newArrayList();
        List<MeetingOrgSync> updateList = Lists.newArrayList();

        List<String> orgIdList = rtList.stream().map(SysOrg::getOrgId).collect(Collectors.toList());

        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("orgIdList", orgIdList);
        List<MeetingOrgSync> dbList = queryAll(searchParam);
        Map<String, MeetingOrgSync> syncMap = dbList.stream()
                .collect(Collectors.toMap(MeetingOrgSync::getOrgId, org -> org));

        // 获取新增用户
        addList = getAddList(rtList, syncMap);

        // 获取修改用户
        updateList = getUpdateList(rtList, syncMap);


        for (MeetingOrgSync orgSync : addList) {
            Integer deptId = orgClient.create(syncSysOrg2AddOrgRequestDTO(orgSync));
            orgSync.setDeptId(deptId);
            save(orgSync);

        }
        for (MeetingOrgSync orgSync : updateList) {
            orgClient.edit(syncSysOrg2EditOrgRequestDTO(orgSync));
            update(orgSync, Sets.newHashSet("orgName", "parentId"));
        }

        log.info("\n党组织同步结果：\n党组织：{}\n党组织总数：{}\n新增党组织：{}\n修改组织：{}", orgId, rtList.size(), addList.size(), updateList.size());
    }


    /**
     * 获取被修改的数据
     *
     * @param rtList
     * @param syncMap
     * @return
     */
    private List<MeetingOrgSync> getUpdateList(List<SysOrg> rtList, Map<String, MeetingOrgSync> syncMap) {
        return rtList.stream().filter(org -> {
            MeetingOrgSync meetingUserSync = syncMap.get(org.getOrgId());
            if (meetingUserSync == null) {
                return false;
            }
            return !(org.getOrgName().equals(meetingUserSync.getOrgName())
                    && (org.getParentId() == null || org.getParentId().equals(meetingUserSync.getParentId())));
        }).map(org -> {
            MeetingOrgSync meetingUserSync = syncMap.get(org.getOrgId());
            meetingUserSync.setOrgName(org.getOrgName());
            meetingUserSync.setParentId(org.getParentId());
            return meetingUserSync;
        }).collect(Collectors.toList());
    }

    /**
     * 获取新增的用户
     *
     * @param rtList
     * @param syncMap
     * @return
     */
    private List<MeetingOrgSync> getAddList(List<SysOrg> rtList, Map<String, MeetingOrgSync> syncMap) {
        List<MeetingOrgSync> addList;
        addList = rtList.stream().filter(org -> syncMap.get(org.getOrgId()) == null).map(org -> {
            MeetingOrgSync orgSync = new MeetingOrgSync();
            orgSync.setOrgId(org.getOrgId());
            orgSync.setOrgName(org.getOrgName());
            orgSync.setParentId(org.getParentId());
            return orgSync;

        }).collect(Collectors.toList());

        return addList;
    }


    @SuppressWarnings("all")
    private EditOrgRequestDTO syncSysOrg2EditOrgRequestDTO(MeetingOrgSync syncSysOrg) {
        EditOrgRequestDTO editOrgRequestDTO = new EditOrgRequestDTO();
        editOrgRequestDTO.setAuthDepartID(syncSysOrg.getOrgId());
        editOrgRequestDTO.setAuthParentDepartID(syncSysOrg.getParentId());
        editOrgRequestDTO.setDepartName(syncSysOrg.getOrgName().replace("（", "").replace("）", ""));
        editOrgRequestDTO.setDepartId(syncSysOrg.getDeptId());
        String parentId = syncSysOrg.getParentId();
        if (!Strings.isNullOrEmpty(parentId)) {
            Integer parentDeptid = orgClient.getByOrgId(parentId);
            editOrgRequestDTO.setParentDepartID(parentDeptid == null ? -1 : parentDeptid);
        } else {
            editOrgRequestDTO.setAuthParentDepartID("0");
            editOrgRequestDTO.setParentDepartID(-1);
        }
        return editOrgRequestDTO;
    }

    @SuppressWarnings("all")
    private AddOrgRequestDTO syncSysOrg2AddOrgRequestDTO(MeetingOrgSync syncSysOrg) {
        AddOrgRequestDTO addOrgRequestDTO = new AddOrgRequestDTO();

        addOrgRequestDTO.setAuthDepartID(syncSysOrg.getOrgId());
        addOrgRequestDTO.setAuthParentDepartID(syncSysOrg.getParentId());
        addOrgRequestDTO.setDepartName(syncSysOrg.getOrgName().replace("（", "").replace("）", ""));
        String parentId = syncSysOrg.getParentId();
        if (!Strings.isNullOrEmpty(parentId)) {
            Integer parentDeptid = orgClient.getByOrgId(parentId);
            addOrgRequestDTO.setParentDepartID(parentDeptid == null ? 0 : parentDeptid);
        } else {
            addOrgRequestDTO.setAuthParentDepartID("0");
            addOrgRequestDTO.setParentDepartID(0);
        }
        return addOrgRequestDTO;
    }

    @Override
    public Map<String, String> getQueryExpressions() {
        return this.expressionMap;
    }
}
