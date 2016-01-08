package com.jiang.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by wuliao on 16/1/8.
 */
public class MyView extends View {
    GestureDetector gestureDetector;
    MyGestureListener gestureListener;
    int visibleSize = 5;
    float scrollY = 0;
    int itemHeight=150;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private Paint paint;
    float offsetY;
    private void init() {
        gestureListener = new MyGestureListener();
        gestureDetector = new GestureDetector(getContext(), gestureListener);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        offsetY = (metrics.bottom-metrics.top)/2-metrics.bottom+itemHeight/2;

    }

    float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean gestureConsumed = gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getRawY();
                scrollY = scrollY + y - lastY;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
            default:
                break;

        }
        invalidate();
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,5*itemHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int position = (int) (scrollY / itemHeight)*-1;
        float surplus = scrollY%itemHeight;

        for (int i = 0; i < visibleSize+1; i++) {
            if(i==0){
                canvas.save();
                canvas.translate(0,surplus);
            }
           canvas.drawText("位置" + (position + i), 0, offsetY + i * itemHeight, paint);
            Paint tempPaint = new Paint();
            tempPaint.setColor(Color.GREEN);
            canvas.drawLine(0,i*itemHeight,500,i*itemHeight,tempPaint);
        }
        canvas.restore();

    }

    private class MyGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }


}
