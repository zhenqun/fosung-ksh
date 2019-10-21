package com.fosung.ksh.meeting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.support.AppRuntimeDict;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import com.fosung.ksh.meeting.constant.UserRight;
import com.fosung.ksh.meeting.constant.UserType;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "meeting_template_person")
@Data
public class MeetingTemplatePerson extends AppJpaBaseEntity {


        /**
         * 会议室模版Id
         */
        @Column(name = "meeting_template_id")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Long meetingTemplateId;


        /**
         * 授权唯一标志
         * 如果为本地用户，则为String类型本地用户ID
         * 如果为党员用户，则为党员用户hash
         */
        @Column(name = "user_hash")
        private String userHash;

        /**
         */
        @Column(name = "telephone")
        private String telephone;

        /**
         * 授权唯一标志
         * 如果为本地用户，则为String类型本地用户ID
         * 如果为党员用户，则为党员用户hash
         */
        @Column(name = "person_name")
        private String personName;


        /**
         * 用户类型 1系统用户 ,2灯塔党员
         */
        @Enumerated(EnumType.STRING)
        @Column(name = "user_type")
        private UserType userType;


        /**
         * 会议室角色 1旁听 2 参会人 3 主席；  权限值为0时，表示取消该用户会议室权限；会议ID为0时，则授予全部会议室。
         */
        @Enumerated(EnumType.STRING)
        @Column(name = "user_right")
        private UserRight userRight;


        /**
         * 所属组织
         */
        @Column(name = "org_id")
        private String orgId;

        /**
         * org_code
         */
        @Column(name = "org_code")
        private String orgCode;


        /**
         * org_name
         */
        @Column(name = "org_name")
        private String orgName;

}
