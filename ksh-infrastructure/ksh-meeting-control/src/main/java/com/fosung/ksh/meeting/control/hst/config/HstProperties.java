package com.fosung.ksh.meeting.control.hst.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 好视通会控接口基本配置
 *
 * @author wangyh
 * @date 2018/12/01 11:42
 */
@ConfigurationProperties(prefix = "ksh.hst")
@Data
public class HstProperties {

    /**
     * 好视通服务地址
     */
    private String webServiceUrl = "http://172.16.67.8:8081/fmapi/webservice/jaxws?wsdl";

    /**
     * pc加入视频会议URL
     *
     * roomId: 好视通视频会议室ID
     * userName: 好视通用户名，一般为ID或者HASH
     * userPwd: 用户密码，随意字符串
     * autoCheck: 是否自动调起人脸识别
     * orgLife: 是否是组织生活会议室，是组织生活时，默认传入用户ID
     * layoutCod: 会议室布局,
     * W1切换到标准布局
     * W2 切换到培训布局
     * W3 切换到视频布局
     * WF	切换到全屏模式
     * WV1 切换到1分屏模式
     * WV2 切换到2分屏模式
     * WV3 切换到画中画分屏模式
     * WV4 切换到4分屏模式
     * WV6 切换到6分屏模式
     * WV9 切换到9分屏模式
     * WV12 切换到12分屏模式
     * WV16 切换到16分屏模式
     * WV25 切换到25分屏模式
     * WV36 切换到36分屏模式
     * WV49 切换到49分屏模式
     * WV64 切换到64分屏模式
     */
    private String joinMeetingUrl = "http://visual.fosung.com:8081/launch/toEnterMeeting.do" +
            "?roomID={roomId}&userName={userName}&userPwd={userPwd}&autoCheck=1&orgLife=1&layoutCode={layoutCode}";

    /**
     * pc视频会议巡查地址
     *
     * roomId: 好视通视频会议室ID
     * userName: 好视通用户名，一般为ID或者HASH
     * userPwd: 用户密码，随意字符串
     * patrol: 是否是视频会议巡查模式
     */
    private String patrolMeetingUrl = "http://visual.fosung.com:8081/launch/toEnterMeeting.do" +
            "?roomID={roomId}&userName={userName}&userPwd={userPwd}&patrol=1";

    /**
     * 用户同步至好视通时默认用户密码
     */
    private String defaultPwd = "AdminABC!@#";

    /**
     * 是否进行MD5加密
     */
    private String password_type = "1";
    /**
     * 密钥用于访问认证，默认为fswebservice2011，接口中出现的keyCode都是fswebservice2011加密后字符串
     */
    private String keyCode = "3025495AEE146DA3864AB81BAAF79A3E";

    /**
     * 方法
     */
    private Method method = new Method();


    @Data
    public class Method {
        /**
         * 用户相关方法
         */
        private User user = new User();

        /**
         * 组织相关方法
         */
        private Depart depart = new Depart();

        /**
         * 会议相关方法
         */
        private Meeting meeting = new Meeting();

    }

    @Data
    public class User {

        /**
         * 新增用户方法
         */
        private String addUserinfo = "addUserinfo";

        /**
         * 批量新增用户
         */
        private String batchAddUser = "batchAddUser";

        /**
         * 修改用户
         */
        private String editUser = "editUser";

        /**
         * 删除用户
         */
        private String delUser = "delUser";

        /**
         * 获取用户信息
         */
        private String getUserinfoList = "getUserinfoList";


    }

    /**
     * 会议相关方法
     */
    @Data
    public class Meeting {

        /**
         * 新增会议室
         */
        private String addRoominfo = "addRoominfo";

        /**
         * 设置固定会议室
         */
        private String editRoomTypeToFixed = "editRoomTypeToFixed";

        /**
         * 设置预约会议室
         */
        private String editRoomTypeToReservation = "editRoomTypeToReservation";

        /**
         * 设置周月例会会议室
         */
        private String editRoomTypeToRoutine = "editRoomTypeToRoutine";

        /**
         * 修改会议室的视频相关参数，包括分辨率、码流、编码器等内容。开启情况下，客户端登录后默认使用这些参数。
         */
        private String editRoomDefaultFlag = "editRoomDefaultFlag";

        /**
         * 修改会议室基本信息
         */
        private String editRoominfo = "editRoominfo";

        /**
         * 制定会议室给用户授权
         */
        private String doUserRightByRoomid = "doUserRightByRoomid";

        /**
         * 通过用户名给会议室授权
         */
        private String doRoomRightByUserName = "doRoomRightByUserName";

        /**
         * 删除会议室
         */
        private String delRoominfo = "delRoominfo";

        /**
         * 获取会议室列表
         */
        private String getRoominfoList = "getRoominfoList";

        /**
         * 获取用户有权限的会议室列表
         */
        private String getRoominfoByUserName = "getRoominfoByUserName";

        /**
         * 获取会议室授权的用户列表
         */
        private String getUserinfoListByRoomId = "getUserinfoListByRoomId";

        /**
         * 获取会议室登录地址
         */
        private String getLoginAddress = "getLoginAddress";

        /**
         * 获取会议室当前登录人数
         */
        private String getCurUserCount = "getCurUserCount";

        /**
         * 获取某个用户是否在某个会议室中
         */
        private String userInRoom = "userInRoom";

        /**
         * 获取某个会议室当前用户信息
         */
        private String queryCurUserinfoPage = "queryCurUserinfoPage";

        /**
         * 获取某个会议室用户在线情况
         */
        private String queryUserOnlineStatus = "queryUserOnlineStatus";

        /**
         * 清空会议室
         */
        private String cleanRoomUsers = "cleanRoomUsers";

        /**
         * 请出用户
         */
        private String kickOutRoomUsers = "kickOutRoomUsers";

        /**
         * 发送信息到某个会议室
         */
        private String sendMsgToRoomUsers = "sendMsgToRoomUsers";

        /**
         * 启用关闭会议室
         */
        private String editRoomStatus = "editRoomStatus";

    }

    /**
     * 组织机构相关方法
     */
    @Data
    public class Depart {

        /**
         * 新增组织机构
         */
        private String addDepartinfo = "addDepartinfo";

        /**
         * 修改组织机构
         */
        private String editDepartinfo = "editDepartinfo";

        /**
         * 删除组织机构
         */
        private String delDepartinfo = "delDepartinfo";

        /**
         * 查询组织机构信息
         */
        private String getDepartinfo = "getDepartinfo";
    }

}


