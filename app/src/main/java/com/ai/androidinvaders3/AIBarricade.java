package com.ai.androidinvaders3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Fred6502 on 8/14/2017.
 */

public class AIBarricade  {
    private AIShapeTable mST;
    private AIAnimeTable mAIAT;
    private AI3Layout mLayout;
    private AIAnime mAnime[];
    //private Bitmap mBitmap[];
    //Bitmap mBarricade[1];
    public AIBarricade(AIAnimeTable animeTable, AIShapeTable st, AI3Layout AI3Layout) {
        mAIAT = animeTable;
        mST = st;
        mLayout = AI3Layout;
        //mBitmap = new Bitmap[mLayout.mBARRICADES_WIDE];
        mAnime  = new AIAnime[mLayout.mBARRICADES_WIDE];

        Init();
    }

    public void Init() {
        for (int i=0; i<mLayout.mBARRICADES_WIDE; i++) {
            //mBitmap[i] = mST.GetBitmapScaledCopy(AIShapeTable.DrawableEnum.BARRICADE);
            mAnime[i] =  mAIAT.New(AIAnime.AnimeEnum.BARRICADE,(int)mLayout.mBarricadeX[i],(int)mLayout.mBarricadeY,null);
        }
    }

    public AIAnime[] GetAIAnimeArray() {
        return mAnime;
    }

    public void Draw(Canvas canvas, Paint paint) {
        AIAnime.DrawArray(canvas, paint, mAnime);
    }
}
