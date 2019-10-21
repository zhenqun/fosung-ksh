package com.fosung.ksh.meeting.control.hst.request.room;

import com.fosung.ksh.meeting.control.hst.config.constant.UserAuth;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("用户唯一标志，通常为ID或者HASH")
    private String userName;


    /**
     * 权限
     */
    @ApiModelProperty("授权权限，（NOAUTH 取消授权，HEARER 旁听，ATTENDEE 参会，CHAIRMAN 主席）")
    private UserAuth auth;

}
