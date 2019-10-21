package com.fosung.ksh.aiface.tencent;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.iai.v20180301.IaiClient;
import com.tencentcloudapi.iai.v20180301.models.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 腾讯云人脸采集接口
 *
 * @author toquery
 * @version 1
 */
@Service
public class PersonService {

    @Resource
    private TencentIaiClientService tencentIaiClientService;

    /**
     * 创建人脸
     *
     * @param groupId
     * @param personName
     * @param personId
     * @param url
     * @return
     * @throws TencentCloudSDKException
     */
    public CreatePersonResponse create(String groupId, String personName, String personId, String url) throws TencentCloudSDKException {
        IaiClient iaiClient = tencentIaiClientService.getIaiClient();
        CreatePersonRequest req = new CreatePersonRequest();
        req.setGroupId(groupId);
        req.setPersonName(personName);
        req.setPersonId(personId);
        req.setUrl(url);
        return iaiClient.CreatePerson(req);
    }

    /**
     * 删除人脸
     *
     * @param personId
     * @return
     * @throws TencentCloudSDKException
     */
    public DeletePersonResponse delete(String personId) throws TencentCloudSDKException {
        IaiClient iaiClient = tencentIaiClientService.getIaiClient();
        DeletePersonRequest req = new DeletePersonRequest();
        req.setPersonId(personId);
        return iaiClient.DeletePerson(req);
    }


    /**
     * 修改人脸信息
     *
     * @param gender
     * @param personName
     * @param personId
     * @return
     * @throws TencentCloudSDKException
     */
    public ModifyPersonBaseInfoResponse modifyPersonBaseInfo(Integer gender, String personName, String personId) throws TencentCloudSDKException {
        IaiClient iaiClient = tencentIaiClientService.getIaiClient();
        ModifyPersonBaseInfoRequest req = new ModifyPersonBaseInfoRequest();
        req.setGender(gender);
        req.setPersonId(personId);
        req.setPersonName(personName);
        return iaiClient.ModifyPersonBaseInfo(req);
    }

    /**
     * 获取人脸信息
     *
     * @param gender
     * @param personName
     * @param personId
     * @return
     * @throws TencentCloudSDKException
     */
    public GetPersonBaseInfoResponse getPersonBaseInfo(String personId) throws TencentCloudSDKException {
        IaiClient iaiClient = tencentIaiClientService.getIaiClient();
        GetPersonBaseInfoRequest req = new GetPersonBaseInfoRequest();
        req.setPersonId(personId);
        return iaiClient.GetPersonBaseInfo(req);
    }


}
