package com.fosung.ksh.sys.service.impl;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.sys.dao.SysDtUserDao;
import com.fosung.ksh.sys.entity.SysDtUser;
import com.fosung.ksh.sys.service.SysDtUserService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 灯塔用户业务类
 */
@Slf4j
@Service
public class SysDtUserServiceImpl extends AppJPABaseDataServiceImpl<SysDtUser, SysDtUserDao> implements SysDtUserService {

    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            this.put("id", "id:EQ");
            this.put("orgId", "orgId:EQ");
            this.put("orgCode", "orgCode:RLIKE");
            this.put("isUse", "isUse:EQ");
            this.put("createDatetime", "createDatetime:GT");
            this.put("lastUpdateDatetime", "lastUpdateDatetime:GT");
        }
    };

    public Map<String, String> getQueryExpressions() {
        return this.expressionMap;
    }

    /**
     * 同步前批量禁用
     *
     * @param date
     */
    @Override
    public void batchCloseUse(List<String> userIdList, Date date) {
        this.entityDao.batchCloseUse(userIdList, date);
    }


    /**
     * 查询目录下当前时间以后同步的数据
     *
     * @param orgId
     * @param date
     * @return
     */
    public List<SysDtUser> queryUpdateList(String orgId, Date date) {
        Map<String, Object> searchParams = Maps.newHashMap();
        searchParams.put("orgId", orgId);
        searchParams.put("lastUpdateDatetime", date);
        List<SysDtUser> userList = queryAll(searchParams);
        return userList;
    }

    /**
     * 根据用户ID获取用户详情
     *
     * @param userId@return
     */
    @Override
    public SysDtUser getSysDtUser(String userId) {
        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("userId", userId);
        List<SysDtUser> sysDtUserList = this.queryAll(searchParam);
        if (UtilCollection.isNotEmpty(sysDtUserList)) {
            return sysDtUserList.get(0);
        }
        return null;
    }

}
