package com.b18060412.superdiary.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDateStringUtil {
    //后端返回的日期格式是 2024-08-14T00:00:00+08:00
    /**
     * 截取字符串的前10个字符。
     * 2024-08-14T00:00:00+08:00 -> 2024-08-14
     * @param str 输入的字符串
     * @return 字符串的前10个字符
     */
    public static String getFirstTenChars(String str) {
        if (str == null || str.length() <= 10) {
            return str;
        }
        return str.substring(0, 10);
    }

    /**
     * 将 字符串改成 yyyy-mm-dd 这种格式,传给后端
     *  3 8 2024 -> 2024-08-03
     * @param day ：3
     * @param month ：8
     * @param year ： 2024
     * @return
     */
    public static String formatDateToTransfer(String day, String month, String year){
        // 将 day 和 month 格式化为两位数
        String formattedDay = String.format("%02d", Integer.parseInt(day));
        String formattedMonth = String.format("%02d", Integer.parseInt(month));

// 拼接成 "YYYY-MM-DD" 格式
        String formattedDate = year + "-" + formattedMonth + "-" + formattedDay;
        return formattedDate;
    }

    /**
     * 将字符串改成中文的格式 例如2024年08月03日
     * 3 8 2024 -> 2024年08月03日
     * @param selectDay 3
     * @param selectMonth 8
     * @param selectYear 2024
     * @return
     */
    public static String formatDateToChinese(String selectDay, String selectMonth, String selectYear) {
        // 将 day 和 month 格式化为两位数
        String formattedDay = String.format("%02d", Integer.parseInt(selectDay));
        String formattedMonth = String.format("%02d", Integer.parseInt(selectMonth));

        // 拼接成 "yyyy年MM月dd日" 格式
        String formattedDate = selectYear + "年" + formattedMonth + "月" + formattedDay + "日";

        return formattedDate;
    }

    public static String formatDateToMonthDayChinese(String inputDate) {
        // 定义输入的日期格式
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 定义输出的日期格式
        SimpleDateFormat outputFormat = new SimpleDateFormat("M月d日");

        try {
            // 将输入的字符串转换为 Date 对象
            Date date = inputFormat.parse(inputDate);
            // 将 Date 对象格式化为目标字符串格式
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // 如果解析失败，返回空字符串
        }
    }

    /**
     * 2024-08-03 -> 2024-08-20T00:00:00+08:00
     */
    public static String formatDateToServer(String formattedDate) {
        String result = formattedDate + "T00:00:00+08:00";
        return result;
    }


    /**
     * 传入一个参数 2020-01-01 这样的字符串，要求返回这天所在周的周一 和周天的时间，要求返回两个字符串
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static String[] getWeekStartAndEnd(String dateStr) throws ParseException {
        // 解析输入的日期字符串
        Date date = DATE_FORMAT.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 确定输入日期是一周中的哪一天
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // 计算周一的日期
        // 如果是周日，则直接使用当前日期作为周日，并计算本周的周一
        if (dayOfWeek == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -6); // 调整到周一
        } else {
            int daysToMonday = dayOfWeek - Calendar.MONDAY;
            calendar.add(Calendar.DAY_OF_MONTH, -daysToMonday); // 调整到周一
        }
        String weekStart = DATE_FORMAT.format(calendar.getTime());

        // 计算周日的日期
        calendar.add(Calendar.DAY_OF_MONTH, 6); // 从周一开始加6天
        String weekEnd = DATE_FORMAT.format(calendar.getTime());

        return new String[]{weekStart, weekEnd};
    }


}
