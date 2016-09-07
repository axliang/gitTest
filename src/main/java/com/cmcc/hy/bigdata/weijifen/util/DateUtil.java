package com.cmcc.hy.bigdata.weijifen.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间工具类
 * 
 * @author Lucifer
 *
 */
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
    /**
     * 时间格式: yyyyMMddHHmmss
     */
    public static final String YMDHMS_PATTERN = "yyyyMMddHHmmss";

    /**
     * 时间格式: yyyyMMdd
     */
    public static final String YMD_PATTERN = "yyyyMMdd";

    /**
     * 时间格式: yyyyMM
     */
    public static final String YM_PATTERN = "yyyyMM";

    /**
     * 时间格式: yyyy-MM-dd
     */
    public static final String YMD_HYPHEN_PATTERN = "yyyy-MM-dd";

    /**
     * 时间格式: yyyy/MM/dd
     */
    public static final String YMD_SLASH_PATTERN = "yyyy/MM/dd";

    /**
     * 时间格式: yyyy-MM-dd HH:mm:ss
     */
    public static final String YMD_HYPHEN_HMS_COLON_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式: yyyy/MM/dd HH:mm:ss
     */
    public static final String YMD_SLASH_HMS_COLON_PATTERN = "yyyy/MM/dd HH:mm:ss";

    /**
     * 时间格式: yyyy-MM-dd-hh.mm.ss.SSSSSS
     */
    public static final String YMD_HYPHEN_HMSS_DOT_PATTEN = "yyyy-MM-dd-hh.mm.ss";

    /**
     * 按指定模式解析时间字符串
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static Date getDateFromStr(String date, String pattern) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 按指定模式格式化时间
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDateToStr(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 根据指定的日期或系统日期，获取计算所需的起始时间和结束时间
     * 
     * @param dateAnalysis
     *            (指定的需要计算的截止日期，eg：20150525，如无则按系统时间的前一天计算) 计算周期长度
     * @return startDate 和 endDate 字符串数组
     */
    public static String[] calculateStartDayAndEndDay(String dateAnalysis, int duration) {
        SimpleDateFormat formatter = new SimpleDateFormat(YMD_PATTERN);
        String startDate = "";
        String endDate = "";

        Calendar calendar = Calendar.getInstance();

        if (dateAnalysis != null && !"null".equals(dateAnalysis)) {
            try {
                calendar.setTime(formatter.parse(dateAnalysis));
                calendar.add(Calendar.DATE, 1);
            } catch (ParseException e) {
                logger.error("The dateAnalysis format is wrong!");
                System.exit(0);
            }
        }
        calendar.add(Calendar.DATE, -1);
        endDate = formatter.format(calendar.getTime());
        calendar.add(Calendar.DATE, -1 * duration);
        startDate = formatter.format(calendar.getTime());

        String[] date = new String[2];
        date[0] = endDate;
        date[1] = startDate;
        return date;
    }

    /**
     * 计算在统计日期范围内双休日的天数和工作日的天数
     * 
     * @param start
     *            开始时间(yyyyMM)
     * @param end
     *            结束时间(yyyyMM)
     * @return 返回格式： 双休日天数;工作日天数
     */
    public static String calculateWeekendAndWeekday(String start, String end) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendarStart = Calendar.getInstance();
            Calendar calendarEnd = Calendar.getInstance();
            calendarStart.setTime(sdf.parse(start));
            calendarEnd.setTime(sdf.parse(end));

            int numOfWeekend = 0;
            int numOfWeekday = 0;

            while (calendarStart.compareTo(calendarEnd) <= 0) {
                if (isWeekend(calendarStart.getTime())) {
                    numOfWeekend++;
                } else {
                    numOfWeekday++;
                }
                calendarStart.add(Calendar.DATE, 1);
            }
            return numOfWeekend + ";" + numOfWeekday;
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 判断某个日期是否为双休日
     * 
     * @param date
     * @author jinyibin
     * @date 2015年9月10日
     * @return
     */
    public static boolean isWeekend(Date date) {
        if (date == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY));
    }

    /**
     * 枚举起始日期和结束日期之间的的日期，如[20151010，20151013]，则返回20151010|20151011|20151012|
     * 20151013
     * 
     * @description
     * @author hechan
     * @date 2015年10月27日
     * @param start
     *            开始时间(yyyyMM),输入为""或null时,默认为系统时间的前一天
     * @param end
     *            结束时间(yyyyMM),输入为""或null时,默认为系统时间的前一天
     * @return
     */
    public static String durationToString(String start, String end) {
        SimpleDateFormat formatter = new SimpleDateFormat(YMD_PATTERN);
        Calendar calendarEnd = Calendar.getInstance();
        Calendar calendarStart = Calendar.getInstance();

        if ("".equals(start) || null == start) {
            calendarStart.add(Calendar.DATE, -1);
            try {
                String date = formatter.format(calendarStart.getTime());
                calendarStart.setTime(formatter.parse(date));
            } catch (ParseException e) {
                logger.error("The now date format is wrong!");
                return "";
            }
        } else {
            try {
                calendarStart.setTime(formatter.parse(start));
            } catch (ParseException e) {
                logger.error("The start date format is wrong!");
                return "";
            }
        }

        if ("".equals(end) || null == end) {
            calendarEnd.add(Calendar.DATE, -1);
            try {
                String date = formatter.format(calendarEnd.getTime());
                calendarEnd.setTime(formatter.parse(date));
            } catch (ParseException e) {
                logger.error("The now date format is wrong!");
                return "";
            }
        } else {
            try {
                calendarEnd.setTime(formatter.parse(end));
            } catch (ParseException e) {
                logger.error("The end date format is wrong!");
                return "";
            }
        }

        StringBuilder strBuilder = new StringBuilder();
        while (calendarEnd.compareTo(calendarStart) >= 0) {
            if (strBuilder.length() > 0) {
                strBuilder.append("|");
            }
            strBuilder.append(formatter.format(calendarStart.getTime()));
            calendarStart.add(Calendar.DATE, 1);
        }
        return strBuilder.toString();
    }

    /**
     * 根据出生日期计算年龄
     * 
     * @param birthday
     * @param nowDate
     * @return
     */
    public static int ageCalculation(String birthday, String nowDate) {
        // 如果输入为空或者不匹配yyyyMMdd格式，则返回-1
        if (birthday == null || !Pattern.matches("[\\d]{8}", birthday)) {
            return -1;
        }
        if (nowDate == null || "".equals(nowDate)) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(YMD_PATTERN);
            nowDate = sdf.format(calendar.getTime());
        } else {
            if (!Pattern.matches("[\\d]{8}", nowDate)) {
                return -1;
            }
        }
        int birthYear = Integer.parseInt(birthday.substring(0, 4));
        int nowYear = Integer.parseInt(nowDate.substring(0, 4));
        int age = nowYear - birthYear;
        if (nowDate.substring(4, 8).compareTo(birthday.substring(4, 8)) < 0) {
            age--;
        }
        if (age < 0) {
            return -1;
        }
        return age;
    }

    private static final Map<String, String> YMD_FORMAT = new HashMap<String, String>();

    static {
        YMD_FORMAT.put("^\\d{8}$", "yyyyMMdd");
        YMD_FORMAT.put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
        YMD_FORMAT.put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
    }

    /**
     * 将原有时间字符串转换成指定格式的时间字符串
     * 
     * @param inputDate
     *            原始时间字符串
     * @param outFormat
     *            指定输出时间格式
     * @return 转换后的时间字符串
     */
    public static String convertYMDFormat(String inputDate, String outFormat) {
        if (inputDate == null || inputDate.isEmpty()) {
            return null;
        }
        String value = null;
        try {
            SimpleDateFormat resFmt = new SimpleDateFormat(outFormat);
            for (Entry<String, String> entry : YMD_FORMAT.entrySet()) {
                Pattern ptn = Pattern.compile(entry.getKey());
                Matcher m = ptn.matcher(inputDate.trim());
                if (m.find()) {
                    String matchFmt = entry.getValue();
                    if (matchFmt.equals(outFormat)) {
                        return inputDate;
                    }
                    SimpleDateFormat tmpSdf = new SimpleDateFormat(matchFmt);
                    value = resFmt.format(tmpSdf.parse(inputDate));
                }
            }
        } catch (Exception e) {
            logger.error("convert format fail!");
        }
        return value;
    }

    /**
     * 通过main函数输入参数，判断需要过滤的时间
     * 
     * @description
     * @author hechan
     * @date 2016年3月30日
     * @param args
     * @return 过滤时间
     */
    public static String getFilterDate(String[] args) {
        String filterDate = null;
        if (args.length >= 2) {
            filterDate = args[1];
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -1);
            filterDate = DateUtil.formatDateToStr(cal.getTime(), DateUtil.YMD_PATTERN);
        }
        Pattern filterDatePattern = Pattern.compile("\\d{8}");
        if (!filterDatePattern.matcher(filterDate).matches()) {
            logger.error("Filter date [{}] is incorrect,please enter in yyyyMMdd pattern.",
                    filterDate);
            filterDate = null;
        }
        logger.info("Filter date is {}", filterDate);
        return filterDate;
    }

    /**
     * 通过输入的参数，返回需要过滤的月份
     * 
     * @param args
     *            输入参数
     * @return 过滤时间
     */
    public static String getFilterMonth(String[] args) {
        String filterMonth = null;
        if (args.length >= 2) {
            filterMonth = args[1];
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            filterMonth = DateUtil.formatDateToStr(cal.getTime(), DateUtil.YM_PATTERN);
        }
        Pattern filterDatePattern = Pattern.compile("\\d{6}");
        if (!filterDatePattern.matcher(filterMonth).matches()) {
            logger.error("Filter date [{}] is incorrect,please enter in yyyyMM pattern.",
                    filterMonth);
            return null;
        }
        logger.info("Filter date is {}", filterMonth);
        return filterMonth;
    }

    /**
     * 根据指定的月份或系统月份，获取计算所需的起始月份和结束月份
     * 
     * @return startMonth 和 endMonth 字符串数组
     */
    public static String[] calculateStartMonthAndEndMonth(String monthAnalysis, int duration) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String startMonth = "";
        String endMonth = "";

        Calendar calendar = Calendar.getInstance();

        if (monthAnalysis != null && !"null".equals(monthAnalysis) && !monthAnalysis.isEmpty()) {
            try {
                calendar.setTime(formatter.parse(monthAnalysis));
                endMonth = formatter.format(calendar.getTime());
            } catch (ParseException e) {
                logger.error("The dateAnalysis format is wrong!");
                System.exit(0);
            }
        } else {
            calendar.add(Calendar.MONTH, -1);
            endMonth = formatter.format(calendar.getTime());
        }
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.MONTH, -1 * duration);
        startMonth = formatter.format(calendar.getTime());

        String[] month = new String[2];
        month[0] = endMonth;
        month[1] = startMonth;
        return month;
    }

    /**
     * 计算两个日期之间的间隔天数
     * 
     * @param s1
     * @param s2
     * @return
     */
    public static long dateDistance(String s1, String s2) {
        long start = 0L;
        long end = 0L;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            if (s1 == null || "".equals(s1)) {
                return -1L;
            }
            if (s2 == null || "".equals(s2)) {
                Date date = new Date();
                s2 = sdf.format(date).toString();
            }
            end = sdf.parse(s2).getTime();
            start = sdf.parse(s1).getTime();
            return (end - start) / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
            e.printStackTrace();
            return -2L;
        }
    }
}
