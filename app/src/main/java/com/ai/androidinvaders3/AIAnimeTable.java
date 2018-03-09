package com.ai.androidinvaders3;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Fred6502 on 12/25/2017.
 */

public class AIAnimeTable {
    private AIShapeTable mST;
    private int mWidth;
    private AIAnime mAlienShotA; // 3-shapes
    private AIAnime mAlienShotB; // "
    private AIAnime mAlienShotC; // "
    private AIAnime mBarricade;
    private AIAnime mBlast;
    private AIAnime mExplosion;
    private AIAnime mInvaderTop;
    private AIAnime mInvaderMiddle;
    private AIAnime mInvaderBottom;
    private AIAnime mPlayer; // Player + Dead + Explosion1 & 2
    private AIAnime mPlayerMissile; // 6 shapes
    private AIAnime mUFO;
    private AIAnime mA[]; //AnimeEnum.NUM_SHAPES.ordinal()];
    private int mCountTick = 0;

    public AIAnimeTable(AIShapeTable st, int width) {
        mST = st;
        mWidth = width;
        float middleX = width / 2;
        float delta = 50.0f;
        // Alien Shots`

        AIShapeTable.DrawableEnum[] stdeListASA = { AIShapeTable.DrawableEnum.ALIEN_SHOT_A0,
                AIShapeTable.DrawableEnum.ALIEN_SHOT_A1, AIShapeTable.DrawableEnum.ALIEN_SHOT_A2 };
        mAlienShotA = new AIAnime(st.GetBitmapScaledCopies(stdeListASA), middleX, delta, null);
        AIShapeTable.DrawableEnum[] stdeListASB = { AIShapeTable.DrawableEnum.ALIEN_SHOT_B0,
                AIShapeTable.DrawableEnum.ALIEN_SHOT_B1, AIShapeTable.DrawableEnum.ALIEN_SHOT_B2 };
        mAlienShotB = new AIAnime(st.GetBitmapScaledCopies(stdeListASB), middleX + delta, delta, mAlienShotA);
        AIShapeTable.DrawableEnum[] stdeListASC = { AIShapeTable.DrawableEnum.ALIEN_SHOT_C0,
                AIShapeTable.DrawableEnum.ALIEN_SHOT_C1, AIShapeTable.DrawableEnum.ALIEN_SHOT_C2 };
        mAlienShotC = new AIAnime(st.GetBitmapScaledCopies(stdeListASC), middleX + 2 * delta, delta, mAlienShotB);
        // Barricade
        mBarricade = new AIAnime(st.GetBitmapScaledCopy(AIShapeTable.DrawableEnum.BARRICADE), middleX, 2 * delta, mAlienShotC);
        // Blast
        AIShapeTable.DrawableEnum[] stdeListBlast = { AIShapeTable.DrawableEnum.BLAST_BLACK,
                AIShapeTable.DrawableEnum.BLAST_WHITE };
        mBlast = new AIAnime(st.GetBitmapScaledCopies(stdeListBlast), middleX + delta, 2 * delta, mBarricade);
        // Explosion
        mExplosion = new AIAnime(st.GetBitmapScaledCopy(AIShapeTable.DrawableEnum.EXPLOSION), middleX + 2 * delta, 2 * delta, mBlast);
        // Invader
        AIShapeTable.DrawableEnum[] stdeListIBot = { AIShapeTable.DrawableEnum.INVADER_BOTTOM0, // Bottom
                AIShapeTable.DrawableEnum.INVADER_BOTTOM1 };
        mInvaderBottom = new AIAnime(st.GetBitmapScaledCopies(stdeListIBot), middleX, 3 * delta, mExplosion);
        AIShapeTable.DrawableEnum[] stdeListIMid = { AIShapeTable.DrawableEnum.INVADER_MIDDLE0, // Middle
                AIShapeTable.DrawableEnum.INVADER_MIDDLE1 };
        mInvaderMiddle = new AIAnime(st.GetBitmapScaledCopies(stdeListIMid), middleX + delta, 3 * delta, mInvaderBottom);
        AIShapeTable.DrawableEnum[] stdeListITop = { AIShapeTable.DrawableEnum.INVADER_TOP0, // Top
                AIShapeTable.DrawableEnum.INVADER_TOP1 };
        mInvaderTop = new AIAnime(st.GetBitmapScaledCopies(stdeListITop), middleX + 2 * delta, 3 * delta, mInvaderMiddle);
        // Player
        AIShapeTable.DrawableEnum[] stdeListPlayer = { AIShapeTable.DrawableEnum.PLAYER, AIShapeTable.DrawableEnum.PLAYER_DEAD,
                AIShapeTable.DrawableEnum.PLAYER_EXPLOSION1, AIShapeTable.DrawableEnum.PLAYER_EXPLOSION2 };
        mPlayer = new AIAnime(st.GetBitmapScaledCopies(stdeListPlayer), middleX, 4 * delta, mInvaderTop);
        // Player Missile
        AIShapeTable.DrawableEnum[] stdeListMissile = { AIShapeTable.DrawableEnum.PLAYER_MISSILE0, AIShapeTable.DrawableEnum.PLAYER_MISSILE1,
                AIShapeTable.DrawableEnum.PLAYER_MISSILE2, AIShapeTable.DrawableEnum.PLAYER_MISSILE3,
                AIShapeTable.DrawableEnum.PLAYER_MISSILE4, AIShapeTable.DrawableEnum.PLAYER_MISSILE5};
        mPlayerMissile = new AIAnime(st.GetBitmapScaledCopies(stdeListMissile), middleX, 5 * delta, mPlayer);
        // UFO
        mUFO = new AIAnime(st.GetBitmapScaledCopy(AIShapeTable.DrawableEnum.UFO), middleX, 6 * delta, mPlayerMissile);
        mA = new AIAnime[]{mAlienShotA, mAlienShotB, mAlienShotC, mBarricade, mBlast, mExplosion, mInvaderBottom, mInvaderMiddle, mInvaderTop, mPlayer, mPlayerMissile, mUFO};
    }

    /*
    public enum AnimeEnum {
        ALIEN_SHOT_A, ALIEN_SHOT_B, ALIEN_SHOT_C, BARRICADE, BLAST, EXPLOSION, INVADER_BOTTOM, INVADER_MIDDLE, INVADER_TOP, PLAYER, PLAYER_MISSILE, UFO;
    };
    */
    public AIShapeTable getST() {
        return mST;
    }

    public int GetWidth(AIAnime.AnimeEnum ae) {
        return mA[ae.ordinal()].GetWidth();
    }
    public int GetHeight(AIAnime.AnimeEnum ae) {
        return mA[ae.ordinal()].GetHeight();
    }

    public void templatesDraw(Canvas canvas, Paint paint) {
        for ( AIAnime.AnimeEnum a : AIAnime.AnimeEnum.values() ) {
            mA[a.ordinal()].drawDontMove(canvas,paint);
        }
        /*
        mAlienShotA.drawDontMove(canvas,paint);
        mAlienShotB.drawDontMove(canvas,paint);
        mAlienShotC.drawDontMove(canvas,paint);
        mBarricade.drawDontMove(canvas,paint);
        mBlast.drawDontMove(canvas,paint);
        mExplosion.drawDontMove(canvas,paint);
        mInvaderTop.drawDontMove(canvas,paint);
        mInvaderMiddle.drawDontMove(canvas,paint);
        mInvaderBottom.drawDontMove(canvas,paint);
        mPlayer.drawDontMove(canvas,paint);
        mPlayerMissile.drawDontMove(canvas,paint);
        mUFO.drawDontMove(canvas,paint);
        */
        mCountTick++;
        if (mCountTick > 20) {
            mCountTick = 0;
            templatesTick();
        }
    }
    public void templatesTick() {
        for ( AIAnime.AnimeEnum a : AIAnime.AnimeEnum.values() ) {
            mA[a.ordinal()].tick();
        }
        /*
        mAlienShotA.tick();
        mAlienShotB.tick();
        mAlienShotC.tick();
        mBarricade.tick();
        mBlast.tick();
        mExplosion.tick();
        mInvaderTop.tick();
        mInvaderMiddle.tick();
        mInvaderBottom.tick();
        mPlayer.tick();
        mPlayerMissile.tick();
        mUFO.tick();
        */
    }

    public AIAnime New(AIAnime.AnimeEnum a, float x, float y, AIAnime next) {
        AIAnime anime = new AIAnime(mA[a.ordinal()], x, y, a, next );
        if (a==AIAnime.AnimeEnum.INVADER_BOTTOM || a==AIAnime.AnimeEnum.INVADER_MIDDLE || a==AIAnime.AnimeEnum.INVADER_TOP) {
            AIShapeTable.DrawableEnum[] stde = new AIShapeTable.DrawableEnum[1];
            stde[0] = AIShapeTable.DrawableEnum.EXPLOSION;
            Bitmap[] explosionArray = mST.GetBitmapScaledCopies(stde);
            anime.ExplosionSet(explosionArray,5);
        }
        return anime;
    }
}
