package com.fosung.ksh.monitor.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fosung.framework.common.exception.AppException;
import com.fosung.framework.common.json.JsonMapper;
import com.fosung.framework.common.util.UtilString;
import com.google.common.collect.Maps;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 海康接口调用实体类
 */
@Slf4j
public class HikVisionArtemisUtil {
    /**
     * 请求类型
     */
    private static final String REQUES_TTYPE = "https://";
    /**
     * 能力开放平台的网站路径
     * TODO 路径不用修改，就是/artemis
     */
    private static final String ARTEMIS_PATH = "/artemis";

    /**
     * 获取实时视频流地址HLS协议
     */
    public static final String GET_CAMERA_HLS_REALTIME = ARTEMIS_PATH + "/api/mss/v1/hls/";

    /**
     * 获取实时视频流地址RTSP协议
     */
    public static final String GET_CAMERA_RTSP_REALTIME = ARTEMIS_PATH + "/api/vms/v1/rtsp/basic/";
    /**
     * 获取历史视频流地址
     */
    public static final String GET_CAMERA_HLS_HISTORY = ARTEMIS_PATH + "/api/video/v1/playback";


    /**
     * 根据appKey获取加密协议
     *
     * @return
     */
    public static String getSecurityParam() {
        String getSecurityApi = ARTEMIS_PATH + "/api/artemis/v1/agreementService/securityParam/appKey/" + ArtemisConfig.appKey;
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getSecurityApi);
            }
        };
        String result = ArtemisHttpUtil.doGetArtemis(path, null, null, null);

        return result;
    }

    /**
     * 获取监控设备信息列表
     *
     * @return
     */
    public static String callApiGetDeviceList() {
        String getSecurityApi = ARTEMIS_PATH + "/api/common/v1/remoteCameraInfoRestService/findCameraInfoPage";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getSecurityApi);
            }
        };
        Map<String, String> querys = new HashMap<String, String>(2) {
            {
                put("start", "0");
                put("size", "100000");
            }
        };
        String result = ArtemisHttpUtil.doGetArtemis(path, querys, null, null);
        return result;
    }

    /**
     * 根据父组织编号查询子组织
     *
     * @param unitCode
     * @return
     */
    public static String callApiGetOrgnizations(String unitCode) {
        String getSecurityApi = ARTEMIS_PATH + "/api/common/v1/remoteControlUnitRestService/findControlUnitByUnitCode";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getSecurityApi);
            }
        };
        Map<String, String> querys = new HashMap<String, String>(1) {
            {
                put("unitCode", unitCode);
            }
        };
        String result = ArtemisHttpUtil.doGetArtemis(path, querys, null, null);
        return result;
    }

    /**
     * 根据组织编号  获取监控点信息
     *
     * @return
     */
    public static String callApiGetMonitoryPoints(String treeNode) {
        String getSecurityApi = ARTEMIS_PATH + "/api/common/v1/remoteControlUnitRestService/findCameraInfoPageByTreeNode";
        Map<String, String> path = new HashMap<String, String>(1) {
            {
                put("https://", getSecurityApi);
            }
        };
        Map<String, String> querys = new HashMap<String, String>(1) {
            {
                put("treeNode", treeNode);
                put("start", "0");
                put("size", "1000");
            }
        };
        String result = ArtemisHttpUtil.doGetArtemis(path, querys, null, null);
        return result;
    }

    /**
     * 新增名单库
     *
     * @param listLibName
     * @param typeId
     * @param describe
     * @return
     */
    public static String callApiPostCreatListLb(String listLibName, int typeId, String describe) {
        String listLibDataApi = ARTEMIS_PATH + "/api/fms/v2/listLib/addListLib";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", listLibDataApi);
            }
        };
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("listLibName", listLibName);
        jsonBody.put("typeId", typeId); // 2 黑名单  3  静态库
        jsonBody.put("describe", describe);
        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json");
        return result;
    }

    /**
     * 修改名单库
     *
     * @param listLibName
     * @param typeId
     * @param describe
     * @return
     */
    public static String callApiPostModifyListLb(String listLibId, String listLibName, String describe) {
        String listLibDataApi = ARTEMIS_PATH + "/api/fms/v2/listLib/modifyListLib";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", listLibDataApi);
            }
        };
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("listLibId", listLibId);
        jsonBody.put("listLibName", listLibName);
        jsonBody.put("describe", describe);
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json");
        return result;
    }


    /**
     * 名单库删除
     *
     * @param listLibId
     * @return
     */
    public static String callApiPostDeleteListLb(String listLibId) {
        String listLibDataApi = ARTEMIS_PATH + "/api/fms/v2/listLib/deleteListLib";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", listLibDataApi);
            }
        };
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("listLibId", listLibId);
        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json");
        return result;
    }

    /**
     * 新增黑名单
     *
     * @param humanName
     * @param picBase64
     * @param listLibId
     * @return
     */
    public static String callApiPostAddRecord(String humanName, String picBase64, String listLibId, String credentialsNum) {
        String listLibDataApi = ARTEMIS_PATH + "/api/fms/v2/blacklist/addRecord";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", listLibDataApi);
            }
        };
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("humanName", humanName);
        jsonBody.put("picBase64", picBase64);
        jsonBody.put("listLibId", listLibId);
        jsonBody.put("credentialsNum", credentialsNum);// 证件号
        jsonBody.put("credentialsType", 0); //证件类型
        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json");
        return result;
    }

    /**
     * 修改黑名单
     *
     * @param humanId
     * @param picBase64
     * @param listLibId
     * @return
     */
    public static String callApiPostEditRecord(String humanId, String picBase64, String listLibId) {
        String listLibDataApi = ARTEMIS_PATH + "/api/fms/v2/blacklist/modifyRecord";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", listLibDataApi);
            }
        };
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("humanId", humanId);
        jsonBody.put("picBase64", picBase64);
        jsonBody.put("listLibId", listLibId);
        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json");
        return result;
    }

    /**
     * 查询黑名单
     *
     * @param credentialsNum 证件号
     * @param listLibIds     名单库
     * @return
     */
    public static String callApiGetRecords(String credentialsNum, String listLibIds) {
        String getSecurityApi = ARTEMIS_PATH + "/api/fms/v2/blacklist/findRecord";
        Map<String, String> path = new HashMap<String, String>(1) {
            {
                put("https://", getSecurityApi);
            }
        };
        Map<String, String> querys = new HashMap<String, String>(1) {
            {
                if (UtilString.isNotBlank(credentialsNum)) {
                    put("credentialsNum", credentialsNum);
                }
                put("pageNo", "1");
                put("pageSize", "100000");
                put("listLibIds", listLibIds);
            }
        };
        String result = ArtemisHttpUtil.doGetArtemis(path, querys, null, null);
        return result;
    }

    /**
     * 删除黑名单
     *
     * @param
     * @param
     * @return
     */
    public static String callApiDeleteRecords(String humanId) {
        String getSecurityApi = ARTEMIS_PATH + "/api/fms/v2/blacklist/deleteRecord";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getSecurityApi);
            }
        };
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("humanId", humanId);
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json");

        return result;
    }


    /**
     * 查询黑名单预警信息
     *
     * @param libIds         名单库
     * @param startAlarmTime 开始时间  格式  	2011-07-01 01:01:01
     * @param endAlarmTime   结束时间  格式    	2011-07-01 01:01:01
     * @return
     */
    public static String callApiPostBlackList(String libIds, String startAlarmTime, String endAlarmTime, String indexCode) {
        String listLibDataApi = ARTEMIS_PATH + "/api/aiapplication/v1/face/alarm/queryAlarmInfoByCondition";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", listLibDataApi);
            }
        };
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", 1);
        jsonBody.put("pageSize", 100000);
        jsonBody.put("startAlarmTime", startAlarmTime);
        jsonBody.put("endAlarmTime", endAlarmTime);
        jsonBody.put("libIds", libIds);
        jsonBody.put("cameraIndexCodes", indexCode);
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json");
        return result;
    }

    /**
     * 根据组织编号 获取设备信息
     *
     * @param indexCode
     * @return
     */
    public static String callApiPostCamera(String indexCode) {
        String getSecurityApi = ARTEMIS_PATH + "/api/fms/v2/resource/findCamera";
        Map<String, String> path = new HashMap<String, String>(1) {
            {
                put("https://", getSecurityApi);
            }
        };
        Map<String, String> querys = new HashMap<String, String>(1) {
            {
                put("credentialsNum", indexCode);
                put("pageNo", "1");
                put("pageSize", "5000");
            }
        };
        String result = ArtemisHttpUtil.doGetArtemis(path, querys, null, null);
        return result;
    }

    /**
     * 实时视频 根据监控点编号获取HLS（RTSP）地址(向下兼容用)
     */

    public static String getCameraVideoRealTime(String indexCode, String videoType) {

        Assert.isTrue(UtilString.isNotEmpty(indexCode), "监控点编号不能为空！");

        HashMap<String, String> path = Maps.newHashMap();
        if (UtilString.equalsIgnoreCase(videoType, "HLS")) {
            path.put(REQUES_TTYPE, GET_CAMERA_HLS_REALTIME + indexCode);
        } else if (UtilString.equalsIgnoreCase(videoType, "RTSP")) {
            path.put(REQUES_TTYPE, GET_CAMERA_RTSP_REALTIME + indexCode);
        } else {
            throw new AppException("获取视频协议参数不正确" + videoType);
        }

        HashMap<String, String> querys = Maps.newHashMap();

        String controlUnitInfoPageStr = ArtemisHttpUtil.doGetArtemis(path, querys, null, null);

        return controlUnitInfoPageStr;

    }


    /**
     * 根据监控点编号和时间获取视频回放url和视频回放时间片段
     *
     * @param cameraIndexCode
     * @param beginTime
     * @param endTime
     * @return
     */
    public static String getCameraVideoHistory(String cameraIndexCode, String beginTime, String endTime) {
        Assert.isTrue(UtilString.isNotEmpty(cameraIndexCode), "监控点编号不能为空！");

        HashMap<String, String> path = Maps.newHashMap();
        path.put(REQUES_TTYPE, GET_CAMERA_HLS_HISTORY);

        HashMap<String, String> querys = Maps.newHashMap();
        querys.put("beginTime", beginTime);
        querys.put("endTime", endTime);
        querys.put("cameraIndexCode", cameraIndexCode);

        log.debug("获取历史视频流请求参数:\npath={},\nquery={}",
                JsonMapper.toJSONString(path,true),
                JsonMapper.toJSONString(querys,true));

        String controlUnitInfoPageStr = ArtemisHttpUtil.doGetArtemis(path, querys, null, null);
        return controlUnitInfoPageStr;

    }
    /**
     * 根据组织编号  获取监控点信息
     *
     * @return
     */
    public static String callApiAlarmTopicInfo(String appkey) {
        String getSecurityApi = ARTEMIS_PATH + "/api/mss/v2/fms/getAlarmTopic";
        Map<String, String> path = new HashMap<String, String>(1) {
            {
                put("https://", getSecurityApi);
            }
        };
        Map<String, String> querys = new HashMap<String, String>(1) {
            {
                put("appKey", appkey);
            }
        };
        String result = ArtemisHttpUtil.doGetArtemis(path, querys, null, null);
        return result;
    }

}
