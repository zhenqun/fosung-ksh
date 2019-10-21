package com.fosung.ksh.meeting.control.dto.room;

import com.fosung.ksh.meeting.control.constant.UserAuth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wangyh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRightByRoomDTO implements Serializable {

    /**
     * 用户名
     */
    private String userName;


    /**
     * 权限
     */
    private UserAuth auth;

}
