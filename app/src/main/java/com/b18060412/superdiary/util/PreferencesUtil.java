package com.b18060412.superdiary.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

    private static final String PREFS_NAME = "MyPrefs"; // 共享偏好设置文件名
    private static SharedPreferences sharedPreferences; // 共享偏好设置实例
    private static SharedPreferences.Editor editor; // 共享偏好设置编辑器

    // 初始化 SharedPreferences
    public static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    // 保存布尔值
    public static void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply(); // 异步保存数据
    }

    // 获取布尔值
    public static boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    // 保存字符串值
    public static void setString(String key, String value) {
        editor.putString(key, value);
        editor.apply(); // 异步保存数据
    }

    // 获取字符串值
    public static String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    // 保存整数值
    public static void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply(); // 异步保存数据
    }

    // 获取整数值
    public static int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    // 保存浮点值
    public static void setFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.apply(); // 异步保存数据
    }

    // 获取浮点值
    public static float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    // 保存长整型值
    public static void setLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply(); // 异步保存数据
    }

    // 获取长整型值
    public static long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    // 清除所有偏好设置
    public static void clear() {
        editor.clear();
        editor.apply(); // 异步清除数据
    }
}
