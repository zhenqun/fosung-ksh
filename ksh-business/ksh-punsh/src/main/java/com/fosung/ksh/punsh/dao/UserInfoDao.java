package com.fosung.ksh.punsh.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.framework.dao.jpa.annotation.MybatisQuery;

import com.fosung.ksh.punsh.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author lqyu
 * @date 2019-4-1 17:34
 */
public interface UserInfoDao extends AppJPABaseDao<UserInfo,Long> {
    @MybatisQuery
    UserInfo findUserInfoByPin(@Param("s") String s, @Param("sn") String sn);

    @MybatisQuery
    void updateUser(@Param("pin") String pin,
                    @Param("card") String card,
                    @Param("grp") String grp,
                    @Param("name") String name,
                    @Param("password") String password,
                    @Param("time") Date time,
                    @Param("sn") String sn);
}
