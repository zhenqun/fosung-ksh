package com.fosung.ksh.oauth2.client.support;

import com.fosung.framework.common.secure.auth.AppUserDetails;
import com.fosung.framework.common.secure.auth.AppUserDetailsDefault;
import com.fosung.framework.common.secure.auth.AppUserDetailsServiceDefault;
import com.fosung.framework.common.util.UtilString;
import com.fosung.ksh.oauth2.client.Oauth2Client;
import com.fosung.ksh.oauth2.client.dto.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * 用户加载服务
 *
 * @Author : liupeng
 * @Date : 2018-10-16
 * @Modified By
 */
@Slf4j
@Service
public class AppSSOUserDetailsService extends AppUserDetailsServiceDefault {


    @Autowired(required = false)
    Oauth2Client oauth2Client;

    public AppUserDetails getAppUserDetails() {
        if (oauth2Client != null) {
            SysUser sysUser = null;
            HttpServletRequest request = RequestContextHolder.getRequestAttributes() != null ? ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest() : null;
            try {
                if (request != null && UtilString.isNotBlank(request.getHeader("x-token"))) {
                    sysUser = oauth2Client.userInfo();
                }
            } catch (Exception e) {
                log.debug(ExceptionUtils.getStackTrace(e));
            }
            if (sysUser == null) {
                log.debug("获取用户信息 authentication=null");
                return null;
            }
            AppUserDetailsDefault userDetails = new AppUserDetailsDefault(sysUser.getUsername());
            userDetails.setEnabled(true);
            userDetails.setTelephone(sysUser.getTelephone());
            userDetails.setUserRealName(sysUser.getRealName());
            userDetails.setUserId(sysUser.getId());
            userDetails.setOrgId(sysUser.getOrgId());
            userDetails.setHash(sysUser.getHash());
            userDetails.setUserSource(sysUser.getUserSource());
            return userDetails;


        } else {
            log.debug("oauth2Client is null");
            return null;
        }

    }

    @Override
    public void loadUserProperties(AppUserDetailsDefault appUserDetailsDefault) {
        super.loadUserProperties(appUserDetailsDefault);
    }


}
