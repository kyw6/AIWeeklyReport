package com.b18060412.superdiary.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.RangeMonthView;

public class GeneReportMonthView extends RangeMonthView {
    private final Paint mTextPaint = new Paint();
    private Paint mSelectedPaint = new Paint();
    private Paint mSelectedRangePaint = new Paint();

    public GeneReportMonthView(Context context) {
        super(context);

        // 初始化文本画笔
        mTextPaint.setTextSize(dipToPx(context, 12));
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        // 初始化选中背景画笔
        mSelectedPaint.setColor(Color.parseColor("#FFFF8514"));
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setAntiAlias(true);

        // 初始化选中范围背景画笔
        mSelectedRangePaint.setColor(Color.parseColor("#FFB21D"));
        mSelectedRangePaint.setStyle(Paint.Style.FILL);
        mSelectedRangePaint.setAntiAlias(true);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelectedPre, boolean isSelectedNext) {
        if (isSelectedPre && isSelectedNext) {
            // 绘制选中范围的背景
            canvas.drawRect(x, y, x + mItemWidth, y + mItemHeight, mSelectedRangePaint);
        } else if (isSelectedPre) {
            // 绘制从当前选中日期到前一个选中日期的背景
            float endX = x + mItemWidth;
            float endY = y + mItemHeight;
            canvas.drawRect(x, y, endX, endY, mSelectedRangePaint);
        } else if (isSelectedNext) {
            // 绘制从当前选中日期到下一个选中日期的背景
            float endX = x + mItemWidth;
            float endY = y + mItemHeight;
            canvas.drawRect(x, y, endX, endY, mSelectedRangePaint);
        } else {
            // 绘制单个选中日期的背景
            canvas.drawRect(x, y, x + mItemWidth, y + mItemHeight, mSelectedPaint);
        }
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        // 如果日期有标记，绘制标记
        if (hasScheme) {
            mTextPaint.setColor(Color.WHITE);
            canvas.drawCircle(x + mItemWidth / 2, y + mItemHeight / 2, dipToPx(getContext(), 7), mTextPaint);
        }
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        // 绘制日期文本
        int cx = x + mItemWidth / 2;
        int top = y + mItemHeight / 2;
        canvas.drawText(String.valueOf(calendar.getDay()), cx, top, mTextPaint);
    }

    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
