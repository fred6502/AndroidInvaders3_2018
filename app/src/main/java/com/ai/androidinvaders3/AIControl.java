package com.ai.androidinvaders3;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Fred6502 on 3/7/2018.
 * Four screen button.
 *   Fire (on left and right) Both Call AIPlayer.StileCanYouHereMe_Fire()
 *   MoveLeft  Calls AIPlayer.MoveLeft
 *   MoveRight Calls AIPlayer.MoveRight
 *
 */

public class AIControl {
    private AI3Layout mLayout;
    private AIPlayer  mPlayer;
    private int mTimeFlash;
    private int mTickFireLeft =0;
    private int mTickFireRight=0;
    private int mTickMoveLeft =0;
    private int mTickMoveRight=0;
    private int mTickScreen   =0; // If touched screen but not a button.
    private boolean mActionDown=false;


    AIControl(AI3Layout layout, AIPlayer player, int time_countdown) {
        mLayout    = layout;
        mPlayer    = player;
        mTimeFlash = time_countdown;
    }
    public void ActionDown(int x, int y) {
        boolean left = false;
        boolean right = false;
        boolean fire = false;
        boolean move = false;
        int dX; // distanceX distanceY
        int dY = Math.abs(mLayout.mControlYFire - y);
        int distance_squared;
        mActionDown = true;
        if ( x < mLayout.mControlRadius ) {
            dX = x;
            left = true;
        } else {
            dX = mLayout.mControlXRight - x;
            if ( dX < mLayout.mControlRadius ) {
                right = true;
            } else {
                mTickScreen = mTimeFlash;
                return ; // Not close to left or right so do nothing.
            }
        }
        // Got here so left or right is true.
        dY = Math.abs(mLayout.mControlYFire - y);
        if (dY < mLayout.mControlRadius) {
            fire = true;
        } else {
            dY = Math.abs(mLayout.mControlYMove - y);
            if ( dY < mLayout.mControlRadius) {
                move = true;
            } else {
                mTickScreen = mTimeFlash;
                return; // Not close to Move or Fire so do nothing.
            }
        }
        Log.d("DEBUG", "AIControl left="+left+" right="+right+" fire="+fire+" move="+move);
        // Got here so fire or move is true.
        // Buttons are half circle shape from side so measure distance since inside rectangle.
        distance_squared = dX^2 + dY^2;
        if ( distance_squared < mLayout.mControlRadiusSquared ) {
            Log.d("DEBUG","Control d^2="+distance_squared+" left="+left+" right="+right+" fire="+fire+" move="+move);
            if ( fire ) {
                mPlayer.StilesCanYouHearMe_Fire();
                if ( left ) mTickFireLeft  = mTimeFlash;
                else        mTickFireRight = mTimeFlash;
            } else if ( left ) {
                mPlayer.MoveLeft(true);
                mTickMoveLeft = mTimeFlash;
            } else if ( right ) {
                mPlayer.MoveRight( true);
                mTickMoveRight = mTimeFlash;
            }
        } else {
            mTickScreen = mTimeFlash;
        }
    }
    public void ActionUp(int x, int y) {
        mActionDown = false;
        mPlayer.MoveLeft(false);
        mPlayer.MoveRight( false);
    }
    public void Draw(Canvas canvas, Paint paint) {
        paint.setColor(0xffffff00);
        if (mTickFireLeft > 0 || mTickScreen > 0) {
            if ( mActionDown==false ) mTickFireLeft--;
            if ( mTickFireLeft > 0 ) paint.setColor(0xffffff00);
            else paint.setColor(0xff0000ff);
            canvas.drawCircle(0,mLayout.mControlYFire,mLayout.mControlRadius,paint);
        }
        if (mTickFireRight > 0 || mTickScreen > 0) {
            if ( mActionDown==false ) mTickFireRight--;
            if ( mTickFireRight > 0) paint.setColor(0xffffff00);
            else paint.setColor(0xff0000ff);
            canvas.drawCircle(mLayout.mControlXRight,mLayout.mControlYFire,mLayout.mControlRadius,paint);
        }
        if (mTickMoveLeft > 0 || mTickScreen > 0) {
            if ( mActionDown==false ) mTickMoveLeft--;
            if ( mTickMoveLeft > 0) paint.setColor(0xffffff00);
            else paint.setColor(0xff0000ff);
            canvas.drawCircle(0,mLayout.mControlYMove,mLayout.mControlRadius,paint);
        }
        if (mTickMoveRight > 0 || mTickScreen > 0) {
            if ( mActionDown==false ) mTickMoveRight--;
            if ( mTickMoveRight > 0) paint.setColor(0xffffff00);
            else paint.setColor(0xff0000ff);
            canvas.drawCircle(mLayout.mControlXRight,mLayout.mControlYMove,mLayout.mControlRadius,paint);
        }
        if ( mActionDown==false && mTickScreen > 0) mTickScreen--;
    }
}
