package com.ai.androidinvaders3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import static com.ai.androidinvaders3.AIAnime.AnimeEnum.INVADER_BOTTOM;

/**
 * Created by Fred6502 on 8/4/2017.
 * The idea is everything or almost everything drawn is done using AIAnime..
 * Invaders, Barricades, Player, Ground.. hmm what else?
 * So it is easier to check for impacts.. Can just do did this hit anything and not
 * need special functions for different objects.
 */

public class AIAnime {
    private float mX, mY; // current location
    private float mXdest, mYdest; // destination
    private float mXV, mYV; // speed toward destination;
    private int mAlive; // 0=dead, <0 = dead dying explosion counting up to 0.
    private int mCurrentShape;
    private Bitmap[] mBitmapArray; // mBitmapArray.length; 2 invader shapes
    private int mExplosionShape;
    private int mExplosionTick;  // When mExplosionTick reaches ..Count ..Shape is incremeneted and Tick set to 0.
    private int mExplosionCount; // Bigger Count causes slower explosion shape change... If > 1 shape.
    private Bitmap[] mExplosionArray;
    private AnimeEnum mATEnum; // Animal table enum converted to integer. -1 means not set.
    private AIAnime mNextShape; // NOT USING - When multiple shapes drawn a linked list of shapes is set.
    private static final int mMaxListLength = 200;
    // IMPACT
    private Overlap mOverlap; // Impact from AIShapeTable.OverlapCheck()
    private AIAnime mImpactAnime;   // Anime which hit us.
    public enum AnimeEnum {
        ALIEN_SHOT_A, ALIEN_SHOT_B, ALIEN_SHOT_C, BARRICADE, BLAST, EXPLOSION, INVADER_BOTTOM, INVADER_MIDDLE, INVADER_TOP, PLAYER, PLAYER_MISSILE, UFO;
    };

    public AIAnime(AIAnime aid ) {
        mX = aid.mX;
        mY = aid.mY;
        mXdest = aid.mXdest;
        mYdest = aid.mYdest;
        mXV = aid.mXV;
        mYV = aid.mYV;
        mAlive = aid.mAlive;
        mCurrentShape = aid.mCurrentShape;
        mBitmapArray = aid.mBitmapArray;
        mATEnum = aid.mATEnum;
        mNextShape = null;
    }
    public AIAnime(AIAnime aid, float x, float y, AIAnime next ) {
        AIAnimeInit(aid, x, y, next );
    }
    public AIAnime(AIAnime aid, float x, float y, AnimeEnum ATEnum, AIAnime next ) {
        AIAnimeInit(aid, x, y, next );
        mATEnum = ATEnum;
    }

    private void AIAnimeInit(AIAnime aid, float x, float y, AIAnime next ) {
        mX = x;
        mY = y;
        mXdest = x;
        mYdest = y;
        mXV = aid.mXV;
        mYV = aid.mYV;
        mAlive = 1;
        mCurrentShape = 0;
        mBitmapArray = CopyBitmapArray(aid.mBitmapArray);
        mATEnum = aid.mATEnum;
        mNextShape = next;
    }
    /*
    public AIAnime(Bitmap bitmap, float x, float y) {
        mNextShape = null;
        mBitmapArray = new Bitmap[1];
        mBitmapArray[0] = bitmap;
        mATEnum = AnimeEnum;
        AIDrawableInit(mBitmapArray,x,y);
    }
    */
    public AIAnime(Bitmap bitmap, float x, float y, AIAnime next) {
        mNextShape = next;
        mBitmapArray = new Bitmap[1];
        mBitmapArray[0] = bitmap.copy(bitmap.getConfig(),true);
        AIDrawableInit(mBitmapArray,x,y);
    }
    public AIAnime(Bitmap[] bitmap, float x, float y) {
        mNextShape = null;
        AIDrawableInit(bitmap,x,y);
    }
    public AIAnime(Bitmap[] bitmap, float x, float y, AIAnime next) {
        mNextShape = next;
        AIDrawableInit(bitmap,x,y);
    }
    public void ExplosionSet(Bitmap[] bitmapArray, int explosionCount) {
        mExplosionArray = bitmapArray;
        mExplosionShape = 0;
        mExplosionTick  = 0;
        mExplosionCount = explosionCount;
    }
    public Bitmap[] CopyBitmapArray(Bitmap[] ba) {
        int length;
        Bitmap baPtr[];
        length = ba.length;
        baPtr = new Bitmap[ba.length];
        for (int i=0; i<length; i++) {
            baPtr[i] = ba[i].copy(ba[i].getConfig(),true);
        }
        return baPtr;
    }

    public void SetXVYV(float xv, float yv) {
        mXV = xv;
        mYV = yv;
    }
    public void Reset(float x, float y, float xv, float yv, float xdest, float ydest) {
        mX = x;
        mY = y;
        mXV = xv;
        mYV = yv;
        mXdest = xdest;
        mYdest = ydest;
        mAlive=1;
        mCurrentShape=0;
    }
    private void AIDrawableInit(Bitmap[] bitmap, float x, float y) {
        mBitmapArray = bitmap;
        mX = mXdest = x;  mY = mYdest = y;
        mXV = 0f;  mYV = 0f; // Must be positive + or - depending of mx >=< mXdest
        mAlive=1;
        mCurrentShape=0;
    }
    private void setNext(AIAnime nextShape) {
        mNextShape = nextShape;
    }
    public void drawDontMove(Canvas canvas, Paint paint) {
        if (mAlive > 0) {
            canvas.drawBitmap(mBitmapArray[mCurrentShape], mX, mY, paint);
        }
    }
    public void draw(Canvas canvas, Paint paint) {
        if (mAlive > 0) {
            canvas.drawBitmap(mBitmapArray[mCurrentShape], mX, mY, paint);
            shapeMove();
        } else if (mAlive < 0) {
            if ( mExplosionArray != null ) {
                canvas.drawBitmap(mExplosionArray[mExplosionShape], mX, mY, paint);
                mExplosionTick++;
                if (mExplosionTick >= mExplosionCount) {
                    mExplosionTick=0;
                    mExplosionShape++;
                    if (mExplosionShape >= mExplosionArray.length) {
                        mExplosionShape = 0;
                    }
                }
            }
            mAlive++;
        }
    }
    public static void DrawArray(Canvas canvas, Paint paint, AIAnime array[]) {
        for (int i=0; i<array.length; i++) array[i].draw(canvas, paint);
    }
    public void shapeMove() {
        if (mAlive > 0) {
            // Adjust shape position
            if (mX != mXdest) { // **** mX *****
                if (mX < mXdest) {
                    mX += mXV;
                    if (mX > mXdest) mX = mXdest;
                } else {
                    mX -= mXV;
                    if (mX < mXdest) {
                        mX = mXdest;
                    }
                }
            } // **** mX ****
            if (mY != mYdest) {
                if (mY < mYdest) {
                    mY += mYV;
                    if (mY > mYdest) mY = mYdest;
                } else {
                    mY -= mYV;
                    if (mY < mYdest) {
                        mY = mYdest;
                    }
                }
            }
        } else if (mAlive < 0) {
            mAlive++;            // Do any explosion stuff
        } else {
            Log.d("WARN","AIShape.Draw called when mAlive==0");
        }
    }

    public void tick() {
        mCurrentShape++;
        if (mCurrentShape >= mBitmapArray.length) mCurrentShape=0;
    }

    public void SetDestination(float x, float y) {
        mXdest = x;
        mYdest = y;
    }
    public void SetDestinationX(float x) {
        mXdest = x;
    }

    public void SetDestDelta(float x, float y) {
        mXdest += x;
        mYdest += y;
    }

    public float GetX() { return mX; }
    public float GetY() { return mY; }
    public float GetDestX() { return mXdest; }
    public float GetDestY() { return mYdest; }
    public void stop()  { mXdest = mX;  mYdest = mY; }
    public float GetMidX() { return mX + GetWidth() / 2; }
    public boolean AtDestX() { return mX == mXdest; }
    public boolean AtDestY() { return mY == mYdest; }

    public boolean AliveBoolean() {
        return mAlive > 0;
    }
    public void SetDead() { mAlive = 0; }
    public void SetDead(int deathClock) { mAlive = deathClock; }

    public int GetWidth() {
        return mBitmapArray[mCurrentShape].getWidth();
    }
    public int GetHeight() {
        return mBitmapArray[mCurrentShape].getHeight();
    }
    public Bitmap GetBitmap() { return mBitmapArray[mCurrentShape]; }

    public void Terminate(AIAnime impacter, Overlap impactCoordinates, AIDebrisArray da) { // Store by what and where we are hit
        Log.d("DEBUG","Terminate this=" + this + " Impacter=" + impacter);
        if (this.mATEnum==AnimeEnum.INVADER_BOTTOM || this.mATEnum==AnimeEnum.INVADER_MIDDLE || this.mATEnum==AnimeEnum.INVADER_TOP) {
            if ( da != null ) {
                da.Assign(GetBitmap(), (int) mX, (int) mY, 0, 0, -1, -1, 0, 2.0f, 2.0f, 0f, 0f);
            }
            SetDead(-20);
        } else {
            SetDead();
        }
    }

    public void Impact(AIAnime impacter, Overlap impactCoordinates) { // Store by what and where we are hit
        Log.d("DEBUG","Impact this=" + this + " Impacter=" + impacter);
        mOverlap = impactCoordinates;
        AIShapeTable.BitmapSubtract( mBitmapArray[0], impactCoordinates.getLeft1(), impactCoordinates.getTop1(),
                impacter.GetBitmap(), impactCoordinates.getLeft2(), impactCoordinates.getTop2()-10,
                impactCoordinates.getWidth(), impactCoordinates.getHeight(), 0 );
        //impacter.SetDead();
    }
    public Overlap OverlapCheck(AIAnime anime2, int step) { // step : skip every skip-th pixel for faster compare
        return AIShapeTable.OverlapCheck(step, (int)mX, (int)mY, mBitmapArray[mCurrentShape],
                (int)anime2.GetX(), (int)anime2.GetY(), anime2.GetBitmap());
    }
    public static Overlap OverlapCheck(AIAnime anime1, boolean terminate1, boolean impact1,
                                       AIAnime anime2, boolean terminate2, boolean impact2, int step) {
        return OverlapCheckDebris(anime1, terminate1, impact1,  anime2, terminate2, impact2, step, null);
    }
    public static Overlap OverlapCheckDebris(AIAnime anime1, boolean terminate1, boolean impact1,
                                       AIAnime anime2, boolean terminate2, boolean impact2, int step, AIDebrisArray da)
    {
        boolean overlapFlag;
        Overlap ocResult;
        if (! anime1.AliveBoolean()) return null;
        if (! anime2.AliveBoolean()) return null;
        // overlapFlag =
        if (step==0) {
            if (da!=null) step=da.GetStep();
        }
        ocResult = AIShapeTable.OverlapCheck(step, (int)anime1.GetX(), (int)anime1.GetY(), anime1.GetBitmap(),
                (int)anime2.GetX(), (int)anime2.GetY(), anime2.GetBitmap());
        if (ocResult != null) {
            Log.d("DEBUG","OverlapCheck anime1=" + anime1 + "  anime2=" + anime2);
            if (impact1) anime1.Impact(anime2,ocResult);
            if (impact2) anime2.Impact(anime1,ocResult.NewFlip());
            if (terminate1) anime1.Terminate(anime2,ocResult,da);
            if (terminate2) anime2.Terminate(anime1,ocResult,da);
        }
        return ocResult;
    }
    public static boolean OverlapCheckArrays(AIAnime animeArray1[], boolean terminate1, boolean impact1,
                                             AIAnime animeArray2[], boolean terminate2, boolean impact2, int step, AIDebrisArray da) {
        int s1 = animeArray1.length;
        int s2 = animeArray2.length;
        Overlap ocResult;
        boolean retcode=false;
        AIAnime a1, a2;
        // Log.d("DEBUG","OverlapCheckArrays: s1=" + s1 + "  s2=" + s2);
        for (int i1=0; i1<s1; i1++) {
            for (int i2=0; i2<s2; i2++) {
                a1 = animeArray1[i1];
                a2 = animeArray2[i2];
                if (a1==null || a2==null) {
                    Log.d("DEBUG","OverlapCheckArrays: array was null a1=" + a1 + "  a2=" + a2);
                }
                if ( (a1!=null) && (a2!=null) && a1.AliveBoolean() && a2.AliveBoolean()) {
                    ocResult = OverlapCheckDebris(a1, terminate1, impact1, a2, terminate2, impact2, step, da);
                    if ( ocResult != null ) retcode = true;
                }
            }
        }
        return false;
    }
    // Probably will change not to use list.
    public boolean OverlapList(AIAnime animeCheck, int step) {
        int listCount = 0;
        Overlap ocResult;
        for (AIAnime animePtr = this; animePtr != null && listCount < mMaxListLength; animePtr = mNextShape, listCount++) {
            ocResult = animePtr.OverlapCheck(animeCheck,step);
            if ( ocResult != null ) {
                animePtr.Impact(animeCheck,ocResult); // Let anime know it was impacted and by who.
                return true;
            }
        }
        return false;
    }

    //(int step, int xleft1, int ytop1, Bitmap image1, int xleft2, int ytop2, Bitmap image2)
}
