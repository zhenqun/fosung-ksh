package com.fosung.ksh.surface.controller;

import com.fosung.framework.web.http.ResponseParam;
import com.fosung.ksh.surface.client.SyncOrgClient;
import com.fosung.ksh.surface.client.SyncUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author toquery
 * @version 1
 */
@Validated
@RestController
@RequestMapping("/hst")
@Deprecated
public class SyncController {
    @Autowired
    private SyncOrgClient syncOrgClient;

    @Autowired
    private SyncUserClient syncUserClient;
    /**
     * 党组织同步接口
     *
     * @param syncDate
     * @return
     */
    @RequestMapping(value = "/sync/org", method = RequestMethod.POST)
    public ResponseParam syncOrg(@RequestParam(value = "syncDate", required = false) String syncDate) {
        return ResponseParam.success().datalist(syncOrgClient.syncOrg(syncDate));
    }


    /**
     * 党员同步接口
     *
     * @param syncDate
     * @return
     */
    @RequestMapping(value = "/sync/user", method = RequestMethod.POST)
    public ResponseParam syncUser(@RequestParam(value = "syncDate", required = false) String syncDate) {
        return ResponseParam.success().datalist(syncUserClient.syncUser(syncDate));
    }
}
