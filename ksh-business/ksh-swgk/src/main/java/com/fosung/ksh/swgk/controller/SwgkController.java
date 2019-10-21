package com.fosung.ksh.swgk.controller;

import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.ksh.swgk.service.SwgkService;
import com.fosung.ksh.swgk.vo.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * @program: fosung-ksh
 * @description: 三务公开相关接口调用
 * @author: LZ
 * @create: 2019-08-09 14:02
 **/
@Slf4j
@RequestMapping(SwgkController.BASE_URL)
@RestController
public class SwgkController extends AppIBaseController {
    public static final String BASE_URL = "/swgk";

    @Autowired
    private SwgkService swgkService;

    @RequestMapping("/query/articles")
    public AcceptVo queryArticles(@RequestParam("orgId") String orgId,// 组织id
                                  @RequestParam(value = "type") ArticleType type, // 查询类型
                                  @RequestParam(required = false, value = "title") String title, // 标题模糊搜索
                                  @RequestParam(required = false, value = "cateId") String cateId, // 三务中的分类
                                  @RequestParam(required = false, value = "pagenum", defaultValue = "1") int pageNum,
                                  @RequestParam(required = false, value = "pagesize", defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) {


        Assert.hasText(orgId,"user orgId must  not null！");
        try{
            return  swgkService.queryArticles(orgId, type, title, cateId, pageNum, pageSize);
        }catch (Exception e){
            throw new AppException("查询异常");
        }
    }

}
