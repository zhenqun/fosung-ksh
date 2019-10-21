package com.fosung.ksh.sys.sync.config;

import com.mzlion.easyokhttp.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangyihua
 * @date 2019-06-18 12:54
 */
@Slf4j
@Configuration
public class OkHttpConfig {
    OkHttpConfig() {
        HttpClient.Instance.connectTimeout(10000).readTimeout(1000 * 60 * 10);
    }
}
