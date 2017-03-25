package com.example.administrator.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2017/3/25.
 */

public class MyView extends View {


//    public MyView(Context context) {
//        super(context);
//    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("jxy","onMeasure......");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
          Log.i("jxy","onLayout......");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
         Log.i("jxy","onDraw......");
    }
}
