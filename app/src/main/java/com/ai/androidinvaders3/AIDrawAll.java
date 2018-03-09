package com.ai.androidinvaders3;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Fred6502 on 8/6/2017.
 */

public class AIDrawAll {
    AIShapeTable    mAIST;
    AI3Layout       mAI3Layout;
    AIWall          mWall;
    AIGround        mAIGround;
    AIBarricade     mAIBarricade;
    AIPlayer        mAIPlayer;
    AIPlayerMissile mAIPlayerMissile;
    AIAnimeTable    mAIDT;
    AIDebrisArray   mAIDebrisArray;
    AIControl       mAIControl;
    final int mDrawsPerTick=20;
    int       mDrawCount=0;


    public AIDrawAll(AIAnimeTable dt, AIShapeTable st, AI3Layout ailayout, AIWall wall, AIGround aiground, AIBarricade barricade,
                     AIPlayer player, AIPlayerMissile pmissile, AIDebrisArray debrisArray, AIControl control) {
        mAIDT = dt;
        mAIST = st;
        mAI3Layout = ailayout;
        mWall = wall;
        mAIGround = aiground;
        mAIBarricade = barricade;
        mAIPlayer = player;
        mAIPlayerMissile = pmissile;
        mAIDebrisArray = debrisArray;
        mAIControl = control;
    }

    private boolean Tick() {
        mDrawCount++;
        if (mDrawCount >= mDrawsPerTick) {
            mDrawCount = 0;
            return true;
        }
        return false;
    }

    public void Draw(Canvas canvas,Paint paint) {
        boolean tick = Tick();
        //ShapeTableTest(canvas,paint);
        //mAIDT.templatesDraw(canvas,paint);
        mWall.Draw(canvas,paint,tick);
        mAIGround.Draw(canvas,paint);
        mAIBarricade.Draw(canvas,paint);
        mAIPlayer.Draw(canvas,paint);
        mAIPlayerMissile.Draw(canvas,paint,true /*tick*/ );
        mAIDebrisArray.Draw(canvas,paint);
        mAIControl.Draw(canvas,paint);
        AIAnime.OverlapCheckArrays(mWall.GetAIAnimeArray(),true, false, mAIPlayerMissile.GetAIAnimeArray(),true, false, 0, mAIDebrisArray); // Impact Check
        AIAnime.OverlapCheckArrays(mAIBarricade.GetAIAnimeArray(), false, true, mAIPlayerMissile.GetAIAnimeArray(), true, false, 1, null);
    }

    public void ShapeTableTest(Canvas canvas,Paint paint) {
        for (AIShapeTable.DrawableEnum de : AIShapeTable.DrawableEnum.values() ) {
            if (de == AIShapeTable.DrawableEnum.NUM_SHAPES) continue;
            int x = de.ordinal() * 30;
            int y = de.ordinal() * 30;
            canvas.drawBitmap(mAIST.GetBitmapScaled(de),x,y,paint);
        }
    }
}
