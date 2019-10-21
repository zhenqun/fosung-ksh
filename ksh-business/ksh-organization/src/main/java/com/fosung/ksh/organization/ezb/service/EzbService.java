package com.fosung.ksh.organization.ezb.service;

import com.fosung.ksh.organization.ezb.dto.AttachmentDTO;
import com.fosung.ksh.organization.ezb.dto.ClassificationDTO;
import com.fosung.ksh.organization.ezb.dto.OrgLifeMeetingDTO;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface EzbService {


    /**
     * 创建组织生活
     * @param meetingDto
     * @return
     * @throws IOException
     */
    public OrgLifeMeetingDTO createMeeting(OrgLifeMeetingDTO meetingDto) throws IOException, ExecutionException, InterruptedException;


    /**
     * 查询组织生活类型
     * @param orgCode
     * @return
     * @throws IOException
     */
    public List<ClassificationDTO> queryClassificationList(String orgCode) throws IOException;

    /**
     * 文件异步上传至Ezb文件服务器
     *
     * @return
     * @throws MalformedURLException
     */
    public List<Future<AttachmentDTO>> uploadToEzb(List<AttachmentDTO> attachmentList) throws IOException;

}
