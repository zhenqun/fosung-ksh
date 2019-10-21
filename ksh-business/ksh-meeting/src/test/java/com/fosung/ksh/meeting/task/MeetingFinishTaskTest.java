package com.fosung.ksh.meeting.task;

import com.fosung.ksh.meeting.MeetingApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"com.fosung"})
@SpringBootTest(classes = MeetingApplication.class)
public class MeetingFinishTaskTest {

    @Autowired
    MeetingFinishTask meetingFinishTask;

    @Test
    public void autoStartTask() {
    }

    @Test
    public void autoFinishTask() {
        meetingFinishTask.autoFinishTask();
    }
}
