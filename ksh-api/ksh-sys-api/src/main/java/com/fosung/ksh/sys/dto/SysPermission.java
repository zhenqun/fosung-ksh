package com.fosung.ksh.sys.dto;

import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Data;

/**
 * 权限
 */
@Data
public class SysPermission extends AppJpaBaseEntity {


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
