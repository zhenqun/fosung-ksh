package com.fosung.ksh.organization.service.impl;

import com.fosung.framework.common.util.UtilDate;
import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.dao.config.mybatis.annotation.MybatisQueryPlugin;
import com.fosung.framework.dao.config.mybatis.page.MybatisPageRequest;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.common.util.DateUtil;
import com.fosung.ksh.common.util.UtilBean;
import com.fosung.ksh.meeting.client.MeetingRoomClient;
import com.fosung.ksh.meeting.client.MeetingRoomPersonClient;
import com.fosung.ksh.meeting.constant.MeetingType;
import com.fosung.ksh.meeting.constant.RoomType;
import com.fosung.ksh.meeting.constant.UserRight;
import com.fosung.ksh.meeting.constant.UserType;
import com.fosung.ksh.meeting.dto.MeetingRoom;
import com.fosung.ksh.meeting.dto.MeetingRoomPerson;
import com.fosung.ksh.organization.constant.OrgLifeStatus;
import com.fosung.ksh.organization.constant.PersonnelType;
import com.fosung.ksh.organization.constant.PushStatus;
import com.fosung.ksh.organization.constant.Sign;
import com.fosung.ksh.organization.dao.OrgLifeDao;
import com.fosung.ksh.organization.entity.OrgLife;
import com.fosung.ksh.organization.entity.OrgLifeAttachment;
import com.fosung.ksh.organization.entity.OrgLifeBranch;
import com.fosung.ksh.organization.entity.OrgLifePeople;
import com.fosung.ksh.organization.entity.OrgLifeType;
import com.fosung.ksh.organization.ezb.constant.AttachmentType;
import com.fosung.ksh.organization.ezb.dto.AttachmentDTO;
import com.fosung.ksh.organization.ezb.dto.ClassificationDTO;
import com.fosung.ksh.organization.ezb.dto.MeetingClassInficationDTO;
import com.fosung.ksh.organization.ezb.dto.MeetingPersonDTO;
import com.fosung.ksh.organization.ezb.dto.OrgLifeMeetingDTO;
import com.fosung.ksh.organization.ezb.service.EzbService;
import com.fosung.ksh.organization.service.*;
import com.fosung.ksh.organization.service.OrgLifeAttachmentService;
import com.fosung.ksh.organization.service.OrgLifeBranchService;
import com.fosung.ksh.organization.service.OrgLifePeopleService;
import com.fosung.ksh.organization.service.OrgLifeService;
import com.fosung.ksh.organization.service.OrgLifeTypeService;
import com.fosung.ksh.sys.client.SysOrgClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringEscapeUtils;
import org.assertj.core.util.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class OrgLifeServiceImpl extends AppJPABaseDataServiceImpl<OrgLife, OrgLifeDao>
        implements OrgLifeService {
    @Autowired
    private OrgLifeAttachmentService attachmentService;

    @Autowired
    private OrgLifePeopleService peopleService;

    @Autowired
    private OrgLifeTypeService typeService;

    @Autowired
    private OrgLifeBranchService branchService;

    @Autowired
    private EzbService ezbService;

    @Autowired
    private MeetingRoomClient meetingRoomClient;

    @Autowired
    private MeetingRoomPersonClient meetingRoomPersonClient;

    @Autowired
    private SysOrgClient sysOrgClient;

    // 忽略不显示的组织生活类型
    private final static Set<String> ignoreClassificationIds = Sets.newHashSet("1");

    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("idIN", "id:IN");

            put("orgLifeId", "orgLifeId:EQ");
            put("orgLifeStatus", "orgLifeStatus:EQ");
            put("endDateGT", "endDate:GTDATE");
            put("endDateLT", "endDate:LTDATE");

        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }


    @Override
    public List<OrgLife> findThisMouthOrgLifeByTypeId(String classificationId) {
        // 获取指定类型的组织生活的ID
        Map<String, Object> param = com.google.common.collect.Maps.newHashMap();
        param.put("classificationId", classificationId);
        List<OrgLifeType> orgLifeTypeList = typeService.queryAll(param);
        Set<Long> orgLifeIdSet = orgLifeTypeList.stream().map(OrgLifeType::getOrgLifeId).collect(Collectors.toSet());


        Map<String, Object> param2 = com.google.common.collect.Maps.newHashMap();
        param2.put("idIN", orgLifeIdSet);
        param2.put("endDateGT", DateUtil.getMouthFirst());
        param2.put("endDateLT", DateUtil.getMonthLastDay());
        return super.queryAll(param2);
    }


    /**
     * 自定义组织生活分页查询
     *
     * @param classificationId 组织生活类型
     * @param startDate        开始时间
     * @param endDate          结束时间
     * @param pushStatus       发布状态
     * @param meetingName      组织生活标题
     * @param orgId            党组织ID
     * @return
     */
    @MybatisQueryPlugin
    @Override
    public Page<OrgLife> queryPageList(
            String classificationId,
            Date startDate,
            Date endDate,
            PushStatus pushStatus,
            String meetingName,
            String orgId,
            int pageNum,
            int pageSize) {
        List<String> branchIdList = sysOrgClient.queryOrgAllChildId(orgId);

        Page<OrgLife> page = this.entityDao.queryPageList(classificationId,
                startDate,
                endDate,
                pushStatus == null ? null : pushStatus.name(),
                meetingName,
                branchIdList,
                MybatisPageRequest.of(pageNum, pageSize));
        page.getContent().stream().forEach(orgLife -> {
            String typeNames = getTypeNames(orgLife.getTypes());
            String branchNames = getBranchNames(orgLife.getBranches());
            orgLife.setTypesName(typeNames);
            orgLife.setBranchNames(branchNames);
        });
        return page;
    }

    /**
     * 获取组织生活类型
     *
     * @param orgCode
     * @throws IOException
     */
    public List<ClassificationDTO> queryClassificationList(String orgCode) throws IOException {
        List<ClassificationDTO> list = ezbService.queryClassificationList(orgCode);
        list = list.stream()
                .filter(dto -> !ignoreClassificationIds.contains(dto.getClassificationId())).collect(Collectors.toList());
        return list;
    }

    /**
     * 获取组织生活详情
     *
     * @param id
     * @return
     */
    @Override
    public OrgLife getInfo(Long id) {
        OrgLife orgLife = get(id);
        //对主要内容中的 html 代码进行转码
        orgLife.setMeetingContent(StringEscapeUtils.unescapeHtml4(orgLife.getMeetingContent()));

        setOrgLifeList(id, orgLife);

        String startAndEndTime = UtilDate.getDateFormatStr(orgLife.getStartDate(), "yyyy年MM月dd日 HH:mm");
        if (orgLife.getEndDate() != null) {
            startAndEndTime += " 至 " + UtilDate.getDateFormatStr(orgLife.getEndDate(), "HH:mm");
        }
        orgLife.setStartAndEndTime(startAndEndTime);

        orgLife.setMeetingNum(getPeopleNum(orgLife.getPeoples(), null));
        orgLife.setAbsentNum(getPeopleNum(orgLife.getPeoples(), PersonnelType.NOT_JOIN));
        orgLife.setActualNum(getPeopleNum(orgLife.getPeoples(), PersonnelType.JOIN));
        return orgLife;
    }

    /**
     * 初始化关联文件
     *
     * @param id
     * @param orgLife
     */
    private void setOrgLifeList(Long id, OrgLife orgLife) {
        Map<String, Object> param = Maps.newHashMap("orgLifeId", orgLife.getId());
        List<OrgLifeType> typeList = typeService.queryAll(param);

        param.put("attachmentType", AttachmentType.IMAGE);
        List<OrgLifeAttachment> imageList = attachmentService.queryAll(param);

        param.put("attachmentType", AttachmentType.FILE);
        List<OrgLifeAttachment> fileList = attachmentService.queryAll(param);

        List<OrgLifePeople> peopleList = peopleService.queryAll(param);

        List<OrgLifeBranch> branchList = branchService.queryAll(param);

        branchList.stream().forEach(branch -> {
            Map<String, Object> searchParam = Maps.newHashMap("orgLifeId", id);
            searchParam.put("branchId", branch.getBranchId());
            List<OrgLifePeople> peoples = peopleService.queryAll(searchParam);

            setPersonnelType(peoples, orgLife.getMeetingRoomId());
            branch.setPeoples(peoples);
        });

        setPersonnelType(peopleList, orgLife.getMeetingRoomId());

        orgLife.setTypesName(getTypeNames(typeList));
        orgLife.setTypes(typeList);

        orgLife.setPeoples(peopleList);
        orgLife.setImages(imageList);
        orgLife.setFiles(fileList);
        orgLife.setBranches(branchList);
    }

    /**
     * 自定义保存
     *
     * @param orgLife
     */
    @Override
    public OrgLife customSave(OrgLife orgLife) throws Exception {
        //先保存组织生活
        if (orgLife.getId() == null) {
            orgLife.setStartDate(new Date());
            orgLife.setSign(Sign.ENABLE);
            orgLife.setRepairSign(Sign.ENABLE);
            save(orgLife);
        } else {
            update(orgLife, UtilBean.getPropertyNames(OrgLife.class));
        }
        saveList(orgLife);

        MeetingRoom meetingRoom = saveAndConvene(orgLife.getId());
        orgLife.setMeetingRoomId(meetingRoom.getId());
        update(orgLife, Sets.newHashSet("meetingRoomId"));
        return orgLife;
    }

    /**
     * 结束组织生活，并上传附件
     *
     * @param orgLife
     * @return
     */
    public OrgLife finish(OrgLife orgLife) {
        attachmentService.save(orgLife.getId(), orgLife.getAttachments());
        orgLife.setEndDate(new Date());
        orgLife.setOrgLifeStatus(OrgLifeStatus.FINISHED);
        meetingRoomClient.close(orgLife.getMeetingRoomId());
        update(orgLife, Sets.newHashSet("orgLifeStatus", "endDate"));
        return orgLife;
    }

    /**
     * 组织生活发布
     *
     * @param id
     */
    @Override
    public void push(Long id) throws InterruptedException, ExecutionException, IOException {
        OrgLife orgLife = getInfo(id);
        List<OrgLifeMeetingDTO> list = getOrgLifeMeetingDTO(orgLife);
        for (OrgLifeMeetingDTO dto : list) {
            ezbService.createMeeting(dto);
        }
        orgLife.setPushStatus(PushStatus.PUSH);
        update(orgLife, Sets.newHashSet("pushStatus"));

    }

    @Override
    public List<String> getOrgListIds(Map<String, Object> paramSearch) {
        String classificationId=paramSearch.get("classificationId").toString();
        if(classificationId.equals("0")) {
            return this.entityDao.orgLifeIds(paramSearch);
        }else{
            return this.entityDao.orgLifeReportIds(paramSearch);
        }
    }



    /**
     * 保存并创建会议室
     *
     * @param orgLifeId
     * @return
     */
    private MeetingRoom saveAndConvene(Long orgLifeId) {
        OrgLife orgLife = get(orgLifeId);
        Map<String, Object> searchParam = Maps.newHashMap("orgLifeId", orgLifeId);
        List<OrgLifePeople> peopleList = peopleService.queryAll(searchParam);
        MeetingRoom dto = new MeetingRoom();
        dto.setId(orgLife.getMeetingRoomId());
        dto.setRoomName(orgLife.getMeetingName());
        dto.setIntervalTime(orgLife.getIntervalTime());
        dto.setOrgId(orgLife.getOrgId());
        dto.setMeetingType(MeetingType.ORGANIZATION);
        dto.setRoomType(RoomType.FIXED);

        List<MeetingRoomPerson> recordPersonList = Lists.newArrayList();
        peopleList.stream().forEach(people -> {
            recordPersonList.add(getPerson(people));
        });
        dto.setPersons(recordPersonList);
        dto.setHopeStartTime(null);
        dto.setSign("ENABLE");
        dto.setSignDuration(orgLife.getSignDuration());
        dto.setRepairSign("ENABLE");
        dto.setRepairDuration(orgLife.getRepairDuration());
        dto = meetingRoomClient.saveAndConvene(dto);
        return dto;
    }

    /**
     * 保存组织生活关联数据
     *
     * @param orgLife
     */
    private void saveList(OrgLife orgLife) {
        Long orgLifeId = orgLife.getId();
        typeService.save(orgLifeId, orgLife.getTypes());
        peopleService.save(orgLifeId, orgLife.getPeoples());
        branchService.save(orgLifeId, orgLife.getBranches());
    }

    /**
     * 初始化授权用户信息
     *
     * @param people
     */
    private MeetingRoomPerson getPerson(OrgLifePeople people) {
        MeetingRoomPerson person = new MeetingRoomPerson();
        person.setPersonName(people.getPersonnelName());
        person.setTelephone(people.getTelephone());
        person.setUserHash(people.getPersonnelHash());
        person.setOrgId(people.getBranchId());
        person.setUserType(UserType.DT);
        person.setUserRight(people.getDel() ? UserRight.NOAUTH : UserRight.ATTENDEE);
        return person;
    }


    /**
     * 初始化数据为e支部组织生活dto
     *
     * @param orgLife
     * @return
     */
    private List<OrgLifeMeetingDTO> getOrgLifeMeetingDTO(OrgLife orgLife) throws InterruptedException, ExecutionException, IOException {
        List<OrgLifeMeetingDTO> list = Lists.newArrayList();
        List<OrgLifeBranch> branchList = orgLife.getBranches();

        List<AttachmentDTO> attachmentDTOList = getAttachmentDTOS(orgLife);

        for (OrgLifeBranch branch : branchList) {
            OrgLifeMeetingDTO meetingDto = new OrgLifeMeetingDTO();
            BeanUtils.copyProperties(orgLife, meetingDto);
            meetingDto.setBranchId(branch.getBranchId());
            meetingDto.setOrgCode(branch.getBranchCode());
            meetingDto.setBranchName(branch.getBranchName());
            meetingDto.setStartTime(orgLife.getStartDate());
            meetingDto.setEndTime(orgLife.getEndDate());

            meetingDto.setMeetingNum(getPeopleNum(branch.getPeoples(), null));


            meetingDto.setActualNum(getPeopleNum(branch.getPeoples(), PersonnelType.JOIN));
            meetingDto.setAbsentNum(getPeopleNum(branch.getPeoples(), PersonnelType.NOT_JOIN));

            List<MeetingClassInficationDTO> classInficationDtos = Lists.newArrayList();
            for (OrgLifeType orgLifeType : orgLife.getTypes()) {
                MeetingClassInficationDTO classInficationDto = new MeetingClassInficationDTO();
                classInficationDto.setClassificationId(orgLifeType.getClassificationId());
                classInficationDto.setClassificationName(orgLifeType.getTypeName());
                classInficationDtos.add(classInficationDto);
            }
            meetingDto.setMeetingUnClassInficationList(classInficationDtos);

            meetingDto.setAttachmentList(
                    attachmentDTOList.stream()
                            .filter(dto -> AttachmentType.IMAGE.getRemark().equals(dto.getAttachmentType()))
                            .collect(Collectors.toList()));
            meetingDto.setUploadFileList(
                    attachmentDTOList.stream()
                            .filter(dto -> AttachmentType.FILE.getRemark().equals(dto.getAttachmentType()))
                            .collect(Collectors.toList()));

            List<MeetingPersonDTO> userlist = Lists.newArrayList();
            for (OrgLifePeople people : branch.getPeoples()) {
                MeetingPersonDTO personDto = new MeetingPersonDTO();
                personDto.setInterName(people.getPersonnelName());
                personDto.setPersonnelId(people.getPersonnelId());
                BeanUtils.copyProperties(people, personDto);
                personDto.setPersonnelType(people.getPersonnelType().getCode());
                userlist.add(personDto);
            }
            meetingDto.setUserlist(userlist);
            list.add(meetingDto);
        }


        return list;
    }


    /**
     * 获取上传的文件
     *
     * @param orgLife
     * @return
     */
    private List<AttachmentDTO> getAttachmentDTOS(OrgLife orgLife) throws IOException, ExecutionException, InterruptedException {

        List<AttachmentDTO> attachmentList = Lists.newArrayList();


        for (OrgLifeAttachment file : orgLife.getFiles()) {
            AttachmentDTO attachmentDto = new AttachmentDTO();
            attachmentDto.setAttachmentType(file.getAttachmentType().getRemark());
            attachmentDto.setAttachmentRelName(file.getAttachmentRealName());
            attachmentDto.setAttachmentAddr(file.getDownloadPath());
            attachmentList.add(attachmentDto);
        }
        for (OrgLifeAttachment file : orgLife.getImages()) {
            AttachmentDTO attachmentDto = new AttachmentDTO();
            attachmentDto.setAttachmentType(file.getAttachmentType().getRemark());
            attachmentDto.setAttachmentRelName(file.getAttachmentRealName());
            attachmentDto.setAttachmentAddr(file.getDownloadPath());
            attachmentList.add(attachmentDto);
        }

        List<Future<AttachmentDTO>> futureList = ezbService.uploadToEzb(attachmentList);
        for (Future<AttachmentDTO> future : futureList) {
            AttachmentDTO dto = future.get();
        }

        return attachmentList;
    }

    /**
     * 获取组织生活类型名称
     *
     * @return
     */
    private String getTypeNames(List<OrgLifeType> types) {
        String typeNames = "";
        for (OrgLifeType type : types) {
            if (UtilString.isEmpty(typeNames)) {
                typeNames = type.getTypeName();
            } else {
                typeNames += "，" + type.getTypeName();
            }
        }
        return typeNames;
    }

    /**
     * 获取组织生活类型名称
     *
     * @param branches
     * @return
     */
    private String getBranchNames(List<OrgLifeBranch> branches) {
        String branchNames = "";
        for (OrgLifeBranch branch : branches) {
            if (UtilString.isEmpty(branchNames)) {
                branchNames = branch.getBranchName();
            } else {
                branchNames += "，" + branch.getBranchName();
            }
        }
        return branchNames;
    }

    private void setPersonnelType(List<OrgLifePeople> peoples, Long meetingRoomId) {
        peoples.stream().forEach(people -> {
            MeetingRoomPerson person = meetingRoomPersonClient.get(meetingRoomId, people.getPersonnelHash());
            if(person!=null) {
                people.setPersonnelType(person.getSignInType() == null ? PersonnelType.NOT_JOIN : PersonnelType.JOIN);
            }else{
                people.setPersonnelType(PersonnelType.NOT_JOIN);
            }
        });
    }


    /**
     * 获取组织生活类型名称
     *
     * @return
     */
    private Integer getPeopleNum(List<OrgLifePeople> peopleList, PersonnelType personnelType) {
        List<OrgLifePeople> list = Lists.newArrayList();
        list = peopleList.stream()
                .filter(people -> personnelType == null || people.getPersonnelType() == personnelType).collect(Collectors.toList());
        return list.size();
    }


}
