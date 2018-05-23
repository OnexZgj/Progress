package com.it.onex.onexprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Created by OnexZgj on 2018/5/19:17:32.
 * des:
 */

public class OnexHorizontalProgress extends ProgressBar {

    private static final int DEFAULT_TEXT_SIZE=10 ;
    private static final int DEFAULT_REACH_COLOR=0xFFff00ff ;
    private static final int DEFAULT_UNREACH_HEIGHT=2 ;
    private static final int DEFAULT_REACH_HEIGHT=2 ;
    private static final int DEFAULT_TEXT_COLOR=0xFFff0000 ;
    private static final int DEFAULT_TEXT_OFFSET=10 ;
    private static final int DEFAULT_UNREACH_COLOR=0xFF0000ff;
    protected LinearGradient mShader;


    protected int mTextSize= sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor=DEFAULT_TEXT_COLOR;
    protected int mUnReachColor=DEFAULT_UNREACH_COLOR;
    protected int mReachColor=DEFAULT_REACH_COLOR;
    protected int mUnReachHeight=dp2px(DEFAULT_UNREACH_HEIGHT);
    protected int mReachHeight=dp2px(DEFAULT_REACH_HEIGHT);
    protected int mTextOffset=dp2px(DEFAULT_TEXT_OFFSET);


    /**
     * the color of start progress
     */
    protected int mStartColor = getResources().getColor(R.color.purple_start);
    /**
     * the color of end progress
     */
    protected int mEndColor = getResources().getColor(R.color.purple_end);

    protected Paint mPaint=new Paint();
    private int mRealWidth;

    private float progressX;
    protected Shader tempShader;

    public OnexHorizontalProgress(Context context) {
        this(context,null);
    }

    public OnexHorizontalProgress(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public OnexHorizontalProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(attrs);
        mPaint.setTextSize(mTextSize);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        tempShader = mPaint.getShader();

    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        mRealWidth = MeasureSpec.getSize(widthMeasureSpec)-getPaddingRight();

        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(mRealWidth,height);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mShader = new LinearGradient(getPaddingLeft()-50, (getHeight()-getPaddingTop())-50, getWidth() - getPaddingRight(), getHeight()/2 + getPaddingTop() ,
                mStartColor, mEndColor, Shader.TileMode.CLAMP);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(),getHeight()/2);

        //是否需要绘制未到的bar
        boolean isNoNeedReach=false;

        //drawReach
        float radio=getProgress()*1.0f/getMax();

        String text=getProgress()+"%";
        int textWidth= (int) mPaint.measureText(text);

        progressX = radio * mRealWidth;
        if (progressX+textWidth>mRealWidth){
            progressX=mRealWidth-mTextOffset*2-textWidth;
            isNoNeedReach=true;
        }

        float endReachX=progressX-mTextOffset/2;

        if (endReachX>0){

            mPaint.setShader(mShader);
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0,0,endReachX,0,mPaint);
        }

        //drawText
        mPaint.setColor(mTextColor);
        int y= (int) (-(mPaint.descent()+mPaint.ascent())/2);

        //todo
        canvas.drawText(text,progressX+mTextOffset,y,mPaint);

        //drawUnReach
        if (!isNoNeedReach){
            float startX = progressX + mTextOffset * 2 + textWidth;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(startX,0,mRealWidth,0,mPaint);
        }


        canvas.restore();


    }

    /**
     * 测量高度
     * @param heightMeasureSpec
     */
    private int measureHeight(int heightMeasureSpec) {
        int result=0;
        int mode=MeasureSpec.getMode(heightMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);

        if (mode==MeasureSpec.EXACTLY){
            result=height;
        }else{
            int textHeight= (int) (mPaint.descent()-mPaint.ascent());
            result=getPaddingBottom()+getPaddingTop()+Math.max(Math.max(mReachHeight,mUnReachHeight),textHeight);

            if (mode==MeasureSpec.UNSPECIFIED){
                result =Math.min(result,height);
            }

        }
        return result;

    }

    //获取实际的属性值
    private void obtainStyledAttrs(AttributeSet attrs) {
        //TODO
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.OnexHorizontalProgress);

        mTextSize=ta.getDimensionPixelSize(R.styleable.OnexHorizontalProgress_text_size,mTextSize);
        mTextOffset=ta.getDimensionPixelOffset(R.styleable.OnexHorizontalProgress_text_offset,mTextOffset);
        mTextColor=ta.getColor(R.styleable.OnexHorizontalProgress_text_color,mTextColor);
        mReachColor=ta.getColor(R.styleable.OnexHorizontalProgress_reach_color,mReachColor);
        mUnReachColor=ta.getColor(R.styleable.OnexHorizontalProgress_unreach_color,mUnReachColor);
        mReachHeight=ta.getDimensionPixelOffset(R.styleable.OnexHorizontalProgress_reach_height,mReachHeight);
        mUnReachHeight=ta.getDimensionPixelOffset(R.styleable.OnexHorizontalProgress_unreach_height,mUnReachHeight);

        ta.recycle();

    }

    /** dp2px */
    public  int dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,getResources().getDisplayMetrics());
    }

    public  int sp2px(int spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spVal,getResources().getDisplayMetrics());
    }

}
