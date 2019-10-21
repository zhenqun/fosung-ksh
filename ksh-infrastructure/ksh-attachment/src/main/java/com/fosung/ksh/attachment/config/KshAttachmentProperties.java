package com.fosung.ksh.attachment.config;

import com.fosung.framework.common.util.UtilString;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;
import java.io.IOException;

@ConfigurationProperties(prefix = KshAttachmentProperties.PREFIX)
@Data
@Slf4j
public class KshAttachmentProperties {

    /**
     * 配置信息的前缀
     */
    public static final String PREFIX = "ksh.attachment";

    /**
     * 临时文件上传路径
     */
    public static final String BASE_PATH = "/attachment/";

    /**
     * 文件上传参数名称
     */
    public static final String UPLOAD_FILE_PARAM = "file";

    // 用户文件存储路径
    private String storePath;

    private String downloadPath = BASE_PATH + "/download/{originName}.{extension}?id={id}";

    private String vadioPath = BASE_PATH + "/vadioload/{originName}.{extension}?id={id}";
   //视频转码后的地址
    private String targetPath;

    private String ffmpeg;

    private Image image = new Image();

    @Getter
    @Setter
    public class Image {
        /**
         * 压缩后文件宽度
         */
        private Integer width = 1920;

        /**
         * 压缩后文件高度
         */
        private Integer height = 1080;

        /**
         * 压缩后文件大小
         */
        private String zip = "200kb";
    }

    /**
     * 获取永久存储的文件路径
     *
     * @return
     */
    public String getStorePath() {
        // 默认使用jdk的用户文件文件路径
        if (UtilString.isBlank(storePath)) {
            storePath = FileUtils.getUserDirectoryPath();
        }

        // 路径默认都以 文件系统分隔符 结束
        if (!UtilString.endsWith(storePath, File.separator)) {
            storePath += File.separator;
        }

        log.debug("上传文件正式存储目录: {}", storePath);

        // 创建文件目录
        try {
            FileUtils.forceMkdir(new File(storePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return storePath;
    }

    /**
     * 获取以日期结束的存储路径
     *
     * @return
     */
    public String getStorePathWithDate() {
        String path = getStorePath();

        String pathWithDate = path + new DateTime().toString("yyyy-MM-dd") + File.separator;

        // 创建文件目录
        try {
            FileUtils.forceMkdir(new File(pathWithDate));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pathWithDate;
    }


}
