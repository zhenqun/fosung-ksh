package com.fosung.ksh.organization.ezb.service.impl;

import com.fosung.ksh.organization.ezb.client.EzbAsyncClient;
import com.fosung.ksh.organization.ezb.dto.*;
import com.fosung.ksh.organization.ezb.id.DistributedIdGenerator;
import com.fosung.ksh.organization.ezb.id.IdGenerator;
import com.fosung.ksh.organization.ezb.service.EzbService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 与E支部对接
 *
 * @author wangyh
 */
@Slf4j
@Component
public class EzbServiceImpl implements EzbService {
    private static final long WORK_ID = 1L;

    private IdGenerator idGenerator = new DistributedIdGenerator(WORK_ID);


    @Autowired
    private EzbAsyncClient ezbClient;

    /**
     * 查询组织生活分类
     *
     * @param orgCode
     * @return
     * @throws IOException
     */
    @Override
    public List<ClassificationDTO> queryClassificationList(String orgCode) throws IOException{
        return ezbClient.queryClassificationList(orgCode);
    }

    /**
     * 发布组织生活到E支部系统
     *
     * @param meetingDto
     * @return
     */
    public OrgLifeMeetingDTO createMeeting(OrgLifeMeetingDTO meetingDto) throws IOException, ExecutionException, InterruptedException {
        String meetingId = idGenerator.next();

        // 上传文件到E支部文件服务器
        meetingDto.setMeetingId(meetingId);

        // 为组织生活类型设置ID和meetingId
        for (MeetingClassInficationDTO meetingClassInficationDto : meetingDto.getMeetingUnClassInficationList()) {
            meetingClassInficationDto.setMeetingId(meetingId);
            meetingClassInficationDto.setUnionId(idGenerator.next());
        }

        // 为参会人员设置ID和meetingId
        for (MeetingPersonDTO meetingPersonDto : meetingDto.getUserlist()) {
            meetingPersonDto.setUnionId(idGenerator.next());
            meetingPersonDto.setMeetingId(meetingId);
        }

        // 为附件设置ID和meetingId
        setAttachmentData(meetingId, meetingDto.getAttachmentList());
        setAttachmentData(meetingId, meetingDto.getUploadFileList());

        ezbClient.createMeeting(meetingDto);
        ezbClient.push(meetingDto);
        return meetingDto;
    }

    /**
     * 为Attachment设置ID和MeetingId
     *
     * @param meetingId
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private void setAttachmentData(String meetingId, List<AttachmentDTO> attachmentDTOList) throws ExecutionException, InterruptedException {
        for (AttachmentDTO dto : attachmentDTOList) {
            dto.setAttachmentId(idGenerator.next());
            dto.setBelongTypeId(meetingId);
        }
    }

    /**
     * 文件异步上传至Ezb文件服务器
     *
     * @return
     * @throws MalformedURLException
     */
    public List<Future<AttachmentDTO>> uploadToEzb(List<AttachmentDTO> attachmentList) throws IOException {
        List<Future<AttachmentDTO>> futureList = Lists.newArrayList();
        for (AttachmentDTO dto : attachmentList) {
            Future<AttachmentDTO> future = ezbClient.uploadFileToEzb(dto);
            futureList.add(future);
        }
        return futureList;
    }

}
