package com.android.renly.plusclub.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.android.renly.plusclub.R;
import com.android.renly.plusclub.utils.DimmenUtils;

/**
 * 自定义标题栏，用来显示周一到周日
 */
public class WeekTitleView extends View {
    private Context context;
    //保存当前的日期
    private int day;

    private Paint mPaint;

    private String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    public WeekTitleView(Context context) {
        super(context);
        this.context = context;
    }

    public WeekTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        day = 0;
        mPaint = new Paint();
    }

    /**
     * 重写测量函数，否则在设置wrap_content的时候默认为match_parent
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setCurrentDay(int day) {
        this.day = day;
    }

    public void onDraw(Canvas canvas) {
        //获得当前View的宽度
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int offset = (width - DimmenUtils.dip2px(context,25)) / 7;
        int currentPosition = DimmenUtils.dip2px(context,25);
        //设置要绘制的字体
        mPaint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
        mPaint.setTextSize(30);
        mPaint.setColor(ThemeUtil.getThemeColor(context, R.attr.colorAccent));

        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        Rect bounds = new Rect();
        mPaint.getTextBounds(days[0], 0, days[0].length(), bounds);
        int h = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

        for (int i = 0; i < 7; i++) {
            //圈出当前的日期
            if (day == i) {
                System.out.println("画出当前的日期!");
            }

            int w = (currentPosition * 2 + offset)/2 - bounds.width()/2;
            canvas.drawText(days[i], w, h, mPaint);
            currentPosition += offset;
        }
        //调用父类的绘图方法
        super.onDraw(canvas);
    }

}
