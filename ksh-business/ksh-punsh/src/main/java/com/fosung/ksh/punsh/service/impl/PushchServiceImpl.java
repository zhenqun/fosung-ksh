package com.fosung.ksh.punsh.service.impl;

import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;

import com.fosung.ksh.punsh.dao.PushchDao;
import com.fosung.ksh.punsh.entity.ClientInfo;
import com.fosung.ksh.punsh.service.PushchService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @date 2019-3-28 15:32
 */
@Service
public class PushchServiceImpl  extends AppJPABaseDataServiceImpl<ClientInfo,PushchDao> implements PushchService {

    @Autowired
    private PushchDao pushchDao;

    private  Map<String,String> map = Maps.newHashMap();
    @Override
    public Map<String, String> getQueryExpressions() {
        map.put("sn","sn:EQ");
        map.put("cityCodeList","orgCode:IN");
        return map;
    }


}
