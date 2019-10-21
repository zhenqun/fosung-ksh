package com.fosung.ksh.aiface.event;

import com.fosung.ksh.aiface.config.AiFaceProperties;
import com.fosung.ksh.aiface.tencent.GroupService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.iai.v20180301.models.GetGroupListResponse;
import com.tencentcloudapi.iai.v20180301.models.GroupInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;

/**
 * 系统启动时，自动检测人脸库，并进行创建
 * @author toquery
 * @version 1
 */
@Slf4j
@Component
public class AiFaceContextRefreshedEvent {


    @Resource
    private GroupService groupService;

    @Resource
    private AiFaceProperties appAiFaceProperties;

    @EventListener(classes = ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) throws TencentCloudSDKException {
        String groupId = appAiFaceProperties.getGroupId();
        String groupName = appAiFaceProperties.getGroupName();

        GetGroupListResponse getGroupListResponse = groupService.getGroupList(null, null);
        Optional<GroupInfo> groupInfoOption = Arrays.stream(getGroupListResponse.getGroupInfos())
                .filter(item -> groupId.equalsIgnoreCase(item.getGroupId())).findAny();
        if (!groupInfoOption.isPresent()) {
            log.info("未找到 {} : {} 的人员组库, 将去创建。", groupId, groupName);
            groupService.create(groupId, groupName);
            log.info("创建 {} : {} 的人员组库成功");
        }
        log.info("智能创建人脸库成功！");
    }
}
