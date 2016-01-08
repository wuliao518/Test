package com.jiang.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by wuliao on 16/1/8.
 */
public class MyAdapterView extends ListView{

    public MyAdapterView(Context context) {
        this(context, null);
    }

    public MyAdapterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

    }
}
