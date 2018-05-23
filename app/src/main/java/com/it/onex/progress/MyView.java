package com.it.onex.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by OnexZgj on 2018/5/23:09:30.
 * des:
 */

public class MyView extends View {

    private int mWidth = 300;
    private int mHeight = 300;
    private Paint mPaintCircle;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 1);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStrokeWidth(5);
        mPaintCircle.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - mPaintCircle.getStrokeWidth() , mPaintCircle);

        canvas.save();

        for (int i = 0; i < 12; i++) {

            if (i == 0|| i==3||i==6|| i==9) {
                float strokeWidth = mPaintCircle.getStrokeWidth();
                mPaintCircle.setStrokeWidth(10);
                float textHeight = (mPaintCircle.descent() + mPaintCircle.ascent()) / 2;

                canvas.drawLine(mWidth / 2 , strokeWidth, mWidth / 2, strokeWidth+20, mPaintCircle);
                mPaintCircle.setTextSize(sp2px(15));
                mPaintCircle.setStrokeWidth(1);
                mPaintCircle.setColor(Color.BLUE);
                canvas.drawText(i+"",mWidth/2-mPaintCircle.measureText(i+"")/2,mPaintCircle.getStrokeWidth()+textHeight+50,mPaintCircle);
                canvas.rotate(30, mWidth / 2, mHeight / 2);
            } else {
                mPaintCircle.setStrokeWidth(3);
                mPaintCircle.setColor(Color.BLACK);
                canvas.drawLine(mWidth / 2 , mPaintCircle.getStrokeWidth(), mWidth / 2, mPaintCircle.getStrokeWidth()+15, mPaintCircle);
                canvas.rotate(30, mWidth / 2, mHeight / 2);
            }

        }

    }

    public  int sp2px(int spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spVal,getResources().getDisplayMetrics());
    }

}
