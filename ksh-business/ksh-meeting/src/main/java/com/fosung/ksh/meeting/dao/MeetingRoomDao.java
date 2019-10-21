package com.fosung.ksh.meeting.dao;

import com.fosung.framework.dao.config.mybatis.page.MybatisPage;
import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.framework.dao.jpa.annotation.MybatisQuery;
import com.fosung.ksh.meeting.constant.MeetingStatus;
import com.fosung.ksh.meeting.constant.MeetingType;
import com.fosung.ksh.meeting.entity.MeetingRoom;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 会议室相关
 *
 * @author wangyh
 */
public interface MeetingRoomDao extends AppJPABaseDao<MeetingRoom, Long> {
    /**
     * 查询我的会议室数据
     *
     * @param orgId
     * @param roomName
     * @param userId
     * @param pageable
     * @return
     */
    @MybatisQuery
    public MybatisPage<Map<String, Object>> queryMyMeetingList(@Param("orgId") String orgId, @Param("roomName") String roomName, @Param("userHash") String userHash, @Param("meetingStatus") String meetingStatus , Pageable pageRequest);

    /**
     * 查询正在召开的会议室
     *
     * @param orgIdList
     * @param pageRequest
     * @return
     */
    @MybatisQuery
    public MybatisPage<MeetingRoom> queryGoingMeetingList(@Param("orgIdList") List<String> orgIdList,@Param("roomName") String roomName, @Param("meetingType") String meetingType, Pageable pageRequest);

    /**
     * 获取全部正在召开的会议室
     *
     * @return
     */
    @MybatisQuery
    public List<Map<String, Object>> countMeetingType(@Param("orgIds") List<String> orgIds);
    @MybatisQuery
    public Integer countMeetingNum(Map<String,Object>  paramMap);

    /**
     * 启动预约会议室
     *
     * @param now
     */
    @Modifying
    @Query("update MeetingRoom set meetingStatus = 'GOING' " +
            " where meetingStatus = 'NOTSTART' and roomType = 'HOPE' and ?1 > hopeStartTime")
    public int start(Date now);

    /**
     * 关闭超时的预约会议室
     *
     * @param now
     */
    @Modifying
    @Query("update MeetingRoom set meetingStatus = 'FINISHED', realEndTime = hopeEndTime " +
            " where meetingStatus = 'GOING' and roomType = 'HOPE' and ?1 > hopeEndTime")
    public int finish(Date now);

    @MybatisQuery
    int meetingsAllYear(@Param("orgIds") List<String> orgIds,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);
    @MybatisQuery
    int sitesIng (Map<String,Object>  paramMap);
}
