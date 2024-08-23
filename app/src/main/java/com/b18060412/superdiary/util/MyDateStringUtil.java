package com.b18060412.superdiary.util;

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

    /**
     * 2024-08-03 -> 2024-08-20T00:00:00+08:00
     */
    public static String formatDateToServer(String formattedDate) {
        String result = formattedDate + "T00:00:00+08:00";
        return result;
    }
}
