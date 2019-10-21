package com.fosung.ksh.monitor.service.impl;

import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.ksh.common.cache.RedisCacheable;
import com.fosung.ksh.monitor.config.MonitorProperties;
import com.fosung.ksh.monitor.http.CameraHlsHistoryResponse;
import com.fosung.ksh.monitor.dto.CameraHlsHistoryTotal;
import com.fosung.ksh.monitor.http.HikResponse;
import com.fosung.ksh.monitor.service.CameraVideoService;
import com.fosung.ksh.monitor.util.HikVisionArtemisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * @program: fosung-ksh
 * @description: 视频流实现类
 * @author: LZ
 * @create: 2019-05-10 09:38
 **/
@Slf4j
@Service
public class CameraVideoServiceImpl implements CameraVideoService {


//    @RedisCacheable(timeUnit = TimeUnit.HOURS, expired = 24, prefix = "video")
    @Cacheable(cacheNames = "video")
    @Override
    public String getCameraVideoRealTime(String indexCode, String videoType) {
        log.info("{}获取海康实时视频流，类型{}",indexCode,videoType);
        HikResponse response = doGetCameraVideoRealTime(indexCode,videoType);
        if (response.ok()) {
            return response.data(String.class);
        } else {
            log.error("海康获取实时视频流失败：{}",response.getMsg());
            throw new AppException(response.getMsg());
        }

    }

    /**
     * @Description: 刷新实时视频流
     * @author LZ
     * @date 2019-07-26 10:58
    */
    @CachePut(cacheNames = "video",unless ="#result.empty" )
    @Override
    public String refreshCameraVideoRealTime(String indexCode, String videoType) {
        log.info("{}获取海康实时视频流，类型{}",indexCode,videoType);
        HikResponse response = doGetCameraVideoRealTime(indexCode,videoType);
        if (response.ok()) {
            return response.data(String.class);
        }
        return "";
    }
    /**
     * @Description: 获取视频流
     * @author LZ
     * @date 2019-07-26 10:59
    */
    public HikResponse doGetCameraVideoRealTime(String indexCode, String videoType) {
        String resultStr = HikVisionArtemisUtil.getCameraVideoRealTime(indexCode, videoType);
        return JsonMapper.parseObject(resultStr, HikResponse.class);
    }

    @Override
    public CameraHlsHistoryTotal getCameraVideoHistory(String cameraIndexCode, String beginTime, String endTime) {
        String resultStr = HikVisionArtemisUtil.getCameraVideoHistory(cameraIndexCode, beginTime, endTime);
        CameraHlsHistoryResponse cameraHlsHistoryResponse = JsonMapper.parseObject(resultStr, CameraHlsHistoryResponse.class);
        if (cameraHlsHistoryResponse.ok()) {
            if (cameraHlsHistoryResponse.getIsWarning() == 1) {
                // TODO 预留 如果等于1 需要进行对rtsp://之后的字符进行转义，暂时还未发现
                return cameraHlsHistoryResponse.data(CameraHlsHistoryTotal.class);
            }
            return cameraHlsHistoryResponse.data(CameraHlsHistoryTotal.class);

        } else {
            log.error("海康获取历史视频流失败：{}", resultStr);
            if ("402".equals(cameraHlsHistoryResponse.getCode())) {
                throw new AppException("当前视频子");
            } else {

                throw new AppException(cameraHlsHistoryResponse.getMsg());
            }
        }

    }
}
