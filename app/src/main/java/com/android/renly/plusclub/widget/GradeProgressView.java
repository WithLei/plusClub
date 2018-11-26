package com.android.renly.plusclub.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.android.renly.plusclub.R;


/**
 * Created by yang on 2016/12/12.
 * 用户经验条进度 view
 */

public class GradeProgressView extends View {
    private float progress;
    private ValueAnimator animator;
    private int widthTotal, totalHeight;
    private float width;
    private Paint paintBg, paintProgress;

    public GradeProgressView(Context context) {
        super(context);
        init(context);
    }

    public GradeProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GradeProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paintBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBg.setColor(ContextCompat.getColor(context, R.color.bg_secondary));
        paintProgress.setColor(ContextCompat.getColor(context, R.color.blue_light));
        paintBg.setStyle(Paint.Style.FILL);
        paintProgress.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthTotal = getMeasuredWidth();
        totalHeight = getMeasuredHeight();
    }

    public void setProgress(float progressTo) {
        if (progressTo > 1) progressTo = 1f;
        if (progressTo < 0.05) progressTo = 0.05f;
        long duration = (long) ((progressTo - progress) * 1500);
        animator = ValueAnimator.ofFloat(progress, progressTo);
        animator.setInterpolator(new AccelerateInterpolator(1.2f));
        animator.setDuration(duration);
        animator.addUpdateListener(valueAnimator -> {
            progress = (float) valueAnimator.getAnimatedValue();
            width = progress * widthTotal;
            if (width > widthTotal) width = widthTotal;
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawProgress(canvas);
    }

    private void drawBg(Canvas canvas) {
        float w = width;
        if (w > 5) {
            w -= 5;
        } else {
            w = 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(w, 0, widthTotal, totalHeight, 3, 3, paintBg);
            return;
        }
        RectF rectF = new RectF(w, 0, widthTotal, totalHeight);
        canvas.drawRoundRect(rectF, 2, 2, paintBg);
    }

    private void drawProgress(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(0, 0, width, totalHeight, 3, 3, paintProgress);
            return;
        }
        RectF rectF = new RectF(0, 0, width, totalHeight);
        canvas.drawRoundRect(rectF, 2, 2, paintProgress);
    }
}
