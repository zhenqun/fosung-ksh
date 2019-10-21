package com.fosung.ksh.duty.dto;

import com.fosung.framework.common.util.UtilBean;
import com.fosung.framework.common.util.UtilNumber;
import com.fosung.ksh.duty.entity.DutyVillageRecord;
import com.fosung.ksh.monitor.entity.MonitorCamera;
import com.fosung.ksh.sys.dto.SysArea;
import com.google.common.collect.Lists;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 城镇查询dto
 *
 * @author wangyihua
 * @date 2019-05-13 14:44
 */
@Data
public class DutyArea extends SysArea {

    public DutyArea() {

    }


    public DutyArea(SysArea sysArea) {
        UtilBean.copyProperties(sysArea, this);
    }

    /**
     * 排名
     */
    private Integer ranking = 0;


    /**
     * 已签到数量
     */
    private Integer signNumber = 0;


    /**
     * 未签到数量
     */
    private Integer notSignNumber = 0;


    /**
     * 签到百分率，保留两位小数
     */
    private String signRate;


    /**
     * 未签到百分率，保留两位小数
     */
    private String notSignRate;

    /**
     * 是否签到
     */
    private Boolean isSign = false;


    /**
     * 设备在线数量
     */
    private Integer onlineNumber = 0;

    /**
     * 设备离线数量
     */
    private Integer offlineNumber = 0;


    /**
     * 设备在线百分率，保留两位小数
     */
    private String onlineRate;

    /**
     * 设备离线百分率，保留两位小数
     */
    private String offlineRate;

    /**
     * 设备是否在线
     */
    private Boolean isOnline = false;


    /**
     * 应该签到的天数
     */
    private Integer shouldSignDayNum;

    /**
     * 已经签到的天数
     */
    private Integer isSingDayNum;
    /**
     * 下级数量
     */
    private Integer leaveNum;

    /**
     * 村级挂载的设备列表
     */
    private List<MonitorCamera> cameraList = Lists.newArrayList();

    /**
     * 村级值班记录列表
     */
    private List<DutyVillageRecord> villageRecordList = Lists.newArrayList();

    /**
     * 值班人员数量
     */
    private Integer dutySchedulePeopeleNumber;

    public String getSignRate() {
        if (signNumber == null || signNumber == 0) {
            return "0.00";
        }
        Double signRate = getAllSignNumber() == 0 ? 0 : signNumber * 100.00 / getAllSignNumber();
        return format(signRate);
    }

    public String getNotSignRate() {
        return format(100 - UtilNumber.createDouble(getSignRate()));
    }


    public String getOnlineRate() {
        if (onlineNumber == null || onlineNumber == 0) {
            return "0.00";
        }
        Double onlineRate = getAllCameraNumber() == 0 ? 0.0 : onlineNumber * 100.00 / getAllCameraNumber();
        return format(onlineRate);
    }


    public String getOfflineRate() {
        return format(100 - UtilNumber.createDouble(getOnlineRate()));
    }


    public Integer getAllSignNumber() {
        return signNumber + notSignNumber;
    }


    public Integer getAllCameraNumber() {
        return onlineNumber + offlineNumber;
    }


    /**
     * 格式化数据，保留两位小数
     *
     * @param value
     * @return
     */
    private String format(Double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);
    }

    public Boolean getIsSign() {
        return getSignNumber() != null && getSignNumber() != 0;
    }

    public Boolean getIsOnline() {

        return getOnlineNumber() != null && getOnlineNumber() != 0;
    }

}
