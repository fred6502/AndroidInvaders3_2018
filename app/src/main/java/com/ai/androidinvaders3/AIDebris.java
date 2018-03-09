package com.ai.androidinvaders3;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Fred6502 on 2/26/2018.
 */

public class AIDebris {
    private float mX, mY, mXV, mYV;
    private static int mWidth, mHeight;
    private static int mSize=2; // horizontal and vertical number of pixels.
    private static int mColorLast=0; // Used so to see if color changed so don't keep recalling Paint.Coolor
    private int mColor;
    private int mLifeClock;
    private int mType; // bit-0=GRAVITY
    AIDebris(    float x, float y, float xv, float yv, int color, int lifeClock, int type) {
        Reset(x, y, xv, yv, color, lifeClock, type);
    }
    AIDebris() {
        mLifeClock = 0;
    }
    void Reset(    float x, float y, float xv, float yv, int color, int lifeClock, int type) {
        mX = x;
        mY = y;
        mXV = xv;
        mYV = yv;
        mColor = color;
        mLifeClock = lifeClock;
        mType = type; // bit-0=GRAVITY
    }
    static void SetWidthHeight(int width, int height) {
        mWidth  = width;
        mHeight = height;
    }
    static void SetColorLast(int color) {
        mColorLast = color;
    }
    static void SetSize(int size) {
        mSize = size;
    }
    boolean Alive() {
        return mLifeClock > 0;
    }
    void Terminate() {
        mLifeClock = 0;
    }
    void Move() {
        if ( Alive() ) {
            mX += mXV;
            mY += mYV;
        }
    }
    boolean Draw(Canvas canvas, Paint paint) {
        mLifeClock--;
        if ( mLifeClock < 1) {
            return true;
        } else {
            mX += mXV;
            mY += mYV;
            mYV += 0.1; // Gravity
            if ( mX < 0f || (int)mX >= mWidth || (int)mY >= mHeight ) {
                Terminate();
                return true; // true tells AIDebrisArray to remove this debris.
            }
        }
        if (mColor != mColorLast) {
            paint.setColor(mColor);
            mColorLast = mColor;
        }
        if (mSize==1) {
            canvas.drawPoint(mX,mY,paint);
        } else {
            canvas.drawRect(mX,mY,mX+mSize-1,mY+mSize-1,paint);
        }
        return false;
    }
}
