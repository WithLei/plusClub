package com.android.renly.plusclub.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyCircleView extends View {

    private int color = 0xffff0000;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean isSelect = false;

    public MyCircleView(Context context) {
        super(context);
        init();
    }

    public MyCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
    }

    public void setColor(int color) {
        this.color = color | 0xff000000;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(this.color);
        invalidate();
    }

    public void setSelect(boolean select) {
        if (select == isSelect) return;
        isSelect = select;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(color);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.min(getHeight() / 2, getHeight() / 2), paint);
        if (isSelect) {
            paint.setColor(0xffffffff);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.min(getHeight() / 2, getHeight() / 2) / 3, paint);
        }
    }
}
