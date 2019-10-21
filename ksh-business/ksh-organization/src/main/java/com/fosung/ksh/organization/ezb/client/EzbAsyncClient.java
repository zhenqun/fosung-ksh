package com.fosung.ksh.organization.ezb.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.common.util.UtilDate;
import com.fosung.ksh.organization.ezb.EzbProperties;
import com.fosung.ksh.organization.ezb.dto.AttachmentDTO;
import com.fosung.ksh.organization.ezb.dto.ClassificationDTO;
import com.fosung.ksh.organization.ezb.dto.OrgLifeMeetingDTO;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.mzlion.core.http.ContentType;
import com.mzlion.core.json.TypeRef;
import com.mzlion.easyokhttp.HttpClient;
import com.mzlion.easyokhttp.http.DebugLoggingInterceptor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.BufferedSink;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * E支部数据同步接口
 *
 * @author wangyh
 */
@Slf4j
@Component
public class EzbAsyncClient {

    @Autowired
    private EzbProperties ezbProperties;

    /**
     * 创建组织生活数据
     * 此处使用原生OKHTT，使用EASYPKHTTP时会丢失请求错误时返回信息
     *
     * @param meetingDto 数据
     * @return
     */
    public void createMeeting(OrgLifeMeetingDTO meetingDto) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();
        String url = ezbProperties.getServerUrl() + ezbProperties.getCreateMeeting();
        String content = JsonMapper.toJSONString(meetingDto);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), content);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-Branch-Id", meetingDto.getBranchId())
                .post(requestBody)
                .build();
        Response response = null;
        response = httpClient.newCall(request).execute();
        if (response.code() != HttpStatus.OK.value()) {
            JSONObject json = JSONObject.parseObject(response.body().string());
            throw new AppException("组织生活同步到E支部失败：" + json.getString("message"));
        }
        log.debug("组织生活创建结果：{},返回值：{}", meetingDto, response.body().string());
    }

    /**
     * 发布组织生活数据
     *
     * @param meetingDto
     * @throws IOException
     */
    public void push(OrgLifeMeetingDTO meetingDto) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();
        String url = ezbProperties.getServerUrl() + ezbProperties.getReleaseDeleteMeeting();
        JSONObject json = new JSONObject();
        json.put("delFlag", "0");
        json.put("meetingId", meetingDto.getMeetingId());
        json.put("publishFlag", "1");
        json.put("publishTime", UtilDate.getCurrentDateFormatStr("yyyy-MM-dd HH:mm:ss"));
        json.put("orgCode", meetingDto.getOrgCode());
        json.put("branchName", meetingDto.getBranchName());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json.toJSONString());
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-Branch-Id", meetingDto.getBranchId())
                .post(requestBody)
                .build();
        Response response = null;
        response = httpClient.newCall(request).execute();
        if (response.code() != HttpStatus.OK.value()) {
            json = JSONObject.parseObject(response.body().string());
            throw new AppException("组织生活发布到E支部失败：" + json.getString("message"));
        }
        log.debug("组织生活发布结果：{},返回值：{}", meetingDto, response.body().string());

    }


    /**
     * 查询组织生活类型
     *
     * @param orgCode
     * @return
     */
    public List<ClassificationDTO> queryClassificationList(String orgCode)  throws IOException{
        OkHttpClient httpClient = new OkHttpClient();
        Map<String, Object> uriVariables = Maps.newHashMap();
        uriVariables.put("orgCode", orgCode);
        String url = ezbProperties.getServerUrl() + ezbProperties.getGetMeetingClassificationList();
        url = UriComponentsBuilder.fromUriString(url)
                .uriVariables(uriVariables).build().toUriString();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = null;
        response = httpClient.newCall(request).execute();
        if (response.code() != HttpStatus.OK.value()) {
            JSONObject resout = JSONObject.parseObject(response.body().string());
            throw new AppException("组织生活同步到E支部失败：" + resout.getString("message"));
        }
        JSONArray resout = JSONArray.parseArray(response.body().string());
        List<ClassificationDTO> list = JSONObject.parseArray(resout.toJSONString(), ClassificationDTO.class);


//        log.debug("组织生活创建结果：{},返回值：{}", orgCode, response.body().string());
        return list;
    }


    /**
     * 将文件异步上传至E支部文件服务器
     *
     * @param attachmentDto
     * @return
     * @throws MalformedURLException
     */
    @Async
    public Future<AttachmentDTO> uploadFileToEzb(AttachmentDTO attachmentDto) throws IOException {
        Future<AttachmentDTO> future;
        attachmentDto = upload(attachmentDto);
        future = new AsyncResult<>(attachmentDto);
        return future;
    }


    /**
     * 文件上传具体操作
     *
     * @param attachmentDto
     * @return
     * @throws MalformedURLException
     */
    public AttachmentDTO upload(AttachmentDTO attachmentDto) throws IOException {
        String url = attachmentDto.getAttachmentAddr();
        String fileName = attachmentDto.getAttachmentRelName();
        String fileType = FilenameUtils.getExtension(fileName);
        String newFile = fileName;
        File file = new File(ezbProperties.getTemp(), UUID.randomUUID().toString() + File.separator + newFile);
        FileUtils.copyURLToFile(new URL(url), file);

        JSONObject responseParam = uploadFile(file);

        List<Map<String, String>> list = (List) responseParam.get("files");
        if (UtilCollection.isNotEmpty(list)) {
            Map<String, String> map = list.get(0);
            attachmentDto.setAttachmentAddr(map.get("address"));
            attachmentDto.setAttachmentRelName(map.get("fileName"));
        }
        return attachmentDto;
    }


    /**
     * 文件上传
     *
     * @param file     文件数据
     * @param fileName 文件名称
     * @return
     * @throws IOException
     */
    public JSONObject uploadFile(File file) throws IOException{
        String url = ezbProperties.getUploadFiles();
//        JSONObject res = HttpClient.post(url).param("file", file).asBean(JSONObject.class);

        RestTemplate restTemplate = new RestTemplate();

        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        org.springframework.http.MediaType type = org.springframework.http.MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);

        //设置请求体，注意是LinkedMultiValueMap
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        FileSystemResource resource = new FileSystemResource(file);
        form.add("file", resource);
        //用HttpEntity封装整个请求报文
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        JSONObject res = restTemplate.postForObject(url, files, JSONObject.class);
//        JSONObject res = HttpClient.post(url).param("file", file).asBean(JSONObject.class);
        file.getParentFile().delete();
        file.delete();
        log.debug("E支部文件上传结果：{}", res);
        return res;
    }

}
