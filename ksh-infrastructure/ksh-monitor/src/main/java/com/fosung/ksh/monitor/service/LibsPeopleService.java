package com.fosung.ksh.monitor.service;

import com.fosung.ksh.monitor.dto.PersonInfo;

import java.util.List;

public interface LibsPeopleService {

    /**
     * 新增黑名单
     *
     * @param humanName 人员姓名
     * @param picBase64 图片base64编码
     * @param listLibId
     * @return
     */
    PersonInfo addPersonInfo(String humanName, String picBase64, String listLibId, String credentialsNum);

    /**
     * 修改黑明单信息
     *
     * @param humanId
     * @param picBase64
     * @param listLibId
     * @return
     */
    PersonInfo editPersonInfo(String humanId, String picBase64, String listLibId);

    /**
     * 查询黑名单信息
     *
     * @param credentialsNum
     * @param listLibIds
     * @return
     */
    List<PersonInfo> getPersonInfo(String credentialsNum, String listLibIds);

    /**
     * 查询黑名单信息
     *
     * @param credentialsNum
     * @param listLibIds
     * @return
     */
    List<PersonInfo> queryPersonInfo(String listLibIds);

    /**
     * 删除黑名单
     *
     * @param humanId
     * @return
     */
    void deleteBlackPerson(String humanId);


}
