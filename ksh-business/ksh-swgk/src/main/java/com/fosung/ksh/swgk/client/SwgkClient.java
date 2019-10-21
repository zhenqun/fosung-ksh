package com.fosung.ksh.swgk.client;



import com.fosung.ksh.swgk.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static com.fosung.framework.web.http.AppIBaseController.DEFAULT_PAGE_SIZE_NUM;
import static com.fosung.framework.web.http.AppIBaseController.DEFAULT_PAGE_START_NUM;




/**
 * @author LZ
 * @Description: 三务公开调用接口
 * @date 2019-08-09 11:43
 */

@FeignClient(name = "sanwu",url = "swgk.dtdjzx.gov.cn", path = SwgkClient.BASE_URL)
public interface SwgkClient {

    String BASE_URL = "/sanwu_phone/externalLZ";

    /**
     *
     * @param orgId
     * @param swType (党务、村务、财务)
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/findCates")
    AcceptVo queryCates(@RequestParam("orgId") String orgId,
                        @RequestParam("swType") String swType,
                        @RequestParam(required = false, value = "pageNo", defaultValue = "1") int pageNo,
                        @RequestParam(required = false, value = "pageSize", defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize);


    @GetMapping("/findByParentID")
    AcceptVo queryCatesByParentID(@RequestParam("orgId") String orgId,
                                  @RequestParam(required = false, value = "title") String title, // 标题模糊搜索
                                  @RequestParam(required = false, value = "cateId") String cateId, // 类型id
                                  @RequestParam(required = false, value = "pageNo", defaultValue = "1") int pageNo,
                                  @RequestParam(required = false, value = "pageSize", defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize);

    /**
     * 查询相关公布信息
     * @param orgId 组织id
     * @param type 类型（最新公示/便民信息）
     * @param title 模糊搜索标题
     * @param cateId 分类id
     * @param pageNo 分页
     * @param pageSize 每页数量
     * @return
     */
    @GetMapping("/selectArtList")
    AcceptVo queryArticles(@RequestParam("orgId") String orgId,
                           @RequestParam(value = "swType") String swType, // 查询类型
                           @RequestParam(required = false, value = "title") String title, // 标题模糊搜索
                           @RequestParam(required = false, value = "cateId") String cateId, // 类型id
                           @RequestParam(required = false, value = "pageNo", defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNo,
                           @RequestParam(required = false, value = "pageSize", defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize);

}
