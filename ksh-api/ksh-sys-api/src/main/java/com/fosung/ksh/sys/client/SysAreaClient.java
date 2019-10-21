package com.fosung.ksh.sys.client;


import com.fosung.ksh.sys.dto.SysArea;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author LZ
 * @Description: 行政机构client
 * @date 2019-05-09 19:54
 */
@FeignClient(name = "ksh-sys-web", path = "/area")
public interface SysAreaClient {

    /**
     * 根据cityCode获取行政机构详情
     *
     * @param areaCode
     * @return
     */
    @RequestMapping(value = "/get-code", method = RequestMethod.POST)
    SysArea getAreaInfo(@RequestParam("areaCode") String areaCode);


    /**
     * 根据cityCode获取行政机构详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    SysArea getAreaInfo(@RequestParam("id") Long id);

    /**
     * @Description: 根据行政机构Id获取下级行政机构
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    List<SysArea> queryAreaList(@RequestParam("id") Long id, @RequestParam(required = false, name = "cityName") String cityName);

    /**
     * 根据行政编码 查询村级行政机构
     *
     * @param id 行政编码
     * @return
     */
    @RequestMapping(value = "/branch", method = RequestMethod.POST)
    List<SysArea> queryBranchList(@RequestParam("id") Long id);

    /**
     * 根据cityCode，获取所有下级党组织ID
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "all-id-list", method = RequestMethod.POST)
    List<Long> queryAreaAllChildId(@RequestParam("id") Long id);
}
