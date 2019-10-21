package com.fosung.ksh.attachment.service.impl.image;

import com.fosung.ksh.attachment.config.KshAttachmentProperties;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Future;

/**
 * @author wangyihua
 * @date 2019-05-14 17:12
 */
@Slf4j
@Component
public class ImageUtil {


    @Autowired
    private KshAttachmentProperties attachmentProperties;

    @Async
    public Future<Boolean> thumbnails(String newFilePath, File tmpFile) {
        log.debug("保存文件：{}", newFilePath);
        try {
            Thumbnails.of(tmpFile).size(attachmentProperties.getImage().getWidth(), attachmentProperties.getImage().getHeight()).outputFormat("jpg").toFile(newFilePath);
            return new AsyncResult<Boolean>(true);
        } catch (Exception e) {
            log.error("\n文件压缩失败,\n压缩后文件：{},\n错误信息：{}", newFilePath, ExceptionUtils.getStackTrace(e));
            return new AsyncResult<Boolean>(false);
        }
    }

    @Async
    public Future<Boolean> thumbnails(String newFilePath, InputStream inputStream,String suffix) {
        try {
            Thumbnails.of(inputStream).size(attachmentProperties.getImage().getWidth(), attachmentProperties.getImage().getHeight()).outputFormat(suffix).toFile(newFilePath);
            return new AsyncResult<Boolean>(true);
        } catch (Exception e) {
            log.error("\n文件压缩失败,\n压缩后文件：{},\n错误信息：{}", newFilePath, ExceptionUtils.getStackTrace(e));
            return new AsyncResult<Boolean>(false);
        }
    }
}
