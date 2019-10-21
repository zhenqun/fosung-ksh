package com.fosung.ksh.duty.service.impl;

import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.duty.dao.DutyScheduleDao;
import com.fosung.ksh.duty.entity.DutyPeople;
import com.fosung.ksh.duty.entity.DutySchedule;
import com.fosung.ksh.duty.service.DutyPeopleService;
import com.fosung.ksh.duty.service.DutyScheduleService;
import com.fosung.ksh.duty.vo.DutyScheduleVo;
import com.fosung.ksh.sys.client.SysAreaClient;
import com.fosung.ksh.sys.dto.SysArea;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @program: fosung-ksh
 * @description: 人员排班设置
 * @author: LZ
 * @create: 2019-05-14 15:27
 **/
@Slf4j
@Service
public class DutyScheduleServiceImpl extends AppJPABaseDataServiceImpl<DutySchedule, DutyScheduleDao> implements DutyScheduleService {
    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("villageId", "villageId:EQ");
            put("allVillageId", "villageId:IN");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }

    @Autowired
    private SysAreaClient sysAreaClient;

    @Autowired
    private DutyPeopleService dutyPeopleService;

    /**
     * 根据村级分组查询已经导入的值班列表
     *
     * @return
     */
    public List<DutyScheduleVo> queryGroupSchedul(Long areaId) {
        List<SysArea> areas = sysAreaClient.queryBranchList(areaId);
        Map<Long, SysArea> collect = areas.stream().collect(Collectors.toMap(SysArea::getAreaId, SysArea -> SysArea));

        HashMap<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("allVillageId", collect.keySet());
        List<DutySchedule> dutySchedulesExistence = queryAll(searchMap);
        Map<Long, List<DutySchedule>> groupCollect = dutySchedulesExistence.stream().collect(Collectors.groupingBy(DutySchedule::getVillageId));
        List<DutyScheduleVo> list = Lists.newArrayList();
        groupCollect.keySet().forEach(
                allVillageId -> {
                    SysArea sysArea = collect.get(allVillageId);
                    DutyScheduleVo scheduleVo = DutyScheduleVo.builder().areaId(sysArea.getAreaId())
                            .cityCode(sysArea.getAreaCode())
                            .cityName(sysArea.getAreaName())
                            .townCityName(sysArea.getParentName()).build();
                    list.add(scheduleVo);
                }
        );

        return list;
    }

    /**
     * 导入值班人员数据
     * 此处需要加入事物处理，保证原子性
     *
     * @param
     */
    @Override
    @Transactional
    public void importTemplate(List<DutySchedule> dutySchedules, Long areaId) {
        dutySchedules = dutySchedules.stream().filter(DutySchedule ->
                UtilString.isNotEmpty(DutySchedule.getCityName()) && UtilString.isNotEmpty(DutySchedule.getDutyPeopleName())
        ).collect(Collectors.toList());
        // 首先对整个镇下人员排班数据进行查询
        List<SysArea> areas = sysAreaClient.queryBranchList(areaId);
        List<Long> idList = areas.stream().map(SysArea::getAreaId).collect(Collectors.toList());

        HashMap<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("allVillageId", idList);
        List<DutySchedule> dutySchedulesExistence = queryAll(searchMap);
        log.info(String.format("areaId:%s,查询原来的数据数量：%s，需要导入的数量为：%s",areaId,dutySchedulesExistence.size(),dutySchedules.size()));
        if (UtilCollection.isNotEmpty(dutySchedulesExistence) && UtilCollection.isNotEmpty(dutySchedules)) {
            List<Long> collectIds = dutySchedulesExistence.stream().map(dutySchedule -> dutySchedule.getId()).collect(Collectors.toList());
            log.info(String.format("需要删除的数量：%s",collectIds.size()));
            // 不为空的话，进行全部删除
            delete(collectIds);
        }
        // 添加镇下的人员排班数据
        for (DutySchedule dutySchedule : dutySchedules) {
            Map<String, SysArea> cityNames = areas.stream().collect(Collectors.toMap(SysArea::getAreaName, a -> a, (k1, k2) -> k1));
            String cityName = dutySchedule.getCityName();
            SysArea sysArea = cityNames.get(cityName);
            if (null == sysArea) {
                log.info("导入表格实体对象：" + dutySchedule.toString());
                throw new AppException("《" + cityName + "》名称导入错误，请校验名称");
            }

            dutySchedule.setVillageId(sysArea.getAreaId());
            dutySchedule.setCityName(sysArea.getAreaName());

            if (UtilString.isEmpty(dutySchedule.getDutyPeopleName())) {
                throw new AppException("《" + cityName + "》下值班人员名称为空");
            }
            dutySchedule.setDutyPeopleName(formatStr(dutySchedule.getDutyPeopleName()));
            dutySchedule.setIdCard(formatStr(dutySchedule.getIdCard()));

            // 根据身份证号 查询值班人员是否已经存在
            String idCard = dutySchedule.getIdCard();
            DutyPeople dutyPeople = dutyPeopleService.getByIdCard(idCard);
            // 如果值班人员已经存在，则不进行操作
            if (dutyPeople == null) {
                // 如果不存在则进行添加值班人员
                dutyPeople = new DutyPeople();
                dutyPeople.setVillageId(sysArea.getAreaId());
                dutyPeople.setPeopleName(dutySchedule.getDutyPeopleName());
                dutyPeople.setPhoneNum(dutySchedule.getPhoneNum());
                dutyPeople.setIdCard(dutySchedule.getIdCard());
                dutyPeople = dutyPeopleService.save(dutyPeople);
            }
            dutySchedule.setDutyPeopleId(dutyPeople.getId());
            save(dutySchedule);
        }

    }

    /**
     * @Description: 去掉字符串中间的空格以及特殊字符
     * @author LZ
     * @date 2019-05-29 16:31
     */
    public String formatStr(String oldStr) {
        Pattern p = Pattern.compile("[`~☆★!@#$%^&*()+=|{}':;,\\[\\]》·.<>/?~！@#￥%……（）——+|{}【】‘；：”“’。，、？]");//去除特殊字符
        Matcher m = p.matcher(oldStr);
        return m.replaceAll("").trim().replace(" ", "").replace("\\", "");//将匹配的特殊字符转变为空
    }
}
