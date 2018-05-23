package com.it.onex.onexprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by Linsa on 2018/5/19:22:47.
 * des:
 */

public class OnexRoundProgress extends OnexHorizontalProgress {

    /**
     * 使用线段进行全部填充
     */
    private boolean mIsLineFill=true;
    private boolean mIsDrawUnReach=true;
    /**
     * 默认半径
     */
    private int mRadius = dp2px(30);
    private int mMaxPaintWidth;
    /**
     * 是否进行填充圆形
     */
    private boolean mIsFill = false;


    public OnexRoundProgress(Context context) {
        this(context, null);
    }

    public OnexRoundProgress(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public OnexRoundProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        mReachHeight= (int) (mUnReachHeight*2.5f);

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.OnexRoundProgress);
        mRadius = (int) ta.getDimension(R.styleable.OnexRoundProgress_radius, mRadius);
        mIsFill = ta.getBoolean(R.styleable.OnexRoundProgress_is_fill, mIsFill);
        mIsLineFill = ta.getBoolean(R.styleable.OnexRoundProgress_is_line_fill, mIsLineFill);
        mIsDrawUnReach = ta.getBoolean(R.styleable.OnexRoundProgress_is_draw_unreach, mIsDrawUnReach);

        ta.recycle();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        //设置paint为圆形的箭帽
        mPaint.setStrokeCap(Paint.Cap.ROUND);


    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxPaintWidth = Math.max(mReachHeight, mUnReachHeight);
        int expect = mRadius * 2 + getPaddingLeft() + getPaddingRight() + mMaxPaintWidth;

        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);

        int realWidth = Math.min(width, height);

        mRadius = (realWidth - getPaddingRight() - getPaddingLeft() - mMaxPaintWidth) / 2;

        setMeasuredDimension(realWidth, realWidth);
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();


//        mPaint.setShader(mShader);
        canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop() + mMaxPaintWidth / 2);
        mPaint.setStyle(Paint.Style.STROKE);
        //draw unreachbar

        if (mIsDrawUnReach) {
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        }

        if (mIsFill) {

            if (mIsLineFill) {
                drawText(canvas);
                canvas.restore();
                mPaint.setShader(mShader);
                mPaint.setColor(mReachColor);
                mPaint.setStrokeWidth(mUnReachHeight);
                int percent = (int) (getProgress() * 1.0f / getMax() * 60);
                for (int j = 0; j < percent; j++) {
                    canvas.drawLine(mRadius, mPaint.getStrokeWidth(), mRadius, mPaint.getStrokeWidth() + 20, mPaint);
                    canvas.rotate(6, mRadius + mPaint.getStrokeWidth(), mRadius + mPaint.getStrokeWidth());
                }
            }else{


                mPaint.setShader(mShader);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mReachColor);
                mPaint.setStrokeWidth(mReachHeight);
                float sweepAngle = getProgress() * 1.0f / getMax() * 360;
                canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), -90, sweepAngle, true, mPaint);

                mPaint.setShader(tempShader);
                drawText(canvas);
            }
        } else {
            mPaint.setShader(tempShader);
            //draw reachbar
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            float sweepAngle = getProgress() * 1.0f / getMax() * 360;
            canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), -90, sweepAngle, false, mPaint);

            drawText(canvas);
        }

        canvas.restore();

    }

    private void drawText(Canvas canvas) {
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);
        int textHeight = (int) ((mPaint.descent() + mPaint.ascent()) / 2);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        canvas.drawText(text, mRadius - textWidth / 2, mRadius - textHeight, mPaint);
    }
}
