package com.fosung.ksh.meeting;


import com.fosung.framework.common.util.UtilNumber;
import com.fosung.ksh.meeting.control.client.OrgClient;
import com.fosung.ksh.meeting.control.client.RoomClient;
import com.fosung.ksh.meeting.control.client.UserClient;
import com.fosung.ksh.meeting.control.constant.SerachType;
import com.fosung.ksh.meeting.control.dto.org.EditOrgRequestDTO;
import com.fosung.ksh.meeting.entity.MeetingOrgSync;
import com.fosung.ksh.meeting.entity.MeetingRoom;
import com.fosung.ksh.meeting.entity.MeetingUserSync;
import com.fosung.ksh.meeting.service.MeetingInitOrgService;
import com.fosung.ksh.meeting.service.MeetingOrgSyncService;
import com.fosung.ksh.meeting.service.MeetingRoomService;
import com.fosung.ksh.meeting.service.MeetingUserSyncService;
import com.fosung.ksh.meeting.task.SyncOrgTask;
import com.fosung.ksh.meeting.task.SyncUserTask;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"com.fosung"})
@SpringBootTest(classes = MeetingApplication.class)
public class MeetingControlApiTest {


    @Autowired
    RoomClient roomClient;


    @Autowired
    OrgClient orgClient;

    @Autowired
    UserClient userClient;

    @Autowired
    MeetingRoomService meetingRoomService;

    @Autowired
    MeetingUserSyncService meetingUserSyncService;

    @Autowired
    MeetingOrgSyncService meetingOrgSyncService;

//    @Autowired
//    SyncOrgSupport syncOrgSupport;

    @Autowired
    SyncUserTask meetingCreateUserTask;

    @Autowired
    SyncOrgTask syncOrgTask;
    @Autowired
    MeetingInitOrgService initOrgService;

    @Test
    public void delRoom() {
        for (int i = 14884; i < 26000; i++) {
            MeetingRoom m = meetingRoomService.getByHstRoomId(i);
            if (m == null) {
                try {
                    roomClient.delRoominfo(i);
                    log.info("删除会议室：" + i);
                } catch (Exception e) {
                    log.error("删除会议室失败： roomId:{},错误信息:{}", i, e.getMessage());
                }
            }
        }


    }
//
//    @Test
//    public void ddd() {
//        syncOrgSupport.pushOrg(UtilDate.parse("2019-06-01"));
//    }


    @Test
    public void sync() {
        meetingCreateUserTask.autoStartTask();
    }

    @Test
    public void syncOrg() {
        syncOrgTask.execute();
    }


    @Test
    public void load() {
        Map<String, String> deptMaps = getDeptIdMap();
        for (int i = 1; i < 1000; i++) {
            List<Map> list = userClient.query("", SerachType.ALL, i, 1000);
            for (Map userResponseDTO : list) {
                MeetingUserSync meetingUserSync = new MeetingUserSync();
                meetingUserSync.setRealName((String) userResponseDTO.get("displayName"));
                meetingUserSync.setUserHash((String) userResponseDTO.get("userName"));

                String orgId = deptMaps.get((String) userResponseDTO.get("departID"));
                meetingUserSync.setOrgId(orgId);
                meetingUserSyncService.save(meetingUserSync);

            }
            if (list.size() < 1000) {
                break;
            }
        }

    }

    @Test
    public void loadOrg() {
        for (int i = 1; i < 100; i++) {
            List<Map<String, Object>> mapList = orgClient.queyOrgList(0, null, null, i, 1000);
            for (Map<String, Object> dept : mapList) {
                MeetingOrgSync orgSync = new MeetingOrgSync();
                orgSync.setOrgId((String) dept.get("authDepartID"));
                orgSync.setOrgName((String) dept.get("departName"));
                orgSync.setParentId((String) dept.get("authParentDepartID"));
                orgSync.setDeptId(UtilNumber.createInteger((String) dept.get("departID")));
                meetingOrgSyncService.save(orgSync);
            }


            if (mapList.size() < 1000) {
                break;
            }
        }

    }


    public Map<String, String> getDeptIdMap() {
        Map<String, String> maps = Maps.newHashMap();
        for (int i = 0; i < 100; i++) {
            List<Map<String, Object>> mapList = orgClient.queyOrgList(0, null, null, i, 1000);
            mapList.stream().forEach(dept -> {
                maps.put((String) dept.get("departID"), (String) dept.get("authDepartID"));
            });
            if (mapList.size() < 1000) {
                break;
            }
        }

        return maps;
    }


    @Test
    public void syncSysOrg2EditOrgRequestDTO() {
        EditOrgRequestDTO editOrgRequestDTO = new EditOrgRequestDTO();
        editOrgRequestDTO.setAuthDepartID("030b9e46-b8ea-47ec-9feb-fb8c3eead801");
        editOrgRequestDTO.setAuthParentDepartID("0");
        editOrgRequestDTO.setDepartName("中国共产党山东省委员会");
        editOrgRequestDTO.setDepartId(1);
        editOrgRequestDTO.setParentDepartID(-1);
        orgClient.edit(editOrgRequestDTO);
        initOrgService.sync();

    }


}
