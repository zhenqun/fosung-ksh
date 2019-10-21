package com.fosung.ksh.oauth2.server.dt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wangyihua
 * @date 2019-06-03 09:36
 */
@Data
public class DTUser implements Serializable {

    private static final long serialVersionUID = 8615789116224247813L;
    private String orgId;

    private String orgCode;

    private String orgName;

    private String hash;

    private String userId;

    private String userName;

    private String telephone;

    @JsonProperty("param1")
    private String realName;

    private Boolean del = false;
}
