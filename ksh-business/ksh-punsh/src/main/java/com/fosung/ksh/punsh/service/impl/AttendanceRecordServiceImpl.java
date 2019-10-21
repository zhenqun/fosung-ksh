package com.fosung.ksh.punsh.service.impl;


import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.fosung.ksh.punsh.constant.Constant;
import com.fosung.ksh.punsh.dao.AttendanceRecordDao;
import com.fosung.ksh.punsh.entity.AttendanceRecord;
import com.fosung.ksh.punsh.service.AttendanceRecordService;
import com.fosung.ksh.punsh.vo.PunchRecord;
import com.fosung.ksh.punsh.vo.QueryRecordConDto;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lqyu
 * @date 2019-4-1 10:04
 */
@Service
public class AttendanceRecordServiceImpl extends AppJPABaseDataServiceImpl<AttendanceRecord, AttendanceRecordDao>
        implements AttendanceRecordService {

    @Override
    public Map<String, String> getQueryExpressions() {
        return null;
    }


    @Override
    public List<PunchRecord> queryPunchRecord(int pagenum, int pagesize, QueryRecordConDto queryRecordConDto) throws ParseException {


        //如果没有传日期获取今天的日期
        if (queryRecordConDto != null) {
            if (queryRecordConDto.getDateTime2() == null) {
                LocalDate localDate = LocalDate.now();
                queryRecordConDto.setDateTime(localDate.toString());
            } else {
                //日期转换
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf.format(queryRecordConDto.getDateTime2());
                queryRecordConDto.setDateTime(format);
            }
        }
        String dateTime = queryRecordConDto.getDateTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateTime;
        Date dateTim = formatter.parse(date);
        String ss = format.format(dateTim);
        List<PunchRecord> query = this.entityDao.queryPunchRecord(queryRecordConDto);
        String dateStr = Constant.TIME;
        long aLong = Long.parseLong(dateStr);
        if (query != null) {
            List<Long> list = new ArrayList<>();
            ArrayList<Long> arrayList = new ArrayList<>();
            for (PunchRecord attendanceRecord : query) {
                String time = attendanceRecord.getTime();
                if (time != null) {
                    String[] arr = time.split(" ");
                    list.add(Long.parseLong(arr[1]));
                }
            }

            for (Long dateLong : list) {
                if (dateLong <= aLong) {
                    arrayList.add(dateLong);
                    Long min = getMin(arrayList);
                    Long max = getMax(arrayList);
                    String valueMin = String.valueOf(min);
                    String valueMax = String.valueOf(max);
                    String minDate = ss + valueMin;
                    String maxDate = ss + valueMax;
                } else {
                    arrayList.add(dateLong);
                    Long min = getMin(arrayList);
                    Long max = getMax(arrayList);
                    String valueMin = String.valueOf(min);
                    String valueMax = String.valueOf(max);
                    String minDate = ss + valueMin;
                    String maxDate = ss + valueMax;
                }
            }
            return query;
        }
        return null;
    }

    @Override
    public List<AttendanceRecord> queryAttend(String dateStart, String dateEnd, String pin) {
        return this.entityDao.queryAttend(dateStart, dateEnd, pin);
    }

    @Override
    public List<PunchRecord> queryAttendRecord(String cityName, String status, String userName, String dateTime, String orgCode, String flag) {
        QueryRecordConDto queryRecordConDto = new QueryRecordConDto();
        queryRecordConDto.setCommunity(cityName);
        queryRecordConDto.setStatus(status);
        queryRecordConDto.setUserName(userName);
        if (dateTime == "") {
            LocalDate localDate = LocalDate.now();
            queryRecordConDto.setDateTime(localDate.toString());
        } else {
            //日期转换
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String format = sdf.format(dateTime);
            queryRecordConDto.setDateTime(format);
        }
        queryRecordConDto.setOrgCode(orgCode);
        queryRecordConDto.setFlag(flag);
        List<PunchRecord> list = this.entityDao.queryAttendRecord(queryRecordConDto);

        if (list != null) {
            int count = 1;
            for (PunchRecord punchRecord : list) {
                punchRecord.setIndex(count);
                String sta = punchRecord.getStatus();
                if (sta != null &&sta.equals("255")) {
                    punchRecord.setStatus("已签到");
                } else if (null == sta){
                    punchRecord.setStatus("未签到");
                }

                String verification = punchRecord.getVerification();
                if (verification != null && verification.equals("1")) {
                    punchRecord.setVerification("指纹");
                } else if (verification != null && verification.equals("2")) {
                    punchRecord.setVerification("人脸");
                } else if (verification != null && verification.equals("3")) {
                    punchRecord.setVerification("密码");
                } else {
                    punchRecord.setVerification("");
                }
                count ++;
            }
            return list;
        }
        return null;
    }

    /**
     * 排序取大小
     * @param
     * @return
     */
    public Long getMax(ArrayList<Long> list) {
        int max = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) > list.get(max)) {
                max = i;
            }
        }
        return list.get(max);
    }

    public Long getMin(ArrayList<Long> list) {
        int min = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) < list.get(min)) {
                min = i;
            }
        }
        return list.get(min);
    }


}
