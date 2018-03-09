package com.ai.androidinvaders3;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Fred6502 on 12/27/2017.
 */

public class AIWall {
    AI3Layout mLayout;
    AIAnimeTable mAIAT;
    int mNumInvaders;
    int mNumRows;
    int mWallRightMinusInvader;
    AIAnime mInvader[];
    float mX, mY;
    float mDeltaX, mDeltaY;
    long mNextTickMillis;
    int  mMillisPerTick;
    boolean mRight=true; // true=Right false=left
    boolean mMoveDown=false; // set true an invader passes left or right edge.

    AIWall(Bundle bundle, AI3Layout layout, AIAnimeTable aiat) {
        mAIAT = aiat;
        mLayout = layout;
        mWallRightMinusInvader = layout.mWallRight - aiat.GetWidth(AIAnime.AnimeEnum.INVADER_MIDDLE);
        if (bundle == null) {
            mX = mLayout.mWallLeft;
            mY = mLayout.mWallTop;
        } else {
            RestoreInstanceState(bundle);
            /* mX = bundle.getFloat("mX");
               mY = bundle.getFloat("mY"); */
        }
        mDeltaX = aiat.GetWidth(AIAnime.AnimeEnum.INVADER_MIDDLE)  / 2;
        mDeltaY = aiat.GetHeight(AIAnime.AnimeEnum.INVADER_MIDDLE) / 2;
        AllocateInvaders();
    }

    private void AllocateInvaders() {
        mNumRows = mLayout.mALIENS_TOP + mLayout.mALIENS_MIDDLE + mLayout.mALIENS_BOTTOM;
        mNumInvaders = mLayout.mALIENS_WIDE * (mNumRows);
        mInvader = new AIAnime[mNumInvaders];
        SetWall();
    }
    private int CalcInvaderX(int h) {
        return mLayout.mWallDeltaX * h + (int)mX;
    }
    private int CalcInvaderY(int v) {
        return mLayout.mWallDeltaY * v + (int)mY;
    }

    private void SetWall() {
        AIAnime animeLast = null;
        AIAnime.AnimeEnum invaderEnum;
        for (int h=0; h<mLayout.mALIENS_WIDE; h++) {
            for (int v=0; v<mNumRows; v++) {
                if (v >= (mLayout.mALIENS_TOP + mLayout.mALIENS_MIDDLE)) {
                    invaderEnum = AIAnime.AnimeEnum.INVADER_BOTTOM;
                } else if (v >= (mLayout.mALIENS_TOP)) {
                    invaderEnum = AIAnime.AnimeEnum.INVADER_MIDDLE;
                } else {
                    invaderEnum = AIAnime.AnimeEnum.INVADER_TOP;
                }
                int index = v * mLayout.mALIENS_WIDE + h;
                int x = CalcInvaderX(h); // mLayout.mWallDeltaX * h + (int)mX;
                int y = CalcInvaderY(v); // mLayout.mWallDeltaY * v + (int)mY;
                mInvader[index] = animeLast = mAIAT.New(invaderEnum, x, y, animeLast);
                animeLast.SetXVYV(1f,1f);
            }
        }
    }

    private void MoveWall() {
        if ( mRight ) {
            mX += mDeltaX;
        } else {
            mX -= mDeltaX;
        }
        if ( mMoveDown ) {
            mY += mDeltaY;
        }
        mMoveDown = false;
    }

    private void SetDestinations() {
        AIAnime invader;
        boolean wallAtBottom = false;
        int maxX = mLayout.mWallLeft;
        int minX = mLayout.mWallRight;
        for (int h=0; h<mLayout.mALIENS_WIDE; h++) {
            for (int v = 0; v < mNumRows; v++) {
                int index = v * mLayout.mALIENS_WIDE + h;
                invader = mInvader[index];
                if ( invader.AliveBoolean() ) {
                    int x = CalcInvaderX(h); // mLayout.mWallDeltaX * h + (int)mX;
                    int y = CalcInvaderY(v); // mLayout.mWallDeltaY * v + (int)mY;
                    if ( y > mLayout.mPlayerY ) wallAtBottom = true;
                    if (x > maxX) {
                        maxX = x;
                    } else if ( x < minX ) {
                        minX = x;
                    }
                    invader.SetDestination((float) x, (float) y);
                }
            }
        }
        if (maxX > mWallRightMinusInvader) { //mLayout.mWallRight)
            mRight    = false;
            mMoveDown = (!wallAtBottom) & true;
        }
        else if (minX < mLayout.mWallLeft) {
            mRight = true;
            mMoveDown = (!wallAtBottom) & true;
        }
    }

    public void Draw(Canvas canvas, Paint paint, boolean tick) {
        for (int h = 0; h < mLayout.mALIENS_WIDE; h++) {
            for (int v = 0; v < mNumRows; v++) {
                int index = v * mLayout.mALIENS_WIDE + h;
                AIAnime invader = mInvader[index];
                // mInvader[index]
                invader.draw(canvas, paint);     //= mAIAT.New(invaderEnum, x, y,null);
                if (tick) invader.tick();
            }
        }
        if ( tick ) {
            MoveWall();
            SetDestinations();
        }
    }

    public boolean Impact(AIAnime impactCheck, int step) {
        // Follow linked list of invaders. Check for impact.
        return mInvader[0].OverlapList(impactCheck,step);
    }
    public AIAnime[] GetAIAnimeArray() {
        return mInvader;
    }

    protected Bundle SaveInstanceState () {
        float xSave, ySave; // Set mX and mY based on where invaders are.
        Bundle bundle;
        bundle = new Bundle();
        xSave = mInvader[0].GetX();
        ySave = mInvader[0].GetY();
        if ( (Math.abs(xSave-mX) > mDeltaX) || (Math.abs(ySave-mY) > mDeltaY) ) {
            xSave = mX;
            ySave = mY;
        }
        bundle.putFloat("mX", xSave);
        bundle.putFloat("mY", ySave);
        bundle.putBoolean("mRight",mRight);
        return bundle;
    }

    protected void RestoreInstanceState (Bundle bundle) {
        mX = bundle.getFloat("mX");
        mY = bundle.getFloat("mY");
        mRight = bundle.getBoolean("mRight");
        //Log.d("DEBUG","Wall RestoreInstnaceState mX=" + mX + " mY=" + mY);
    }



}
