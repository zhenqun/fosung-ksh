package com.fosung.ksh.aiface.tencent;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.iai.v20180301.IaiClient;
import com.tencentcloudapi.iai.v20180301.models.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@Service
public class GroupService {

    @Resource
    private TencentIaiClientService tencentIaiClientService;

    /**
     * @param offset 起始序号，默认值为0
     * @param limit  返回数量，默认值为10，最大值为1000
     */
    public GetGroupListResponse getGroupList(Integer offset, Integer limit) throws TencentCloudSDKException {
        IaiClient iaiClient = tencentIaiClientService.getIaiClient();
        GetGroupListRequest req = new GetGroupListRequest();
        req.setLimit(limit == null ? 100 : limit);
        return iaiClient.GetGroupList(req);
    }

    public CreateGroupResponse create(String groupId, String groupName) throws TencentCloudSDKException {
        IaiClient iaiClient = tencentIaiClientService.getIaiClient();
        CreateGroupRequest req = new CreateGroupRequest();
        req.setGroupId(groupId);
        req.setGroupName(groupName);
        return iaiClient.CreateGroup(req);
    }

    public ModifyGroupResponse ModifyGroup(String groupId, String groupName) throws TencentCloudSDKException {
        IaiClient iaiClient = tencentIaiClientService.getIaiClient();
        ModifyGroupRequest req = new ModifyGroupRequest();
        req.setGroupId(groupId);
        req.setGroupName(groupName);
        return iaiClient.ModifyGroup(req);
    }
}
