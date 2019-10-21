package com.fosung.ksh.punsh.controller;

import com.fosung.framework.common.util.UtilCollection;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.web.http.ResponseParam;
import com.fosung.ksh.punsh.constant.Constant;
import com.fosung.ksh.punsh.entity.AttendanceRecord;
import com.fosung.ksh.punsh.entity.ClientInfo;
import com.fosung.ksh.punsh.entity.UserInfo;
import com.fosung.ksh.punsh.service.AdministrationService;
import com.fosung.ksh.punsh.service.AttendanceRecordService;
import com.fosung.ksh.punsh.service.PushchService;
import com.fosung.ksh.punsh.service.UserInfoServcie;
import com.fosung.ksh.punsh.support.PunchConfigProperties;
import com.fosung.ksh.punsh.support.PunchreProperties;
import com.fosung.ksh.punsh.util.UtilPunshEasyPoi;
import com.fosung.ksh.punsh.vo.Administration;
import com.fosung.ksh.punsh.vo.PunchRecord;
import com.fosung.ksh.punsh.vo.QueryRecordConDto;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @date 2019-3-28 10:48
 */
@Slf4j
@RestController
@RequestMapping(PunchController.BASE_URL)
public class PunchController extends AppIBaseController {

    public static final String BASE_URL = "/iclock";

    @Autowired
    private PunchConfigProperties punchConfig;
    @Autowired
    private PushchService pushchService;
    @Autowired
    private AttendanceRecordService attendanceRecordService;
    @Autowired
    private UserInfoServcie userInfoServcie;

    @Autowired
    private AdministrationService administrationService;


    /**
     * 初始化配置信息
     *
     * @param csds
     * @param sn       序列号
     * @param language 语言
     * @param options  获取服务器配置参数,目前只有all
     * @param pushver  push协议的版本
     * @return
     */
    @RequestMapping(value = "/cdata", method = RequestMethod.GET)
    public String clientInfo(@RequestParam(value = "CSDS", required = false) String csds,
                             @RequestParam(value = "SN") String sn,
                             @RequestParam(value = "language") String language,
                             @RequestParam(value = "options") String options,
                             @RequestParam(value = "pushver") String pushver) {
        HttpServletRequest request = getHttpServletRequest();

        //根据序列号进行查询数据库是否初始化了本机器
        HashMap<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("sn", sn);
        List<ClientInfo> clientInfos = pushchService.queryAll(searchMap);
        //保存
        if (UtilCollection.sizeIsEmpty(clientInfos)) {
            ClientInfo clientInfo = new ClientInfo();
            clientInfo.setCsds(csds);
            clientInfo.setLanguage(language);
            clientInfo.setOptions(options);
            clientInfo.setPushver(pushver);
            clientInfo.setSn(sn);
            clientInfo.setPort(request.getRemotePort());
            clientInfo.setHost(request.getRemoteHost());
            ClientInfo saveClientInfo = pushchService.save(clientInfo);
            log.info("-------------初始化配置信息:{}", clientInfo);
        } else {
            //更新
            Set<String> set = getParametersStartingWith(getHttpServletRequest(), "").keySet();
            pushchService.update(clientInfos.get(0), set);
            log.info("-------------更新配置信息");
        }

        String config = "GET OPTION FROM:" + sn + "\nATTLOGStamp=" + punchConfig.getAttlogStamp() + "\nOPERLOGStamp=" + punchConfig.getOperlogStamp() + "\nATTPHOTOStamp="
                + punchConfig.getAttphotoStamp() + "\nErrorDelay=" + punchConfig.getErrorDelay() + "\nDelay=" + punchConfig.getDelay() + "\nTransTimes=" + punchConfig.getTransTimes() + "\nTransInterval="
                + punchConfig.getTransInterval() + "\nTransFlag=" + punchConfig.getTransFlag() + "\nTimeZone=" + punchConfig.getTimeZone() + "\nRealtime=" + punchConfig.getRealtime() + "\nEncrypt=" + punchConfig.getEncrypt() + "";

        log.info("----------------返回配置信息:{}", config);
        return config;
    }

    /**
     * 主动向服务器读取命令
     * 此方法没有进行保存
     *
     * @param sn
     * @return
     */
    @RequestMapping("/getrequest")
    public String getrequest(@RequestParam(value = "SN") String sn) {

        return "C:${123456}:CHECK";
//        return "ok";
    }

    /**
     * @return
     */
    @PostMapping("/devicecmd")
    public String devicecmd(@RequestParam(value = "SN") String sn,
                            HttpServletRequest request) {
        String CmdRecord = readAsChars(request);
        return CmdRecord;
    }


    /**
     * 打卡记录
     *
     * @param sn    序列号
     * @param table 表示为考勤记录
     * @param stamp 最新的时间戳
     * @return
     */
    @PostMapping("/cdata")
    public String addPunchRecord(@RequestParam(value = "SN") String sn,
                                 @RequestParam(value = "table") String table,
                                 @RequestParam(value = "Stamp", required = false) String stamp,
                                 HttpServletRequest request) throws ParseException {

        String dataRecord = readAsChars(request);
        if (StringUtils.isNotBlank(table)) {
            //保存打卡日志
            if (Constant.ATTLOG.equals(table)) {
                String[] str = splitPunchStr(dataRecord);
                if (str == null || str.length <= 0) {
                    log.error("参数错误");
                    return "No";
                }
                //保存打卡记录:
                AttendanceRecord attendanceRecord = new AttendanceRecord();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateStr = str[1];
                Date date = formatter.parse(dateStr);
                String ss = format.format(date);

                String dateStart = ss + " " + "00:00:00";
                String dateEnd = ss + " " + "23:59:59";
                String pin = str[0];
                List<AttendanceRecord> li = attendanceRecordService.queryAttend(dateStart, dateEnd, pin);
                if (UtilCollection.sizeIsEmpty(li)) {
                    attendanceRecord.setPin(str[0]);
                    attendanceRecord.setSn(sn);
                    attendanceRecord.setReserved(str[5]);
                    attendanceRecord.setStatus(str[2]);
                    //人脸
                    if (Constant.HUMAN_FACE.equals(str[3])) {
                        attendanceRecord.setVerification(2);
                        //指纹
                    } else if (Constant.FINGERPRINT.equals(str[3])) {
                        attendanceRecord.setVerification(1);
                        //密码
                    } else if (Constant.PASSWORD.equals(str[3])) {
                        attendanceRecord.setVerification(3);
                    }
                    attendanceRecord.setVerify(str[3]);
                    attendanceRecord.setWorkCode(str[4]);
                    attendanceRecord.setTime(formatter.parse(str[1]));
                    AttendanceRecord save = attendanceRecordService.save(attendanceRecord);
                    if (save != null && save.getId() > 0) {
                        log.info("打卡记录保存成功:{}", save);
                        return "OK:1";
                    }
                    log.error("打卡记录保存失败:{}", save);
                    return "NO";
                } else {
                    return "OK";
                }
            } else if (Constant.OPERLOG.equals(table)) {

                Map<String, String> map = splitUserStr(dataRecord);
                if (map == null || map.size() <= 0) {
                    log.error("参数错误");
                    return "NO";
                }
                String[] str = splitPunchStr(dataRecord);
                List<List> lists = returnLists(str);

                for (List<String> ls : lists) {
                    UserInfo user = userInfoServcie.findUserInfoByPin(ls.get(0), sn);
                    if (user == null) {
                        UserInfo us = new UserInfo();
                        // 添加用户
                        us.setName(ls.get(1));
                        us.setSn(sn);
                        us.setPin(ls.get(0));
                        us.setLastUpdateDatetime(new Date());
                        us.setPri(ls.get(2));
                        us.setPassword(ls.get(3));
                        us.setCard(ls.get(4));
                        us.setGrp(ls.get(5));
                        us.setTz(ls.get(6));
                        //保存用户信息
                        UserInfo save = userInfoServcie.save(us);
                    } else {
                        // 更新用户信息
//                        user.setSn(sn);
                        user.setName(ls.get(1));
                        user.setPri(ls.get(2));
                        user.setPassword(ls.get(3));
                        user.setCard(ls.get(4));
                        user.setGrp(ls.get(5));
                        user.setTz(ls.get(6));
                        user.setLastUpdateDatetime(new Date());
                        userInfoServcie.updateUser(user, sn);
                    }
                }
                log.info("更新用户信息成功:{}");
                return "OK:1";
            }
        }
        log.error("非法参数:{}", table);
        return "NO";
    }


    /**
     * 查询组织树结构
     */
    @PostMapping("/query/org")
    public ResponseParam queryOrg(@RequestParam("cityCode") String cityCode) {

        Administration administration = administrationService.queryAdminList(cityCode);


        return ResponseParam.success().data(administration);

    }

    /**
     * 查询打卡记录
     */
    @PostMapping("/query/punchRecord")
    public ResponseParam queryPunchRecord(@RequestParam(required = false, value = "pagenum", defaultValue = "" + DEFAULT_PAGE_START_NUM) int pageNum,
                                          @RequestParam(required = false, value = "pagesize", defaultValue = "" + DEFAULT_PAGE_SIZE_NUM) int pageSize,
                                          @ModelAttribute QueryRecordConDto queryRecordConDto) throws ParseException {

        List<PunchRecord> attendanceRecordsList = attendanceRecordService.queryPunchRecord(pageNum, pageSize, queryRecordConDto);

        return ResponseParam.success().datalist(attendanceRecordsList);
    }

    /**
     * 导出考勤记录
     */
    @PostMapping(value = "export/exportTypeAll")
    public void expotByCondition() {
        Map<String, Object> searchParam = getParametersStartingWith(getHttpServletRequest(), DEFAULT_SEARCH_PREFIX);
        String cityName = searchParam.get("community").toString();
        String status = searchParam.get("status").toString();
        String userName = searchParam.get("userName").toString();
        String dateTime = searchParam.get("dateTime2").toString();
        String orgCode = searchParam.get("orgCode").toString();
        String flag = searchParam.get("flag").toString();

        List<PunchRecord> list = attendanceRecordService.queryAttendRecord(cityName, status, userName, dateTime, orgCode, flag);
        PunchreProperties.ExportConfig exportConfig = new PunchreProperties().getExportConfig();
        UtilPunshEasyPoi.exportExcel(list, exportConfig.getTitle(), exportConfig.getSheetName(), PunchRecord.class, exportConfig.getFileName(), getHttpServletResponse());
    }


    /**
     * 查询机器列表
     */
    @GetMapping("/query/punchList")
    public ResponseParam queryPunchList() {
        Map map = new HashMap();
        List<ClientInfo> list = pushchService.queryAll(map, null);
        Collections.reverse(list);
        return ResponseParam.success().datalist(list);
    }


    /**
     * 读取request
     *
     * @param request
     * @return
     */
    public static String readAsChars(HttpServletRequest request) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 截取打卡字符串
     */
    private String[] splitPunchStr(String str) {

        if (StringUtils.isBlank(str)) {
            log.error("参数错误:{}", str);
            return new String[0];
        }
        str = str.replaceAll("\t", ",");
        str = str.replaceAll(",,", ",");
        String[] split = str.split(",");
        return split;
    }

    private List<List> returnLists(String[] str) {
        List<List> lists = new ArrayList<>();
        List<String> listStr = Arrays.asList(str);
        List<String> strings = new ArrayList<>();

        for (String l : listStr) {
            String[] split = l.split("=");
            String s = "";
            if (split.length == 2) {
                s = split[1];
            } else if (split.length == 3) {
                s = split[2];
            }
            strings.add(s);
        }

        int count = 0;
        int quantity = 8;

        while (strings.size() > count) {
            if (strings.size() > (count + quantity)) {
                lists.add(strings.subList(count, count + quantity));
                count += quantity;
            } else {
                break;
            }
        }
        return lists;
    }

    private List<List> returnListsRecord(String[] str) {
        List<List> lists = new ArrayList<>();
        List<String> listStr = Arrays.asList(str);

        int count = 0;
        int quantity = 7;
        while (listStr.size() > count) {
            if (listStr.size() > (count + quantity)) {
                lists.add(listStr.subList(count, count + quantity));
                count += quantity;
            } else {
                break;
            }
        }
        return lists;
    }

    /**
     * 截取保存用户的字符串
     */
    private Map<String, String> splitUserStr(String str) {
        Map<String, String> map = new HashMap<>();

        if (StringUtils.isBlank(str)) {
            log.error("参数错误:{}", str);
            return map;
        }
        String[] split = str.split("\t");

        for (int i = 0; i < split.length; i++) {
            String[] split1 = split[i].split("=");
            map.put(split1[0].equals(Constant.USER_PIN) ? Constant.PIN : split1[0], split1.length > 1 ? split1[1] : "");
        }
        return map;
    }

    public static String gbToUtf8(String str) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            String s = str.substring(i, i + 1);
            if (s.charAt(0) > 0x80) {
                byte[] bytes = s.getBytes("Unicode");
                String binaryStr = "";
                for (int j = 2; j < bytes.length; j += 2) {
                    // the first byte
                    String hexStr = getHexString(bytes[j + 1]);
                    String binStr = getBinaryString(Integer.valueOf(hexStr, 16));
                    binaryStr += binStr;
                    // the second byte
                    hexStr = getHexString(bytes[j]);
                    binStr = getBinaryString(Integer.valueOf(hexStr, 16));
                    binaryStr += binStr;
                }
                // convert unicode to utf-8
                String s1 = "1110" + binaryStr.substring(0, 4);
                String s2 = "10" + binaryStr.substring(4, 10);
                String s3 = "10" + binaryStr.substring(10, 16);
                byte[] bs = new byte[3];
                bs[0] = Integer.valueOf(s1, 2).byteValue();
                bs[1] = Integer.valueOf(s2, 2).byteValue();
                bs[2] = Integer.valueOf(s3, 2).byteValue();
                String ss = new String(bs, "UTF-8");
                sb.append(ss);
            } else {
                sb.append(s);
            }
        }
        return sb.toString();
    }

    private static String getHexString(byte b) {
        String hexStr = Integer.toHexString(b);
        int m = hexStr.length();
        if (m < 2) {
            hexStr = "0" + hexStr;
        } else {
            hexStr = hexStr.substring(m - 2);
        }
        return hexStr;
    }

    private static String getBinaryString(int i) {
        String binaryStr = Integer.toBinaryString(i);
        int length = binaryStr.length();
        for (int l = 0; l < 8 - length; l++) {
            binaryStr = "0" + binaryStr;
        }
        return binaryStr;
    }

}
