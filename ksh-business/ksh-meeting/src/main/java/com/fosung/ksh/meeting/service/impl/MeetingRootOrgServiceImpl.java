package com.fosung.ksh.meeting.service.impl;

import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.meeting.dao.MeetingRootOrgDao;
import com.fosung.ksh.meeting.entity.MeetingRootOrg;
import com.fosung.ksh.meeting.service.MeetingRootOrgService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MeetingRootOrgServiceImpl extends AppJPABaseDataServiceImpl<MeetingRootOrg, MeetingRootOrgDao>
        implements MeetingRootOrgService {

    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {

    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return this.expressionMap;
    }

    public List<String> getRootOrgIdList() {
        List<MeetingRootOrg> list = queryAll(Maps.newHashMap());
        List<String> rootOrgIdList = list.stream().map(MeetingRootOrg::getOrgId).collect(Collectors.toList());
        return rootOrgIdList;
    }
}
