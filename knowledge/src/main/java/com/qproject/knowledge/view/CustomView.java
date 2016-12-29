package com.qproject.knowledge.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.qproject.knowledge.R;

/**
 * Created by chengxiang.peng on 2016/12/30.
 */

public class CustomView extends View {
    private int fillColor = Color.BLACK;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        fillColor = typedArray.getColor(R.styleable.CustomView_fillColor, Color.BLACK);
        typedArray.recycle();
        
        init();
    }

    private void init() {
        //设置画笔的颜色
        paint.setColor(fillColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(50, 50, 30, paint);
    }
}
