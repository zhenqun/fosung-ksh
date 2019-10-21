package com.fosung.ksh.meeting.controller;

import com.fosung.ksh.common.dto.DtoUtil;
import com.fosung.ksh.common.dto.handler.KshDTOCallbackHandler;
import com.fosung.ksh.common.response.Result;
import com.fosung.ksh.meeting.entity.MeetingNotice;
import com.fosung.ksh.meeting.entity.MeetingNoticePerson;
import com.fosung.ksh.meeting.entity.MeetingRoom;
import com.fosung.ksh.meeting.service.MeetingNoticePersonService;
import com.fosung.ksh.meeting.service.MeetingNoticeService;
import com.fosung.ksh.meeting.service.MeetingRoomService;
import com.fosung.ksh.sys.dto.SysOrg;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.web.http.ResponseParam;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Slf4j
@Validated
@RestController
@RequestMapping(value = MeetingNoticeController.BASE_PATH)
public class MeetingNoticeController extends AppIBaseController {
    /**
     * 当前模块跟路径
     */
    public static final String BASE_PATH = "/meeting-notice";

    @Autowired
    private MeetingNoticeService meetingNoticeService;

    @Autowired
    private MeetingNoticePersonService meetingNoticePersonService;

    @Autowired
    private MeetingRoomService meetingRoomService;
    /**
     * 发送通知
     * @param meetingNotice
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseEntity<MeetingNotice> save(@Valid @RequestBody MeetingNotice meetingNotice) throws Exception {
        meetingNoticeService.saveMeetNotice(meetingNotice);
        return Result.success(meetingNotice);
    }
    @RequestMapping(value = "get", method = RequestMethod.POST)
    public ResponseEntity<MeetingNotice> get(@RequestParam String userHash){
        List<MeetingNoticePerson> mPersons=meetingNoticePersonService.meetingNoticePeoples(userHash);
        MeetingNotice notice=new MeetingNotice();
        if(mPersons.size()>0){
            MeetingNoticePerson mPerson=mPersons.get(0);
            Long meetingNoticeId=mPerson.getMeetingNoticeId();
            notice=meetingNoticeService.get(meetingNoticeId);
            Long meetingRoomId=notice.getMeetingRoomId();
            MeetingRoom mroom=meetingRoomService.get(meetingRoomId);
           if(mroom!=null) {
               notice.setRoomName(mroom.getRoomName());
           }
        }
        return Result.success(notice);
    }

    @RequestMapping(value = "update",method = RequestMethod.POST)
    public ResponseEntity<MeetingNoticePerson> update(@RequestParam Long meetingNoticeId,@RequestParam String userHash){
        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("meetingNoticeId", meetingNoticeId);
        searchParam.put("userHash", userHash);
        List<MeetingNoticePerson> mPersons=meetingNoticePersonService.queryAll(searchParam);
        if(mPersons.size()>0){
            MeetingNoticePerson person=   mPersons.get(0);
            person.setReadInfo("2");
            Set<String> updateFields = Sets.newLinkedHashSet("readInfo");
            meetingNoticePersonService.update(person,updateFields);
        }
        return Result.success(mPersons);
    }

    @RequestMapping(value = "detail",method = RequestMethod.POST)
    public ResponseEntity<MeetingNotice> detail(@RequestParam("noticeId") long noticeId){
       MeetingNotice notice= meetingNoticeService.get(noticeId);
        Date currentTime = notice.getCreateDatetime();
        if(currentTime!=null){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
            notice.setCreateTime(dateString);
        }
       Long meetingRoomId=notice.getMeetingRoomId();
       MeetingRoom room= meetingRoomService.get(meetingRoomId);
        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("meetingNoticeId", noticeId);
        List<MeetingNoticePerson> mPersons=meetingNoticePersonService.queryAll(searchParam);
        notice.setMeetingRoom(room);
        notice.setPeoples(mPersons);
        return Result.success(notice);
    }
    @ApiOperation(value = "分页查询会议室通知")
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public ResponseEntity<Page<MeetingNotice>> query(
            @ApiParam(value = "页码", defaultValue = "0")
            @RequestParam(required = false, value = "pagenum",
                    defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @ApiParam(value = "每页显示条数", defaultValue = "0")
            @RequestParam(required = false, value = "pagesize",
                    defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize
    ) {
        //获取查询参数
        Map<String, Object> searchParam = getParametersStartingWith(getHttpServletRequest(), DEFAULT_SEARCH_PREFIX);
        //执行分页查询
        Page<MeetingNotice> meetingNoticePage = meetingNoticeService.queryByPage(searchParam, pageNum, pageSize, new String[]{"createDatetime_desc"});
        DtoUtil.handler(meetingNoticePage.getContent(), getDtoCallbackHandler());

        return Result.success(meetingNoticePage);
    }

    private KshDTOCallbackHandler getDtoCallbackHandler() {
        //创建转换接口类
        KshDTOCallbackHandler<MeetingNotice> dtoCallbackHandler = new KshDTOCallbackHandler<MeetingNotice>() {
            @Override
            public void doHandler(MeetingNotice dto) {

                Date currentTime = dto.getCreateDatetime();
                if(currentTime!=null){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateString = formatter.format(currentTime);
                    dto.setCreateTime(dateString);
                }
                long meetingRoomId=dto.getMeetingRoomId();
                MeetingRoom meetingRoom = meetingRoomService.get(meetingRoomId);
                if (meetingRoom != null) {
                  dto.setMeetingRoom(meetingRoom);
                }
            }
        };
        return dtoCallbackHandler;
    }

}
