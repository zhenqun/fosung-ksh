package com.fosung.ksh.duty.service.impl;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.common.util.UtilBase64;
import com.fosung.ksh.duty.config.DutyProperties;
import com.fosung.ksh.duty.dao.DutyPeopleDao;
import com.fosung.ksh.duty.entity.DutyPeople;
import com.fosung.ksh.duty.service.DutyPeopleService;
import com.fosung.ksh.monitor.client.LibsPeopleClient;
import com.fosung.ksh.monitor.dto.PersonInfo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.Transient;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DutyPeopleServiceImpl extends AppJPABaseDataServiceImpl<DutyPeople, DutyPeopleDao>
        implements DutyPeopleService {


    @Autowired
    private LibsPeopleClient libsPeopleClient;

    @Autowired
    private DutyProperties dutyProperties;
    /**
     * 文件临时存储路径
     */
    private static final String TEMP_PATH = "temp";


    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("idCard", "idCard:EQ");
            put("idCardLike", "idCard:LIKE");
            put("humanId", "humanId:LLIKE");
            put("villageIdList", "villageId:IN");
            put("villageId", "villageId:EQ");
            put("peopleName", "peopleName:LIKE");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }


    @Transient
    @Override
    public void delete(DutyPeople dutyPeople) {
        if (UtilString.isNotEmpty(dutyPeople.getHumanId())) {

            List<PersonInfo> personInfos = libsPeopleClient.getPersonInfo(dutyPeople.getIdCard(), dutyProperties.getLibId());
            // 如果该身份证号下面有多个人脸需要全部删除
            if (UtilCollection.isNotEmpty(personInfos)) {
                personInfos.forEach(personInfo1 -> {
                    if (UtilString.isNotEmpty(personInfo1.getHumanId())) {
                        libsPeopleClient.deleteBlackPerson(personInfo1.getHumanId());
                        log.debug("删除{}海康人脸身份证号{}，人脸id为{}", dutyPeople.getPeopleName(), dutyPeople.getIdCard(), dutyPeople.getHumanId());
                    }
                });
            }
            super.delete(dutyPeople);
        }
    }

    /**
     * 人脸采集
     *
     * @param
     */
    public DutyPeople collect(DutyPeople dutyPeople) {
        Assert.isTrue(UtilString.isIdCard(dutyPeople.getIdCard()), "身份证号错误");

        DutyPeople people = getByIdCard(dutyPeople.getIdCard());
        // 如果根据身份证号，没有获取到，就根据主键获取
        if (dutyPeople.getId() != null && people == null) {
            people = get(dutyPeople.getId());
        }
        Assert.notNull(people, "该身份证号对应的值班人员不存在，请检查身份证号是否正确或先添加值班人员");

        if (UtilString.isNotEmpty(dutyPeople.getFacePicUrl())){
            String base64Image = UtilBase64.ImageToBase64ByOnline(dutyPeople.getFacePicUrl());

            PersonInfo personInfo = new PersonInfo();
            personInfo.setHumanName(UtilString.isNotEmpty(dutyPeople.getPeopleName())?dutyPeople.getPeopleName():people.getPeopleName());
            personInfo.setCredentialsNum(dutyPeople.getIdCard());
            personInfo.setListLibId(dutyProperties.getLibId());
            personInfo.setPicBase64(base64Image);
            // 如果已经上传过，humid是不为空的
            if (UtilString.isEmpty(people.getHumanId())) {
                List<PersonInfo> personInfos = libsPeopleClient.getPersonInfo(personInfo.getCredentialsNum(), dutyProperties.getLibId());
                // 如果人脸库已经存在，说明通过其它途径进行了添加了人脸，需要进行删除
                if (UtilCollection.isNotEmpty(personInfos)) {
                    personInfos.forEach(personInfo1 -> {
                        if (UtilString.isNotEmpty(personInfo1.getHumanId())) {
                            libsPeopleClient.deleteBlackPerson(personInfo1.getHumanId());
                        }
                    });
                }

                personInfo = libsPeopleClient.addPersonInfo(personInfo);
            } else {
                personInfo.setHumanId(people.getHumanId());
                personInfo = libsPeopleClient.editPersonInfo(personInfo);
            }
            people.setHumanId(personInfo.getHumanId());
            people.setFacePicUrl(dutyPeople.getFacePicUrl());
        }

        people.setPeopleName(UtilString.isNotEmpty(dutyPeople.getPeopleName())?dutyPeople.getPeopleName():people.getPeopleName());
        people.setIdCard(dutyPeople.getIdCard());
        update(people, Sets.newLinkedHashSet("humanId", "facePicUrl","peopleName","idCard"));
        return dutyPeople;
    }

    /**
     * 根据身份证号获取人员数据
     *
     * @param idCard
     * @return
     */
    public DutyPeople getByIdCard(String idCard) {
        Map<String, Object> searchParam = Maps.newHashMap("idCard", idCard.toUpperCase());
        List<DutyPeople> list = queryAll(searchParam);
        if (UtilCollection.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void preSaveHandler(DutyPeople entity) {
        Assert.hasText(entity.getIdCard(), "添加人员:" + entity.getPeopleName() + "身份证号不能为空");
        Assert.isTrue(getByIdCard(entity.getIdCard()) == null, "添加人员:" + entity.getPeopleName() + "身份证号重复：" + entity.getIdCard());
        super.preSaveHandler(entity);
    }

    /**
     * 根据身份证号获取人员数据
     *
     * @param idCard
     * @return
     */
    public DutyPeople getByHumanId(String humanId) {
        Map<String, Object> searchParam = Maps.newHashMap("humanId", humanId);
        List<DutyPeople> list = queryAll(searchParam);
        if (UtilCollection.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

}