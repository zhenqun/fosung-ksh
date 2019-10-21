package com.fosung.ksh.attachment.controller;

import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.web.util.UtilMediaType;
import com.fosung.ksh.attachment.config.KshAttachmentProperties;
import com.fosung.ksh.attachment.entity.SysAttachment;
import com.fosung.ksh.attachment.service.SysAttachmentService;
import com.fosung.ksh.common.response.Result;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * 临时文件上传处理
 *
 * @Author : liupeng
 * @Date : 2018-11-05
 * @Modified By
 */
@Slf4j
@CrossOrigin(methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS}, allowedHeaders = {
        "content-type",
        "xfilename",
        "xfilecategory",
        "xfilesize",
        "XMLHttpRequest",
        "x-requested-with",
        "x-token"
})
@RestController
@RequestMapping(value = "/app" + KshAttachmentProperties.BASE_PATH)
public class SysAttachmentController extends AppIBaseController {

    @Autowired
    private SysAttachmentService attachmentService;

    @Autowired
    private KshAttachmentProperties attachmentProperties;
    /**
     * 记录分页查询
     *
     * @param pageNum  分页号，由0开始
     * @param pageSize 每页记录数，默认为10
     * @return
     * @throws Exception
     */
    @ApiOperation("根据参数，查询文件上传列表")
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public ResponseEntity<Page<SysAttachment>> query(
            @RequestParam(required = false, value = "pagenum", defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
            @RequestParam(required = false, value = "pagesize", defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize) {
        //获取查询参数
        Map<String, Object> searchParam = getParametersStartingWith(getHttpServletRequest(), DEFAULT_SEARCH_PREFIX);
        //执行分页查询
        Page<SysAttachment> page = attachmentService.queryByPage(searchParam, pageNum, pageSize);

        return Result.success(page);
    }

    /**
     * 普通文件文件上传
     *
     * @return
     */
    @ApiOperation("文件上传")
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public ResponseEntity<SysAttachment> upload(MultipartRequest multipartRequest,
                                                SysAttachment appAttachmentEntity) throws IOException {
        //获取上传文件
        MultipartFile multipartFile = multipartRequest.getFile(KshAttachmentProperties.UPLOAD_FILE_PARAM);

        attachmentService.saveAttachment(appAttachmentEntity,
                multipartFile);

        appAttachmentEntity.setStoragePath("");
        return Result.success(appAttachmentEntity);
    }



    @ApiOperation("视频上传")
    @RequestMapping(value = "videoUpload",method = RequestMethod.POST)
    public ResponseEntity<SysAttachment> videoUpload(MultipartRequest multipartRequest,SysAttachment appAttachmentEntity) throws IOException, InterruptedException {
        //获取上传文件
      MultipartFile multipartFile = multipartRequest.getFile(KshAttachmentProperties.UPLOAD_FILE_PARAM);
      attachmentService.saveVideo(appAttachmentEntity, multipartFile);
      return Result.success(appAttachmentEntity);
    }
    /**
     * 图片上传，并对图片进行压缩
     *
     * @return
     */
    @ApiOperation("图片上传，将会对图片进行压缩")
    @RequestMapping(value = "upload/image", method = RequestMethod.POST)
    public ResponseEntity<SysAttachment> image(MultipartRequest multipartRequest,
                                               SysAttachment appAttachmentEntity) throws IOException, ExecutionException, InterruptedException {

        //获取上传文件
        MultipartFile multipartFile = multipartRequest.getFile(KshAttachmentProperties.UPLOAD_FILE_PARAM);

        appAttachmentEntity = attachmentService.saveImage(appAttachmentEntity,
                multipartFile);

        appAttachmentEntity.setStoragePath("");
        return Result.success(appAttachmentEntity);
    }

    /**
     * 图片上传，并对图片进行压缩
     *
     * @return
     */
    @ApiOperation("zip压缩包上传")
    @RequestMapping(value = "upload/zip", method = RequestMethod.POST)
    public ResponseEntity<List<SysAttachment>> image(MultipartRequest multipartRequest) {
        List<SysAttachment> appAttachmentDTOS = null;
        try {
            //获取上传文件
            MultipartFile multipartFile = multipartRequest.getFile(KshAttachmentProperties.UPLOAD_FILE_PARAM);
            appAttachmentDTOS = attachmentService.save(multipartFile);

        }catch (AppException e) {
            e.printStackTrace();
            // 处理 getBytes 异常
            return Result.failure(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            // 处理 getBytes 异常
            return Result.failure("获取上传文件内容失败");
        } catch (Exception e) {
            e.printStackTrace();
            // 处理文件上传处理的异常
            return Result.failure("上传文件处理异常");
        }
        return Result.success(appAttachmentDTOS);
    }

    @ApiOperation("视频下载")
    @RequestMapping(value = "/vadioload/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public Object vadioload(@PathVariable String id){
        byte[] fileByte =null;
        String fileFullName="";
        String extension="";
        if(id.indexOf("ts")!=-1){
            String path=attachmentProperties.getTargetPath();
            String storePath=path+"\\"+id;
            fileByte = attachmentService.loadFile(storePath);
            fileFullName=id;
            extension="ts";
        }else{
            SysAttachment appAttachment = attachmentService.get(Long.parseLong(id));
            fileFullName = appAttachment.getOriginName() + "." + appAttachment.getExtension();
            log.debug("下载文件：{} , 文件存储信息: {}", fileFullName, JsonMapper.toJSONString(appAttachment));
            fileByte = attachmentService.loadFile(appAttachment.getStoragePath());
            extension=appAttachment.getExtension();
        }
        if (fileByte == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            String downLoadName = new String(fileFullName.getBytes("GBK"), "iso8859-1");
            MediaType mediaType = UtilMediaType.parseMediaType(extension);
            ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(HttpStatus.OK).contentType(mediaType);
             // 针对流下载的方式，设置下载文件名称
            if (mediaType == MediaType.APPLICATION_OCTET_STREAM) {
                bodyBuilder.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + downLoadName);
            }

            return bodyBuilder.body(fileByte);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    /**
     * 下载文件，下载地址应该与 SysAttachmentProperties 中配置的 downloadPath 相同
     *
     * @param id 附件id
     * @return
     */
    @ApiOperation("文件下载")
    @RequestMapping(value = "/download/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public Object download(@PathVariable Long id) {
        SysAttachment appAttachment = attachmentService.get(id);
        String fileFullName = appAttachment.getOriginName() + "." + appAttachment.getExtension();

        log.debug("下载文件：{} , 文件存储信息: {}", fileFullName, JsonMapper.toJSONString(appAttachment));

        byte[] fileByte = attachmentService.loadFile(appAttachment.getStoragePath());
        if (fileByte == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            String downLoadName = new String(fileFullName.getBytes("GBK"), "iso8859-1");

            MediaType mediaType = UtilMediaType.parseMediaType(appAttachment.getExtension());

            ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(HttpStatus.OK).contentType(mediaType);

            // 针对流下载的方式，设置下载文件名称
            if (mediaType == MediaType.APPLICATION_OCTET_STREAM) {
                bodyBuilder.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + downLoadName);
            }

            return bodyBuilder.body(fileByte);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ApiOperation("文件批量下载")
    @RequestMapping(value = "/downloads")
    public Object download(@NotNull @Size(min = 1) @RequestParam Set<Long> ids) {
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("businessIds", ids);
        List<SysAttachment> appAttachment = attachmentService.queryAll(searchMap);
        if (UtilCollection.isEmpty(appAttachment)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        byte[] fileByte = this.attachment2Byte(appAttachment);
        if (fileByte == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            String downLoadName = new String((System.currentTimeMillis() + ".zip").getBytes("GBK"), "iso8859-1");
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(HttpStatus.OK).contentType(mediaType);
            bodyBuilder.header(HttpHeaders.CONTENT_TYPE, "application/zip");
            // 针对流下载的方式，设置下载文件名称
            bodyBuilder.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + downLoadName);
            return bodyBuilder.body(fileByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    private static final String CHINESE_CHARSET = "GBK";
    private static final int CACHE_SIZE = 1024;
    private byte[] cache = new byte[CACHE_SIZE];

    /**
     * 将附件list转换为zip压缩包字节数据
     */
    private byte[] attachment2Byte(List<SysAttachment> appAttachment) {
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(out);
        ) {
            // 解决中文文件名乱码
            zos.setEncoding(CHINESE_CHARSET);
            for (SysAttachment file : appAttachment) {
                try (
                        InputStream is = new FileInputStream(file.getStoragePath());
                        BufferedInputStream bis = new BufferedInputStream(is);
                ) {
                    zos.putNextEntry(new ZipEntry(file.getStorageName()));
                    int nRead = 0;
                    while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
                        zos.write(cache, 0, nRead);
                    }
                    zos.closeEntry();
                    zos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            zos.finish();
            out.flush();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //文件删除
    @PostMapping("dels")
    public ResponseEntity dels(@RequestParam(value = "ids") String ids){
        System.out.println(ids);
        String[] idAll=ids.split(",");
        for (int i=0;i<idAll.length;i++){
            long id=Long.parseLong(idAll[i]);
            attachmentService.delete(id);
        }
        return Result.success();
    }


    //视频转换
}


