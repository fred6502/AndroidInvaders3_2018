package com.ai.androidinvaders3;

import android.util.Log;

/**
 * Created by Fred6502 on 8/3/2017.
 */

public class AI3Layout {
    Boolean mLayoutComplete = Boolean.FALSE;
    int mWidth = 0;
    int mHeight = 0;
    int mAlienPixelsWide;
    int mScale;
    // WALL OF SPACE INVADERS
    final int mALIENS_WIDE = 11; // Set aliens size to fit across screen
    final int mALIENS_TOP = 1;
    final int mALIENS_MIDDLE = 2;
    final int mALIENS_BOTTOM = 2;
    final int mWallLeft; //  = 0 + 5 * mScale;
    final int mWallRight; //  = mWidth - mWallLeft;
    final int mWallTop = mScale * 11;
    int mWallDeltaX, mWallDeltaY; // Spacing between space invaders

    // GROUND
    int mGroundLeft, mGroundRight, mGroundTop, mGroundBottom;
    // PLAYER
    int mPlayerY, mPlayerHeight, mPlayerWidth;
    int mPlayerLeft, mPlayerRight;
    float mPlayerSpeed;
    // BARRICADE
    int mBarricadeWidth, mBarricadeHeight;
    final int mBARRICADES_WIDE = 4; // Set Barricade size to fit across screen
    int mBarricadeX[];
    int mBarricadeY;
    // CONTROL
    int mControlXLeft;
    int mControlXRight;
    int mControlYFire;
    int mControlYMove;
    int mControlRadius;
    int mControlRadiusSquared;

    AI3Layout(int width, int height, AIShapeTable st) {
        mWidth = width;
        mHeight = height;
        mAlienPixelsWide = st.getWidth(AIShapeTable.DrawableEnum.INVADER_BOTTOM0);
        if (width==0 || height==0) {
            Log.d("ERROR","width=" + width + " or height=" + height + " not set");
            System.exit(-1);
        }
        // GROUND
        mScale = st.getScale();
        mWallLeft = 0 + 5 * mScale;
        mWallRight = mWidth - mWallLeft;
        mWallDeltaX = (int)(st.getWidthScaled(AIShapeTable.DrawableEnum.INVADER_BOTTOM0)  * 1.5);
        mWallDeltaY = (int)(st.getHeightScaled(AIShapeTable.DrawableEnum.INVADER_BOTTOM0) * 1.5);
        mGroundLeft = 0; mGroundRight = mWidth - 1;
        mGroundBottom = (int)height - (int)(st.getHeightScaled(AIShapeTable.DrawableEnum.PLAYER) * 1.1f); //    (mHeight * 0.977);
        mGroundTop = mGroundBottom - (int)(mHeight * 0.01);
        // PLAYER
        mPlayerHeight = (int)(st.getHeightScaled(AIShapeTable.DrawableEnum.PLAYER));
        mPlayerWidth  = (int)(st.getWidthScaled(AIShapeTable.DrawableEnum.PLAYER));

        mPlayerY = mGroundTop - (int)(mPlayerHeight * 1.05);
        mPlayerLeft=0; mPlayerRight=width-mPlayerWidth;
        mPlayerSpeed = 2.0f;
        // BARRICADES
        mBarricadeX = new int[mBARRICADES_WIDE];
        mBarricadeWidth  = st.getWidthScaled(AIShapeTable.DrawableEnum.BARRICADE); //barricade_pixels_wide;
        mBarricadeHeight = st.getHeightScaled(AIShapeTable.DrawableEnum.BARRICADE); //barricade_pixels_wide;

        int screen_width_minus_barricade = mWidth - mBarricadeWidth;
        int barricade_delta = screen_width_minus_barricade / (mBARRICADES_WIDE+1);
        for (int i=0; i<mBARRICADES_WIDE; i++)
            mBarricadeX[i] = (i+1) * barricade_delta;
        mBarricadeY = mGroundTop - mBarricadeHeight - (int)(mPlayerHeight * 1.5);
        // Control
        mControlXLeft = 0;
        mControlXRight = mWidth - 1;
        mControlYFire = mHeight / 2;
        mControlYMove = (mHeight * 3) / 4;
        mControlRadius = mWidth / 10;
        mControlRadiusSquared = mControlRadius ^ 2;

    }
}
