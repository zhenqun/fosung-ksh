package com.fosung.ksh.monitor.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 监控设备信息
 *
 * @author wangyihua
 * @date 2019-05-09 18:03
 */
@Getter
@Setter
public class MonitorInfo implements Serializable {

    private static final long serialVersionUID = 2580804953174894950L;


    /**
     * cameraId : 1
     * indexcode :  16082611254372195927
     * name : test01
     * parentIndexCode :  0
     * cameraType : 0
     * pixel : 1
     * latitude : 11.2356
     * longitude : 14.4356
     * description : 测试01
     * isOnline : 1
     * controlUnitName : 杭州
     * decodeTag : hikvision
     * createTime : 1458715074229
     * updateTime : 1458715074229
     * extraField : extraFild
     */

    /**
     * 设备通道
     */
    private String cameraId;

    /**
     * 索引编号
     */
    private String indexCode;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 父级节点code
     */
    private String parentIndexCode;

    /**
     * 摄像机类型(0枪机,1半球,2快球3带云台枪机)
     */
    private String cameraType;

    /**
     * 摄像头像素
     */
    private String pixel;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 精度
     */
    private String longitude;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否在线(1在线,0不在线)
     */
    private String isOnline;

    /**
     * 控制中心名称
     */
    private String controlUnitName;

    /**
     * 解码标签
     */
    private String decodeTag;



}
