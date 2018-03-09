package com.ai.androidinvaders3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Fred6502 on 8/13/2017.
 * RGB, 32=0x20,224=0xe0,64=0x40
 */

public class AIGround {
    private AI3Layout mAI3Layout;
    private Bitmap mBitmap;
    private final int BARRICADE_COLOR = 0xFF20e040;
    private int x, y;
    public AIGround(AI3Layout AI3Layout) {
        mAI3Layout = AI3Layout;
        x = mAI3Layout.mGroundLeft;
        y = mAI3Layout.mGroundTop;
        InitBitmap();
    }
    public void InitBitmap() {
        int width  = mAI3Layout.mGroundRight - mAI3Layout.mGroundLeft + 1;
        int height = mAI3Layout.mGroundBottom - mAI3Layout.mGroundTop + 1;
        Log.d("DEBUG","AIGround width=" + width +" height=" + height);
        mBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(BARRICADE_COLOR);
    }
    public Bitmap GetBitmap() {
        return mBitmap;
    }
    public void Draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitmap,(float)x,(float)y,paint);
    }
}
