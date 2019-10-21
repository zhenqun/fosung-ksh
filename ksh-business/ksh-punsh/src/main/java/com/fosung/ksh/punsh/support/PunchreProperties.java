package com.fosung.ksh.punsh.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = PunchreProperties.PREFIX )
@Setter
@Getter
public class PunchreProperties {
    public static final String PREFIX = "puncher";
    /**
     * 导出文件配置
     */
    public ExportConfig exportConfig = new ExportConfig();

    @Getter
    @Setter
    public static class ExportConfig {
        private String title = "考勤记录统计";
        private String sheetName = "考勤记录统计";
        private String fileName = "考勤记录统计.xlsx";
//        private String ogravgfileName = "党组织党员党性自检结果平均分.xlsx";
    }
}
