package com.fosung.ksh.monitor.http;

import com.fosung.ksh.monitor.http.HikResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 历史视频流响应对象
 */
@Data
public class CameraHlsHistoryResponse extends HikResponse {

    /**
     * 注意
     * 提示字段，该字段为1时，表示url中密码有特殊字符，
     * 需要将url中“rtsp://”之后的字符全部转义，vlc才能直接播放；
     * 当没有该字段，或字段为0时，可直接贴到vlc播放
     */
    private Integer isWarning;

}
