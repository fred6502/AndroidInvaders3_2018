package com.ai.androidinvaders3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Fred6502 on 2/28/2018.
 */

public class AIDebrisArray {
    private AIDebris[] mAIDebris;
    int mNumAlive; // [0-mNumAlive) are alive. When one ends it is swapped with mNumAlive-1. Which is then decremented.
    int mStepDefault; // step through bitmap by how many pixel at a time.
    int mSizeDefault; // When drawing draw squares of how many pixels on a size.
    AIDebrisArray(int maxDebris, int width, int height, int stepDefault, int sizeDefault) {
        mAIDebris = new AIDebris[maxDebris];
        AIDebris.SetWidthHeight(width,height);
        for (int i=0; i<mAIDebris.length; i++) {
            mAIDebris[i] = new AIDebris();
        }
        mNumAlive = 0;
        mStepDefault = stepDefault;
        mSizeDefault = sizeDefault;
        AIDebris.SetSize(mSizeDefault);
    }
    void Draw(Canvas canvas, Paint paint) {
        AIDebris ptr;
        AIDebris.SetColorLast(0);
        AIDebris.SetSize(mSizeDefault); // Could be more than one Debris Array so reset at each Draw Call.
        for (int i=0; i<mNumAlive; i++) {
            if ( mAIDebris[i].Draw(canvas,paint) ) {
                mNumAlive--;
                if (i<mNumAlive) { // If last ALIVE don't need to swap.
                    // ELSE swap now dead Debris with dead Debris after last on list.
                    ptr = mAIDebris[i];
                    mAIDebris[i] = mAIDebris[mNumAlive];
                    mAIDebris[mNumAlive] = ptr;
                    // Swapped Debris misses one turn to move... BIG DEAL.
                }
            }
        }
    }
    /* Assign fragments from bitmap located on the Canvas.
     * bitmap - Debris only assigned if there is a pixel.
     * bmX, bmmY location of bitmap in the Canvas.
     * x1, y1, width, height - area within bitmap to explode.
     * sx, sy - means speedx, speedy. Speed proportional to distance from explosion region. Fastest at perimeter.
     * step - step through bit map. Maybe only check a fraction of pixels for explosion.
     * offx, offy - Offset speed added to sx, sy. Could be used to catch some momentum from missile.
     *
     */
    int Assign(Bitmap bitmap, int bmX, int bmY, int x1, int y1, int width, int height, int stepParam, float sx, float sy, float offx, float offy) {
        if (width==-1)  width =bitmap.getWidth()-x1;
        if (height==-1) height=bitmap.getHeight()-y1;
        Log.d("DEBUG","Assign bmX:"+bmX+ "bmY"+bmY+" x1"+x1+" y1"+y1+" width"+width+" height"+height+" stepParam"+stepParam+
                " sx"+sx+" sy"+sy+" offx"+offx+" offy"+offy);
        int   numAssigned = 0;
        int   xCenter = x1 + width / 2;
        int   yCenter = y1 + height / 2;
        int   pixel;
        float xMultiplier = sx / ((float)width / 2f);
        float yMultiplier = sy / ((float)height / 2f);
        float debrisXV, debrisYV;
        int step = (stepParam==0) ? mStepDefault : stepParam;

        AIDebris debris;
        for (int i=x1; i<(x1+width); i+=step) {
            for (int j=y1; j<(y1+height); j+=step) {
                pixel = bitmap.getPixel(i, j);
                if (Color.alpha(pixel) != 0) {
                    if ((pixel & 0x00ffffff) != 0) {
                        debris = GetDebris();
                        if (debris==null) {
                            Log.d("DEBUG","AIDebrisArray no more debris. Assigned:"+numAssigned+" debris");
                            return numAssigned;
                        }
                        debrisXV = (xMultiplier * (i - xCenter))  +  offx;
                        debrisYV = (yMultiplier * (j - yCenter))  +  offy;
                        debris.Reset(bmX+i,bmY+j,debrisXV,debrisYV,0xffffff44 /*Color.RED*/,200,1);
                        numAssigned++;
                    }
                }
            }
        }
        Log.d("DEBUG","AIDebrisArray assigned: "+numAssigned+" Debris");
        return numAssigned;
    }
    AIDebris GetDebris() {
        if (mNumAlive < mAIDebris.length) {
            AIDebris debris = mAIDebris[mNumAlive];
            mNumAlive++;
            return debris;
        }
        return null;
    }

    int GetStep() {
        return mStepDefault; // step through bitmap by how many pixel at a time.
    }
    int GetSize() {
        return mSizeDefault; // When drawing draw squares of how many pixels on a size.
    }


}
