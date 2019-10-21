package com.fosung;

import com.fosung.framework.common.json.JsonMapper;
import com.fosung.ksh.meeting.control.MeetingControlApplication;
import com.fosung.ksh.meeting.control.hst.config.constant.SerachType;
import com.fosung.ksh.meeting.control.hst.response.RoomResponseDTO;
import com.fosung.ksh.meeting.control.hst.service.MeetingRoomService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"com.fosung"})
@SpringBootTest(classes = MeetingControlApplication.class)
public class HstTest {
    @Autowired
    MeetingRoomService meetingRoomService;

    @Test
    public void test() throws Exception {
        List<RoomResponseDTO> list = meetingRoomService.getRoominfoList(10369,null, SerachType.LR,0,10);
        log.info(JsonMapper.toJSONString(list));
        Integer curUserCount = meetingRoomService.getCurUserCount(10369);
        log.info(curUserCount + "");
    }


    @Test
    public void userInRoom() throws Exception {
        log.info(meetingRoomService.userInRoom(10940,"Ui84PDPWe7N5wsbvfe6hqqjQbLE=") + "");
    }
}
