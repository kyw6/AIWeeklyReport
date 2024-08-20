package com.b18060412.superdiary.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

// 自定义的月份视图类，继承自 MonthView，
// 用于自定义每一天的绘制效果。
public class MyMonthView extends MonthView {
    private static final float OFFSET = -1.5f; // 你可以根据需要调整这个值
    // 文本画笔，用于绘制日期文字
    private Paint mTextPaint = new Paint();

    // Scheme (标记) 画笔，用于绘制带标记的日期
    private Paint mSchemeBasicPaint = new Paint();
    private float mRadio; // 圆的半径，用于绘制标记点
    private int mPadding; // 内边距，用于调整绘制的边距
    private float mSchemeBaseLine; // 基线，用于文本绘制的垂直对齐

    // 构造方法，初始化画笔和绘制参数
    public MyMonthView(Context context) {
        super(context);

        // 初始化文本画笔的属性
        mTextPaint.setTextSize(dipToPx(context, 12));
        mTextPaint.setColor(0xffffffff); // 白色文字
        mTextPaint.setAntiAlias(true); // 抗锯齿
        mTextPaint.setFakeBoldText(true); // 加粗

        // 初始化 Scheme 画笔的属性
        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL); // 填充样式
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER); // 居中对齐
        mSchemeBasicPaint.setFakeBoldText(true); // 加粗

        // 初始化半径、内边距和基线高度
        mRadio = dipToPx(getContext(), 7);
        mPadding = dipToPx(getContext(), 4);
        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mRadio - metrics.descent +
                (metrics.bottom - metrics.top) / 2 +
                dipToPx(getContext(), 1);
    }

    // 绘制选中的日期背景
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        // 设置为只绘制边框样式
        mSelectedPaint.setStyle(Paint.Style.STROKE);
        mSelectedPaint.setStrokeWidth(dipToPx(getContext(), 2)); // 设置边框宽度
        // 设置边框颜色
        mSelectedPaint.setColor(Color.parseColor("#FFB21D"));

        // 计算圆心的坐标
        float cx = x + mItemWidth / 2f; // 使用 float 确保计算准确
        float cy = y + mItemHeight / 2f + dipToPx(getContext(), OFFSET); // 使用 float 确保计算准确

        // 计算半径，取宽和高中的最小值除以2，再减去padding
        float radius = Math.min(mItemWidth, mItemHeight) / 2f - mPadding;

        // 绘制圆形边框
        canvas.drawCircle(cx, cy, radius, mSelectedPaint);

        return true; // 返回 true 表示自定义选中效果
    }


    // 绘制带有 Scheme (标记) 的日期
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        // 设置标记的小圆点颜色为金黄色
        mSchemeBasicPaint.setColor(Color.parseColor("#FFB21D")); // 金黄色

        // 设置为填充样式，去除边框
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);

        // 计算日期单元格的中心坐标
        float centerX = x + mItemWidth / 2f; // 使用 float 确保计算准确
        float centerY = y + mItemHeight / 2f + dipToPx(getContext(), OFFSET); // 使用 float 确保计算准确

        // 计算半径，取宽和高中的最小值除以2，再减去padding
        float radius = Math.min(mItemWidth, mItemHeight) / 2f - mPadding;

        // 绘制标记的小圆点
        canvas.drawCircle(centerX, centerY, radius, mSchemeBasicPaint);


    }




    // 获取文本的宽度，用于居中对齐
    private float getTextWidth(String text) {
        return mTextPaint.measureText(text);
    }

    // 绘制日期文字
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2; // 计算中心 x 坐标
        int top = y - mItemHeight / 6; // 计算文本顶部 y 坐标

        // 检查日期是否在有效范围内
        boolean isInRange = isInRange(calendar);

        if (isSelected) {
            // 绘制选中日期的文字
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mSelectTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSelectedLunarTextPaint);
        } else if (hasScheme) {
            // 绘制带有 Scheme 的日期文字
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentMonth() && isInRange ? mSchemeTextPaint : mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mCurMonthLunarTextPaint);
        } else {
            // 绘制普通日期文字
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() && isInRange ? mCurMonthTextPaint : mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
                    calendar.isCurrentDay() && isInRange ? mCurDayLunarTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
        }
    }

    // 将 dp 转换为 px 的工具方法
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
