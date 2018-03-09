package com.ai.androidinvaders3;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Fred6502 on 1/19/2018.
 */

public class AIPlayerMissile {
    private AIAnime mMissile[]; // 1 or more missiles.
    private float mMISSILE_YV;
    private int mMissileWidth;

    AIPlayerMissile(AIAnimeTable at, int num_missiles) {
        AIAnime aiPTR = null;
        int missileIndex = num_missiles;
        mMissile = new AIAnime[num_missiles];
        while( (missileIndex--) > 0 ) {
            mMissile[missileIndex] = aiPTR = at.New(AIAnime.AnimeEnum.PLAYER_MISSILE, -1f, -1f, aiPTR);
            aiPTR.SetDead();
        }
        mMissileWidth = at.GetWidth(AIAnime.AnimeEnum.PLAYER_MISSILE);
        mMISSILE_YV = 3.0f;
    }

    public boolean StilesCanYouHearMeFire(float x, float y) {
        for (int i=0; i<mMissile.length; i++) {
            if ( ! mMissile[i].AliveBoolean() ) {
                mMissile[i].Reset(x, y, 0, mMISSILE_YV, x, 0f);
                return true;
            }
        }
        return false; // No missiles to launch.
    }

    public void Draw(Canvas canvas, Paint paint, boolean tick) {
        AIAnime pmptr;
        for (int i=0; i<mMissile.length; i++) {
            pmptr = mMissile[i];
            if ( pmptr != null ) {
                if (pmptr.AliveBoolean()) {
                    pmptr.draw(canvas, paint);
                    if (tick) pmptr.tick();
                    if (pmptr.AtDestY()) {
                        pmptr.SetDead();
                    }
                }
            }
        }
    }
    public int GetWidth() {
        return mMissileWidth;
    }
    public AIAnime[] GetAIAnimeArray() {
        return mMissile; // Used for impact detection (overlap)
    }
}
