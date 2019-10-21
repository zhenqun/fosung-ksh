package com.fosung.ksh.attachment.service.impl;

import com.alibaba.fastjson.JSON;
import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.util.UtilString;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.attachment.config.KshAttachmentProperties;
import com.fosung.ksh.attachment.dao.SysAttachmentDao;
import com.fosung.ksh.attachment.entity.SysAttachment;
import com.fosung.ksh.attachment.service.SysAttachmentService;
import com.fosung.ksh.attachment.service.impl.image.ImageUtil;
import com.fosung.ksh.attachment.util.ConvertVideo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author wangyh
 */
@Slf4j
@Service
public class SysAttachmentServiceImpl extends AppJPABaseDataServiceImpl<SysAttachment, SysAttachmentDao> implements SysAttachmentService {


    @Autowired
    private KshAttachmentProperties attachmentProperties;

    @Autowired
    private ImageUtil imageUtil;


    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("businessIds", "businessId:IN");
            put("type", "type:EQ");
            put("businessId", "businessId:EQ");
            put("extensionField1", "extensionField1:EQ");
            put("extensionField2", "extensionField2:EQ");
            put("extensionField3", "extensionField3:EQ");
            put("extensionField4", "extensionField4:EQ");
        }
    };


    /**
     * 文件保存
     *
     * @param appAttachment
     * @param file
     * @return
     */
    @Override
    public SysAttachment saveAttachment(SysAttachment appAttachment,
                                        MultipartFile file) throws IOException {
        //文件原名
        String originalFilename = file.getOriginalFilename();

        // 文件扩展名
        String fileExtension = FilenameUtils.getExtension(originalFilename);

        //文件存储路径,以日期进行存储
        String pathWithDate = attachmentProperties.getStorePathWithDate();

        //新文件名称通过uuid生成
        String newFileName = UUID.randomUUID().toString();


        if (UtilString.isNotEmpty(fileExtension)) {
            newFileName += "." + fileExtension;
        }

        // 新文件存储路径
        String newFilePath = pathWithDate + newFileName;
        File newFile = new File(newFilePath);


        //保存文件
        FileUtils.copyToFile(file.getInputStream(), newFile);
//        save(appAttachment);
        saveAttachment(appAttachment, originalFilename, fileExtension, newFileName, newFilePath, newFile);
        return appAttachment;
    }

    @Override
    public SysAttachment saveVideo(SysAttachment appAttachment, MultipartFile file) throws IOException, InterruptedException {
        //文件原名
        String originalFilename = file.getOriginalFilename();

        // 文件扩展名
        String fileExtension = FilenameUtils.getExtension(originalFilename);

        //文件存储路径,以日期进行存储
        String pathWithDate = attachmentProperties.getStorePath();
        //新文件名称通过uuid生成
        String newFileName = UUID.randomUUID().toString();
        if (UtilString.isNotEmpty(fileExtension)) {
            newFileName += "." + fileExtension;
        }

        // 新文件存储路径
        String newFilePath = pathWithDate + newFileName;
        File newFile = new File(newFilePath);
        FileUtils.copyToFile(file.getInputStream(), newFile);

        String ffmpegPath = attachmentProperties.getFfmpeg();
        ConvertVideo convert = new ConvertVideo(ffmpegPath);
        String targetName = UUID.randomUUID().toString()+".m3u8";
        //转换地址
        String targetPath=pathWithDate.replace("attachment", "m3u8");
        File targetFile =new File(targetPath);
        String targetFilePath = targetPath + targetName;
        convert.start(newFilePath, targetFilePath);
        saveAttachmentByVadio(appAttachment, originalFilename, "m3u8", targetName, targetFilePath,targetFile);
        return appAttachment;
    }


    /**
     * 图片保存
     * 图片保存会对图片进行压缩，压缩后的图片为jpg格式
     * 图片压缩使用的是谷歌Thumbnails工具，该工具对Png压缩存在BUG，所以图片都转换为jpg保存
     *
     * @param appAttachment
     * @param file
     * @return
     */
    @Override
    public SysAttachment saveImage(SysAttachment appAttachment,
                                   MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        //文件原名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        // 文件扩展名
        String fileExtension = suffix;

        //文件存储路径
        String pathWithDate = attachmentProperties.getStorePathWithDate();

        //新文件名称
        String newFileName = UUID.randomUUID().toString() +"."+ suffix;

        // 新文件存储路径
        String newFilePath = pathWithDate + newFileName;

        File newFile = new File(newFilePath);

        //保存文件
        Future<Boolean> future = imageUtil.thumbnails(newFilePath, file.getInputStream(),suffix);
//        Thumbnails.of(file.getInputStream()).size(attachmentProperties.getImage().getWidth(), attachmentProperties.getImage().getHeight()).outputFormat("jpg").toFile(newFile);
        if (future.get()) {
            saveAttachment(appAttachment, originalFilename, fileExtension, newFileName, newFilePath, newFile);
            return appAttachment;
        }
        throw new AppException("文件上传失败");
    }


    /**
     * 图片批量上传
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @Override
    public List<SysAttachment> save(MultipartFile multipartFile) throws IOException {
        List<SysAttachment> appAttachmentList = handlerUploadFile(multipartFile);

        //WC ，必须保存后再修改
        appAttachmentList.forEach(appAttachment -> {
            this.entityDao.save(appAttachment);
            // 设置文件下载路径，考虑到下载路径中可能会带有记录的id，所有保存之后再执行一次更新操作
            appAttachment.setDownloadPath(formatDownloadPath(appAttachment));
            // 更新下载路径
            this.update(appAttachment, Arrays.asList("downloadPath"));
            log.debug("保存附件成功后，去修改附件下载地址\n{}", JSON.toJSONString(appAttachment, true));
        });
        return appAttachmentList;
    }


    private List<SysAttachment> handlerUploadFile(MultipartFile multipartFile) throws IOException {
        List<SysAttachment> list = null;
        // 兼容mac 适配不同编码
        try {
            ZipInputStream zis = new ZipInputStream(multipartFile.getInputStream(), Charset.forName("GBK"));
            list = this.getAppAttachmentDTOS(zis);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            ZipInputStream zis = new ZipInputStream(multipartFile.getInputStream());
            list = this.getAppAttachmentDTOS(zis);
        }
        return list;
    }

    /**
     * @param zis
     * @return
     * @throws IOException
     */
    private List<SysAttachment> getAppAttachmentDTOS(ZipInputStream zis) throws IOException {
        List<SysAttachment> appAttachmentList = Lists.newArrayList();
        BufferedInputStream bis = new BufferedInputStream(zis);

        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            String zeName = ze.getName();

            // 过滤文件夹和mac系统上传时导致的附件
            if (!ze.isDirectory() && zeName.toUpperCase().indexOf("_MACOSX") == -1) {

                // 文件名
                String fileName = FilenameUtils.getBaseName(zeName);

                //文件扩展类型
                String fileExtension = FilenameUtils.getExtension(zeName);

                //文件存储路径
                String pathWithDate = attachmentProperties.getStorePathWithDate();

                //新文件名称
                String newFileName = UUID.randomUUID().toString();

                if (UtilString.isNotEmpty(fileExtension)) {
                    newFileName += "." + fileExtension;
                }

                // 新文件存储路径
                String newFilePath = pathWithDate + newFileName;

                File tmpFile = new File(attachmentProperties.getStorePath() + "tmp" + File.separator + newFileName);

                copyFileToHDD(zis, bis, tmpFile);
                imageUtil.thumbnails(newFilePath, tmpFile);
                addSysAttachment(appAttachmentList, ze, fileName, fileExtension, newFileName, newFilePath);

            }

        }
        bis.close();
        zis.close();
        return appAttachmentList;
    }


    /**
     * 生成上传数据BEAN
     *
     * @param appAttachmentList
     * @param ze
     * @param fileName
     * @param fileExtension
     * @param newFileName
     * @param newFilePath
     */
    private void addSysAttachment(List<SysAttachment> appAttachmentList, ZipEntry ze, String fileName, String fileExtension, String newFileName, String newFilePath) {
        SysAttachment appAttachment = new SysAttachment();
        if(fileName.length() < 18){
            throw  new AppException("上传失败！文件名" + fileName + "错误。");
        }

        String userName = fileName.substring(18);
        String idCard = fileName.substring(0, 18);
        appAttachment.setBusinessId(idCard + userName);
        appAttachment.setBusinessName("人脸");
        appAttachment.setType("FACE");
        appAttachment.setExtension(fileExtension);
        appAttachment.setOriginName(fileName);
        appAttachment.setSize(ze.getSize());
        appAttachment.setStorageName(newFileName);
        appAttachment.setStoragePath(newFilePath);
        appAttachment.setDownloadPath(formatDownloadPath(appAttachment));
        appAttachmentList.add(appAttachment);
    }

    /**
     * 将文件保存到硬盘
     *
     * @param zis
     * @param bis
     * @param newFilePath
     * @throws IOException
     */
    private void copyFileToHDD(ZipInputStream zis, BufferedInputStream bis, File newFile) throws IOException {

        //保存文件
        FileOutputStream fos = new FileOutputStream(newFile);
        int len;
        byte[] buf = new byte[1024];
        while ((len = bis.read(buf)) != -1) {
            fos.write(buf, 0, len);
        }
        fos.flush();
        fos.close();
        zis.closeEntry();
    }


    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }


    /**
     * 保存文件到数据库中
     *
     * @param appAttachment
     * @param originalFilename 原文件名
     * @param fileExtension    文件扩展名
     * @param newFileName      新文件名
     * @param newFilePath      新文件保存路径
     * @param newFile          新文件
     */
    private void saveAttachment(SysAttachment appAttachment, String originalFilename, String fileExtension, String newFileName, String newFilePath, File newFile) {
        // 执行实际文件内容的保存操作
        long fileSize = newFile.length();

        // 重置id
        appAttachment.setId(null);
        appAttachment.setOriginName(FilenameUtils.getBaseName(originalFilename.toLowerCase()));
        appAttachment.setStorageName(newFileName);
        appAttachment.setSize(fileSize);
        appAttachment.setExtension(fileExtension.toLowerCase());

        // 文件存储路径
        appAttachment.setStoragePath(newFilePath);

        // 保存临时文件入库
        this.save(appAttachment);

        // 设置文件下载路径，考虑到下载路径中可能会带有记录的id，所有保存之后再执行一次更新操作
        appAttachment.setDownloadPath(formatDownloadPath(appAttachment));

        // 更新下载路径
        this.update(appAttachment, Arrays.asList("downloadPath"));
    }
    private void saveAttachmentByVadio(SysAttachment appAttachment, String originalFilename, String fileExtension, String newFileName, String newFilePath, File newFile) {
        // 执行实际文件内容的保存操作
        long fileSize = newFile.length();

        // 重置id
        appAttachment.setId(null);
        appAttachment.setOriginName(FilenameUtils.getBaseName(originalFilename.toLowerCase()));
        appAttachment.setStorageName(newFileName);
        appAttachment.setSize(fileSize);
        appAttachment.setExtension(fileExtension.toLowerCase());

        // 文件存储路径
        appAttachment.setStoragePath(newFilePath);

        // 保存临时文件入库
        this.save(appAttachment);

        // 设置文件下载路径，考虑到下载路径中可能会带有记录的id，所有保存之后再执行一次更新操作
        appAttachment.setDownloadPath(formatVadioPath(appAttachment));

        // 更新下载路径
        this.update(appAttachment, Arrays.asList("downloadPath"));
    }

    public String formatVadioPath(SysAttachment appAttachment) {
        Map<String, Object> uriVariables = Maps.newHashMap();

        try {
            uriVariables = PropertyUtils.describe(appAttachment);
        } catch (Exception e) {
            log.error("格式化下载地址异常", ExceptionUtils.getStackTrace(e));
        }
        String path=attachmentProperties.getDownloadPath();
        String uri = UriComponentsBuilder.fromUriString(attachmentProperties.getVadioPath())
                .uriVariables(uriVariables).build().toUriString();

        return uri;
    }
    /**
     * 格式化下载路径
     *
     * @param appAttachment
     * @return
     */
    public String formatDownloadPath(SysAttachment appAttachment) {
        Map<String, Object> uriVariables = Maps.newHashMap();

        try {
            uriVariables = PropertyUtils.describe(appAttachment);
        } catch (Exception e) {
            log.error("格式化下载地址异常", ExceptionUtils.getStackTrace(e));
        }
        String path=attachmentProperties.getDownloadPath();
        String uri = UriComponentsBuilder.fromUriString(attachmentProperties.getDownloadPath())
                .uriVariables(uriVariables).build().toUriString();

        return uri;
    }

    /**
     * 加载下载文件
     *
     * @param filePath
     * @return
     */
    @Override
    public byte[] loadFile(String filePath) {
        if (UtilString.isBlank(filePath)) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        byte[] result = null;
        try {
            result = FileCopyUtils.copyToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
