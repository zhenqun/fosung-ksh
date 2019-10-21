package com.fosung.ksh.oauth2.server.dt.surpport;

import com.fosung.ksh.oauth2.server.config.SSOProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public  class AppInterSSOUserAdapter extends AbstractBaseSSOUserAdapter {


    @Autowired
    private SSOProperties ssoProperties;

    /**
     * 获取互联网端
     * @return
     */
    @Override
    public SSOProperties.SSOConfig getSSOConfig() {
        return ssoProperties.getDt();
    }
}
