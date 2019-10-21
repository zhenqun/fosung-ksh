package com.fosung.ksh.common.util;

import com.fosung.framework.common.util.UtilDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtilDateTime extends UtilDate {


    /**
     * 获取当前时间是本月第几天
     *
     * @param date
     * @return
     */
    public static int getMonthDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前时间是本月第几天
     *
     * @param date
     * @return
     */
    public static int getMonthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    /**
     * 获取当前时间季度的第一天
     *
     * @return
     */
    public static Date getQuarterStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int currentMonth = c.get(Calendar.MONTH) + 1;
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 6);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前季度的结束时间，即2012-03-31 23:59:59
     *
     * @return
     */
    public static Date getQuarterEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getQuarterStartTime(date));
        cal.add(Calendar.MONTH, 3);
        cal.add(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    /**
     * 获取年度第一天
     *
     * @param date
     * @return
     */
    public static Date getYearStartTime(Date date) {
        String year = UtilDateTime.getDateFormatStr(date, "yyyy");
        Date d = UtilDateTime.parse(year + "/01/01");
        return d;
    }

    /**
     * 获取年度最后一天
     *
     * @param date
     * @return
     */
    public static Date getYearEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getYearStartTime(date));
        cal.add(Calendar.YEAR, 1);
        cal.add(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }


    // 获得本月第一天0点时间
    public static Date getMonthStartTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    // 获得本月最后一天24点时间
    public static Date getMonthEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.add(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }


    /**
     * 获取当前日期为今年第几季度
     *
     * @param date
     * @return
     */
    public static Integer getQuarterNum(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int currentMonth = cal.get(Calendar.MONTH);
        return currentMonth / 3 + 1;
    }

    /**
     * 获取明天的凌晨6点
     *
     * @return 明天
     */
    public static Date getTomorrowSix() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1);
        String dateStr = UtilDate.getDateFormatStr(cal.getTime(), "yyyy/MM/dd");
        dateStr += " 06:00:00";
        Date date = UtilDate.parse(dateStr);
        return date;
    }

    /**
     * D当前时间距离明天6点还有多久，用于缓存超时
     *
     * @return
     */
    public static long minuteTomorrowSix() {
        long nowTime = System.currentTimeMillis();
        return (getTomorrowSix().getTime() - nowTime) / 1000;
    }


    /**
     * 获取周一
     *
     * @param date
     * @return
     */
    public static Date getMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    public static Date getSunday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getMonday(date));
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, 6);
        return cal.getTime();
    }



    public static int getWeekDays(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        return dayWeek;
    }


    /**
     * 日期相减，获取天数
     *
     * @param start
     * @param end
     * @return
     */
    public static int minusDays(Date start, Date end) {
        //毫秒ms
        long diff = end.getTime() - start.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return (int) diffDays;
    }


    /**
     * 获取N天后的日期
     * @param date
     * @param i
     * @return
     */
    public static Date plusDays(Date date, int i ) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
       cal.add(Calendar.DAY_OF_MONTH,i);
        return cal.getTime();
    }






    public static void main(String[] args) {
        Date date = UtilDateTime.parse("2019" + "/" + "05" + "/26");
        Date startTime = UtilDateTime.getMonthStartTime(date);
        Date endTime = UtilDateTime.getMonthEndTime(date);
        System.out.println(startTime);
        System.out.println(endTime);
        System.out.println(getMonthDay(new Date()));
        System.out.println(getMonthLastDay(new Date()));
        System.out.println("s" + getMonthDay(date));
    }
}
