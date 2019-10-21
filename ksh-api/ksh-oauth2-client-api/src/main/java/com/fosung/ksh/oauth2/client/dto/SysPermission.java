package com.fosung.ksh.oauth2.client.dto;

import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限
 */
@Data
public class SysPermission implements Serializable {


    /**
     * permissionId : 34
     * permissionName : FIRSTSECRETARY_OPERATE
     * permissionDescription : 可操作第一书记
     * clientId : party_firstsecretary_ui
     */

    private String permissionId;

    private String permissionName;

    private String permissionDescription;

}
