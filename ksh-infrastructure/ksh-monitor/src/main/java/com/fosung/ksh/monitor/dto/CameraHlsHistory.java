package com.fosung.ksh.monitor.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 回放的分段录像
 */
@Getter
@Setter
public class CameraHlsHistory implements Serializable {
    private static final long serialVersionUID = -8402950835434864513L;
    /**
     * 录像开始时间
     * 示例：1511798463
     */
    private String beginTime;
    /**
     * 录像结束时间
     * 示例：1511884799
     */
    private String endTime;
    /**
     * 该分段录像的回放url。
     * rtsp://10.19.139.12:554/ncg_file://10.67.180.15:7099:0:cameraId?range=20171128T200010Z-20171128T035009Z&checkinFo=jkkHLOUWQo84JGlu&idinfo=jkkHLOUWQo84JGluFNg4
     */
    private String playbackUrl;

}
