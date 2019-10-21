package com.fosung.ksh.monitor.service;

import com.fosung.ksh.monitor.dto.CameraHlsHistoryTotal;

/**
 * @program: fosung-ksh
 * @description: 视频流
 * @author: LZ
 * @create: 2019-05-10 09:34
 **/

public interface CameraVideoService {
    /**
     * 实时视频 根据监控点编号获取HLS（RTSP）地址(向下兼容用)
     */
    String getCameraVideoRealTime(String indexCode, String videoType);

    String refreshCameraVideoRealTime(String indexCode, String videoType);

    /**
     * 根据监控点编号和时间获取视频回放url和视频回放时间片段
     */
    CameraHlsHistoryTotal getCameraVideoHistory(String cameraIndexCode, String beginTime, String endTime);
}
