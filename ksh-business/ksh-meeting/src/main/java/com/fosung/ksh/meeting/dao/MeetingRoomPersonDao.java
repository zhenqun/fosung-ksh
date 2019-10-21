package com.fosung.ksh.meeting.dao;

import com.fosung.framework.dao.jpa.AppJPABaseDao;
import com.fosung.framework.dao.jpa.annotation.MybatisQuery;
import com.fosung.ksh.meeting.entity.MeetingRoomPerson;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 参会人员
 *
 * @author wangyh
 */
public interface MeetingRoomPersonDao extends AppJPABaseDao<MeetingRoomPerson, Long> {


    /**
     * 分类型统计签到人数，未签到人数
     *
     * @param meetingRoomId
     * @param orgIds
     * @param userType
     * @return
     */
    @MybatisQuery
    public Map<String, Integer> countSign(@Param("meetingRoomId") Long meetingRoomId,
                                          @Param("orgIds") List<String> orgIds,
                                          @Param("userType") String userType
    );

    @MybatisQuery
    public Integer countSignNeed(@Param("meetingRoomId") Long meetingRoomId,
                                 @Param("orgIds") List<String> orgIds,
                                 @Param("userType") String userType,
                                 @Param("userRight") String userRight,
                                 @Param("signInType") String signInType,
                                 @Param("signNotType") String signNotType

    );

    @MybatisQuery
    public Integer countOrgSignNeed(@Param("meetingRoomId") Long meetingRoomId,
                                    @Param("orgIds") List<String> orgIds,
                                    @Param("userType") String userType,
                                    @Param("userRight") String userRight,
                                    @Param("signInType") String signInType
    );

    @MybatisQuery
    int sitesIng(@org.apache.ibatis.annotations.Param("orgIds") List<String> orgIds);

    /**
     * 分类型统计签到人数，未签到人数
     *
     * @param meetingRoomId
     * @param orgIds
     * @param userType
     * @return
     */
    @MybatisQuery
    public Map<String, Integer> countOrgSign(@Param("meetingRoomId") Long meetingRoomId,
                                             @Param("orgIds") List<String> orgIds,
                                             @Param("userType") String userType);


    @MybatisQuery
    int sitesNum(Map<String, Object> paramMap);

    @MybatisQuery
    int sitesAllYear(@org.apache.ibatis.annotations.Param("orgIds") List<String> orgIds,
                     @org.apache.ibatis.annotations.Param("startDate") Date startDate,
                     @org.apache.ibatis.annotations.Param("endDate") Date endDate);

    @MybatisQuery
    List<Map<String, Object>> meeetingRoomOrgNum(@org.apache.ibatis.annotations.Param("searchParam") Map<String, Object> searchParam);

    @MybatisQuery
    Integer meeetingRoomPersonNum(@org.apache.ibatis.annotations.Param("searchParam") Map<String, Object> searchParam);

    @MybatisQuery
    List<Map<String, Object>> getOrgByMeetingRoom(@org.apache.ibatis.annotations.Param("searchParam") Map<String, Object> searchParam);
}
