package com.fosung.ksh.attachment.service;

import com.fosung.framework.common.support.service.AppBaseDataService;
import com.fosung.ksh.attachment.entity.SysAttachment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface SysAttachmentService extends AppBaseDataService<SysAttachment, Long> {
    /**
     * 文件保存
     *
     * @param appAttachment
     * @param file
     * @return
     */
    SysAttachment saveAttachment(SysAttachment appAttachment,
                                 MultipartFile file) throws IOException;

    SysAttachment saveVideo(SysAttachment appAttachment,
                                 MultipartFile file) throws IOException, InterruptedException;

    /**
     * 图片保存
     * 图片保存会对图片进行压缩，压缩后的图片为jpg格式
     * 图片压缩使用的是谷歌Thumbnails工具，该工具对Png压缩存在BUG，所以图片都转换为jpg保存
     *
     * @param appAttachment
     * @param file
     * @return
     */
    SysAttachment saveImage(SysAttachment appAttachment,
                            MultipartFile file) throws IOException, ExecutionException, InterruptedException;

    /**
     * 图片批量上传
     * @param multipartFile
     * @return
     * @throws IOException
     */
    List<SysAttachment> save(MultipartFile multipartFile) throws IOException;

    /**
     * 加载文件
     * @param filePath
     * @return
     */
    byte[] loadFile(String filePath);
}
