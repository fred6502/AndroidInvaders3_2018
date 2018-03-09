package com.ai.androidinvaders3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Fred6502 on 9/7/2017.
 */

public class AIPlayer {
    private AIShapeTable mST;
    private AIAnimeTable mAT;
    private AI3Layout mLayout;
    private AIPlayerMissile mPM;
    private Bitmap mBitmap;
    AIAnime mAPlayer;
    private boolean mAlive;
    //private float mXpos;
    private boolean mMoveLeft;
    private boolean mMoveRight;
    private AIAnime mTestAnime;
    public AIPlayer(AIAnimeTable at, AIShapeTable st, AI3Layout AI3Layout, AIPlayerMissile pm) {
        mST = st;  mAT = at;  mLayout = AI3Layout;  mPM = pm;
        InitAlive();
        MoveStop();
        mTestAnime = null; /*mAT.New(AIAnime.AnimeEnum.UFO, mLayout.mBarricadeX[1], (int)mLayout.mPlayerY - 18, null);*/
    }
    private void InitAlive() {
        mBitmap = mST.GetBitmapScaledCopy(AIShapeTable.DrawableEnum.PLAYER);
        mAPlayer = mAT.New(AIAnime.AnimeEnum.PLAYER,mLayout.mBarricadeX[0],mLayout.mPlayerY,null);
        mAPlayer.SetXVYV(mLayout.mPlayerSpeed,0);
        //mXpos = mLayout.mBarricadeX[0];
        mAlive = true;
    }
    private void InitDead() {
        mBitmap = mST.GetBitmapScaledCopy(AIShapeTable.DrawableEnum.PLAYER_DEAD);
        mAlive = false;
    }
    public void MoveLeft(boolean tf) {
        mMoveLeft=tf;
    }
    public void MoveRight(boolean tf) {
        mMoveRight=tf;
    }
    public void MoveStop() {
        mMoveLeft=false;  mMoveRight=false;
        mAPlayer.stop();
    }
    public void StilesCanYouHearMe_Fire() {
        mPM.StilesCanYouHearMeFire(mAPlayer.GetX() + mAPlayer.GetWidth() / 2 - mPM.GetWidth() / 2 , mAPlayer.GetY());
        //mPM.StilesCanYouHearMeFire(mXpos + mBitmap.getWidth() / 2 - mPM.GetWidth() / 2 , mLayout.mPlayerY);
    }
    public void Draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitmap, mAPlayer.GetX(), mLayout.mPlayerY,paint);
        /*
            canvas.drawBitmap(mBitmap, 100, mLayout.mPlayerY - 22,paint);
            mST.OverlapCheck(1, (int)mAPlayer.GetX(), mLayout.mPlayerY, mBitmap, 100, mLayout.mPlayerY - 22, mBitmap);
        */
        mAPlayer.draw(canvas,paint);
        if ( mTestAnime != null ) {
            mTestAnime.draw(canvas, paint);
            if (mTestAnime.OverlapCheck(mAPlayer, 1) != null) {
                Log.d("DEBUG", "Player Anime Overlap X=" + mAPlayer.GetX());
            }
            if (AIAnime.OverlapCheck(mTestAnime, false, false, mAPlayer, false, false, 1) != null) {
                Log.d("DEBUG", "Player UFO  X=" + mTestAnime.GetX() + "  Anime Overlap X=" + mAPlayer.GetX());
            }
        }
        if (mMoveLeft && !mMoveRight) {
            //mXpos -= mLayout.mPlayerSpeed;
            mAPlayer.SetDestDelta(-mLayout.mPlayerSpeed,0f);
            if ( mAPlayer.GetDestX() < mLayout.mPlayerLeft ) mAPlayer.SetDestinationX(mLayout.mPlayerLeft);
            //if (mXpos < mLayout.mPlayerLeft) mXpos = mLayout.mPlayerLeft;
        }
        else if (!mMoveLeft && mMoveRight) {
            //mXpos += mLayout.mPlayerSpeed;
            mAPlayer.SetDestDelta(mLayout.mPlayerSpeed, 0);
            if ( mAPlayer.GetDestX() > mLayout.mPlayerRight ) mAPlayer.SetDestinationX(mLayout.mPlayerRight);
        }
    }
}
