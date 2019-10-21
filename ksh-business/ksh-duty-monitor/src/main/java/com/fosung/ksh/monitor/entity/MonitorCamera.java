package com.fosung.ksh.monitor.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fosung.framework.common.config.AppProperties;
import com.fosung.framework.common.support.dao.entity.AppJpaIdEntity;
import com.fosung.framework.common.support.dao.entity.AppJpaSoftDelEntity;
import com.fosung.ksh.monitor.entity.constant.MonitorStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 监控摄像头清单
 *
 * @author wangyihua
 * @date 2019-05-08 11:25
 */
@Entity
@Table(name = "monitor_camera")
@Setter
@Getter
public class MonitorCamera extends AppJpaIdEntity  {

    /**
     * 设备所属行政区划
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "village_id")
    private Long villageId;



    /**
     * 是否在线
     */
    @Column(name = "monitor_status")
    @Enumerated(value = EnumType.STRING)
    private MonitorStatus monitorStatus;

    /**
     * 海康设备ID
     */
    @Column(name = "camera_id")
    private String cameraId;

    /**
     * 设备索引编码
     */
    @Column(name = "index_code")
    private String indexCode;

    /**
     * 设备名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 父级节点code
     */
    @Column(name = "parent_index_code")
    private String parentIndexCode;

    /**
     * 摄像机类型(0枪机,1半球,2快球3带云台枪机)
     */
    @Column(name = "camera_type")
    private String cameraType;

    /**
     * 摄像头像素
     */
    @Column(name = "pixel")
    private String pixel;

    /**
     * 纬度
     */
    @Column(name = "latitude")
    private String latitude;

    /**
     * 精度
     */
    @Column(name = "longitude")
    private String longitude;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;


    /**
     * 控制中心名称
     */
    @Column(name = "control_unit_name")
    private String controlUnitName;

    /**
     * 解码标签
     */
    @Column(name = "decode_tag")
    private String decodeTag;


    /**
     * 同步时间
     */
    @Column(name = "sync_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = AppProperties.DATE_TIME_PATTERN)
    private Date syncTime;

    /**
     * 展示顺序
     */
    @Column(name = "show_order")
    private Integer showOrder;



    /**
     * 所属村级名称
     */
    @Transient
    private String villageName;

    /**
     * 所属村级名称
     */
    @Transient
    private String villageCode;


    /**
     * 所属镇编码
     */
    @Transient
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long townId;


    /**
     * 所属镇编码
     */
    @Transient
    private String townCode;

    /**
     * 所属镇名称
     */
    @Transient
    private String townName;


}
