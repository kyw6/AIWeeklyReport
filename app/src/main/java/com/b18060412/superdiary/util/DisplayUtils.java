package com.b18060412.superdiary.util;
import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayUtils {

    /**
     * 将 dp 转换为 px。
     *
     * @param context 上下文
     * @param dp      dp 值
     * @return 转换后的 px 值
     */
    public static float dpToPx(Context context, float dp) {
        // 获取屏幕密度
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        // 将 dp 转换为 px
        return dp * displayMetrics.density + 0.5f; // 0.5f 是为了四舍五入到最接近的整数
    }
}
