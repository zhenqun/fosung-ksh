package com.fosung.ksh.organization.ezb;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 同步Ezb所需的基本配置
 *
 * @author wangyh
 * @date 2018/12/01 11:42
 */
@ConfigurationProperties(prefix = "ksh.ezb")
@Data
public class EzbProperties {

    private ConnectionConfig connection = new ConnectionConfig() ;


    /**
     * 组织生活服务地址，默认为测试地址
     */
    private String serverUrl = "http://10.254.23.62:7412/manage";

//    private String serverUrl = "http://lzksh.fosung.com:98/manage";
    /**
     * 创建组织生活方法
     */
    private String createMeeting = "/createMeeting";

    /**
     * 修改组织生活方法
     */
    private String updateMeeting = "/updateMeeting";

    /**
     * 获取组织生活类型
     */
    private String getMeetingClassificationList = "/getMeetingClassificationList/{orgCode}";


    /**
     * 数据发布
     */
    private String releaseDeleteMeeting = "/releaseDeleteMeeting";


    /**
     * 文件上传服务地址
     */
    private String  uploadFiles =  "http://10.254.23.84:6411/native/app/uploadFiles?pathType=1";
//    private String  uploadFiles =  "http://ezb.dtdjzx.gov.cn/native/app/uploadFiles?pathType=1";

    /**
     * 文件下载临时保存路径
     */
    private String temp = "temp";

    /**
     * 灯塔连接配置
     */
    @Getter
    @Setter
    public static class ConnectionConfig{
        /**
         * 连接超时时间，单位秒
         */
        private Integer connectTimeout  = 3 ;

        /**
         * 读取超时时间，单位秒
         */
        private Integer readTimeout  = 5 ;
    }
}


