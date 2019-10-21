package com.fosung.ksh.meeting.control.client;

import com.fosung.ksh.common.cache.RedisCacheable;
import com.fosung.ksh.meeting.control.dto.org.AddOrgRequestDTO;
import com.fosung.ksh.meeting.control.dto.org.EditOrgRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 好视通视频会议会议室模块接口
 *
 * @author wangyh
 */
@FeignClient(name = "ksh-meeting-control", path = "/org")
public interface OrgClient {

    /**
     * 新增会议室
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Integer create(@RequestBody AddOrgRequestDTO requestDto);


    /**
     * 修改
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public void edit(@RequestBody EditOrgRequestDTO editOrgRequestDTO);

    @RequestMapping(value = "/get-by-org-id", method = RequestMethod.POST)
    public Integer getByOrgId(@RequestParam String orgId);

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public List<Map<String, Object>> queyOrgList(@RequestParam("departId") Integer departId,
                                                 @RequestParam("departName") String departName,
                                                 @RequestParam("orgId") String orgId,
                                                 @RequestParam("currPage") Integer currPage,
                                                 @RequestParam("pageSize") Integer pageSize);
}
