package com.fosung.ksh.swgk.service;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilString;
import com.fosung.ksh.swgk.vo.*;
import com.fosung.ksh.swgk.client.SwgkClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @program: fosung-ksh
 * @description: 三务公开服务类
 * @author: LZ
 * @create: 2019-08-09 14:06
 **/
@Slf4j
@Service
public class SwgkService {
    @Autowired
    private SwgkClient swgkClient;

    /**
     * @param orgId  组织id
     * @Description: 文章分类列表
     * @author LZ
     * @date 2019-08-09 15:00
     */
    public AcceptVo queryCates(String orgId, String type, String title, String cateId, Integer pageNum, Integer pageSize){
       if (UtilString.isNotEmpty(cateId)){
           AcceptVo acceptVo = swgkClient.queryArticles(orgId,type, title,cateId, pageNum, pageSize);
           // 狗屎接口，两个字段返回的同一对象，却用不同的名称
           if (UtilCollection.isNotEmpty(acceptVo.getArticleList())&&UtilCollection.isEmpty(acceptVo.getArtList())){
                acceptVo.setArtList(acceptVo.getArticleList());
           }
           return acceptVo;
       }
        return swgkClient.queryCates(orgId, type, pageNum, pageSize);
    }

    public AcceptVo queryArticles(String orgId, ArticleType type, String title, String cateId, Integer pageNum, Integer pageSize) throws Exception {
        switch (type) {
            case NEWPUBLISH:
                log.info("最新公示信息");
                return swgkClient.queryArticles(orgId, "article", title, cateId, pageNum, pageSize);
            case FINANCE:
                log.info("财务");
                return queryCates(orgId,  "caiwu",  title,  cateId,  pageNum,  pageSize);

            case VILLAGEAFFAIRS:
                log.info("村务");
                return queryCates(orgId,  "cunwu",  title,  cateId,  pageNum,  pageSize);

            case PARTYAFFAIRS:
                log.info("党务");
                return queryCates(orgId,  "dangwu",  title,  cateId,  pageNum,  pageSize);
            case CONVENIENCE:
                log.info("便民信息");
                return swgkClient.queryArticles(orgId, "Convenience", title, cateId, pageNum, pageSize);
        }
        throw new Exception("查询类型错误:" + type);
    }
}
