package com.ai.androidinvaders3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

/**
 * Created by Fred6502 on 7/26/2017.
 */

public class AICanvas extends View {
    private Paint mPaint;
    private Random mRandom;
    private AI3Layout mAI3Layout;
    private AIDrawAll mAIDrawAll;
    public AICanvas(Context context) {
        super(context);
        AICanvasInit();
    }

    public AICanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        AICanvasInit();
    }

    public AICanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        AICanvasInit();
    }

    /*
    public AICanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        AICanvasInit();
    }
    */
    private void AICanvasInit() {
        mPaint = new Paint();
        mPaint.setAntiAlias(Boolean.FALSE);
        mRandom = new Random();
        //mRandom.setSeed(1l);
    }
    public void setLayout(AI3Layout AI3Layout) {
        mAI3Layout = AI3Layout;
    }

    public void setDrawAll(AIDrawAll AIDrawAll) {
        mAIDrawAll = AIDrawAll;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAI3Layout ==null) {
            Log.d("ERROR","OnDraw called with mAI3Layout==null");
            return;
        }
        /*
        for (int i=0; i<20; i++) {
            mPaint.setARGB(0xff, mRandom.nextInt() & 0xff, mRandom.nextInt() & 0xff, mRandom.nextInt() & 0xff);
            canvas.drawLine(mRandom.nextFloat() * mAI3Layout.mWidth, mRandom.nextFloat() * mAI3Layout.mHeight, mRandom.nextFloat() * mAI3Layout.mWidth, mRandom.nextFloat() * mAI3Layout.mHeight, mPaint);
        }
        */
        mPaint.setAntiAlias(false);
        mAIDrawAll.Draw(canvas,mPaint);
        //Log.d("DEBUG","AICanvas Width:" + canvas.getWidth() +" Height:" + canvas.getHeight());
    }
}
