package com.b18060412.superdiary.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.b18060412.superdiary.util.DisplayUtils;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

// 自定义的月份视图类，继承自 MonthView，
// 用于自定义每一天的绘制效果。
public class MyMonthView extends MonthView {
    private static final float OFFSET = -19f; // 你可以根据需要调整这个值
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
        mSelectedPaint.setColor(Color.parseColor("#FFFF8514"));

        // 获取日历文字宽度和高度
        String dayText = String.valueOf(calendar.getDay());
        float textWidth = mTextPaint.measureText(dayText);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        // 计算文字的中心位置
        float centerX = x + (mItemWidth - textWidth) / 2f + textWidth / 2f;
        float centerY = y + (mItemHeight - textHeight) / 2f + (textHeight - fontMetrics.bottom) / 2f + OFFSET;

        // 半径15
        float radius = DisplayUtils.dpToPx(getContext(), 15);
        // 绘制圆形边框
        canvas.drawCircle(centerX, centerY, radius, mSelectedPaint);

        return true; // 返回 true 表示自定义选中效果
    }

    // 绘制带有 Scheme (标记) 的日期
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        mSchemeBasicPaint.setColor(Color.parseColor("#FFB21D"));

        // 设置为填充样式，去除边框
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        //设置日历文字的颜色:白色
        mSchemeTextPaint.setColor(Color.WHITE);

        // 获取日历文字宽度和高度
        String dayText = String.valueOf(calendar.getDay());
        float textWidth = mTextPaint.measureText(dayText);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        // 计算文字的中心位置
        float centerX = x + (mItemWidth - textWidth) / 2f + textWidth / 2f;
        float centerY = y + (mItemHeight - textHeight) / 2f + (textHeight - fontMetrics.bottom) / 2f + OFFSET;

        // 半径15dp
        float radius = DisplayUtils.dpToPx(getContext(), 15);

        // 绘制
        canvas.drawCircle(centerX, centerY, radius, mSchemeBasicPaint);
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
            // 在绘制文本之前先绘制选中的边框
            onDrawSelected(canvas, calendar, x, y, hasScheme);
        }

        if (calendar.isCurrentDay()) {
            // 设置今天的日期文字为黄色
            mCurDayTextPaint.setColor(Color.parseColor("#FFB21D"));
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mCurDayTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mCurDayLunarTextPaint);
        } else if (hasScheme) {
            // 设置带有 Scheme 的日期文字为白色
            mSchemeTextPaint.setColor(Color.WHITE);
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentMonth() && isInRange ? mSchemeTextPaint : mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mCurMonthLunarTextPaint);
        } else {
            // 设置普通日期的文字颜色为黑色
            mCurMonthTextPaint.setColor(Color.BLACK);
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentMonth() && isInRange ? mCurMonthTextPaint : mOtherMonthTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
                    calendar.isCurrentMonth() ? mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
        }
    }

    // 将 dp 转换为 px 的工具方法
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
