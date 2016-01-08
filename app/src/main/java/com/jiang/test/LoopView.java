package com.jiang.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by wuliao on 16/1/5.
 */
public class LoopView extends View {
    private Adapter mAdapter;
    public LoopView(Context context) {
        this(context, null);
    }

    public LoopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.item_test,null,false);
        text = (TextView) view.findViewById(R.id.text);
        image = (ImageView) view.findViewById(R.id.image);
//        addView(view);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        image.layout(0,0,600,600);
    }

    private TextView text;
    private ImageView image;

//    @Override
//    public Adapter getAdapter() {
//        return mAdapter;
//    }
//
//    @Override
//    public void setAdapter(Adapter adapter) {
//        this.mAdapter = adapter;
//    }
//
//    @Override
//    public View getSelectedView() {
//        return null;
//    }
//
//    @Override
//    public void setSelection(int position) {
//
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.BLACK);
//        TextView textView=new TextView(getContext());
//        textView.setBackgroundColor(Color.WHITE);
//        ViewGroup.LayoutParams params = textView.getLayoutParams();
//        params.width=ViewGroup.LayoutParams.WRAP_CONTENT;
//        params.height=ViewGroup.LayoutParams.WRAP_CONTENT;


    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_test,null,false);
//        TextView text = (TextView) view.findViewById(R.id.text);
        canvas.save();
        image.draw(canvas);
        canvas.restore();
    }
}
