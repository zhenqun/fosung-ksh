package com.fosung.ksh.monitor.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 这是根据开始时间和结束时间一个总的录像
 * 历史视频data响应对象接受实体
 */
@Getter
@Setter
@ToString
public class CameraHlsHistoryTotal implements Serializable {
    private static final long serialVersionUID = -8995814165948005838L;
    /**
     * 返回结果个数
     * 示例：10
     */
    private Integer total;
    /**
     * 总的播放url,用该url即可播放整个录像
     * rtsp://10.19.139.12:554/ncg_file://10.67.180.15:7099:0:cameraId?range=20171128T200010Z-20171128T205009Z&checkinFo=jkkHLOUWQo84JGlu&idinfo=jkkHLOUWQo84JGluFNg4
     */
    private String totalTimeUrl;

    /**
     * 总的播放url,用该url即可播放整个录像
     * rtsp://10.19.139.12:554/ncg_file://10.67.180.15:7099:0:cameraId?range=20171128T200010Z-20171128T205009Z&checkinFo=jkkHLOUWQo84JGlu&idinfo=jkkHLOUWQo84JGluFNg4
     */
    private List<CameraHlsHistory> list;
}
