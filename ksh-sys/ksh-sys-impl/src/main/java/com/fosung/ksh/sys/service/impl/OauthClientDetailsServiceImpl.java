package com.fosung.ksh.sys.service.impl;

import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.sys.dao.OauthClientDetailsDao;
import com.fosung.ksh.sys.entity.OauthClientDetails;
import com.fosung.ksh.sys.service.OauthClientDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wangyh
 */
@Slf4j
@Service
public class OauthClientDetailsServiceImpl extends AppJPABaseDataServiceImpl<OauthClientDetails, OauthClientDetailsDao>
        implements OauthClientDetailsService {


    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
        }
    };


    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }
}
