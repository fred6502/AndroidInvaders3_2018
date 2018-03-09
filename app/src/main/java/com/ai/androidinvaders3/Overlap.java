package com.ai.androidinvaders3;

/**
 * Created by Fred6502 on 2/17/2018.
 */

/* Stores overlap = impact coordinates detected in AIShapeTable.
   At first non transparent non black pixel in both shapes overlapping this class is returned.
   Overlap region scanned from left to right top to bottom. Hmmm do I always want to do it that way?
 */
public class Overlap {
    private int mOLeft1, mOTop1, mORight1, mOBottom1;
    private int mOLeft2, mOTop2, mORight2, mOBottom2;
    private int mOWidth, mOHeight; // range of overlap
    private int mOX, mOY; // point of overlap

    public Overlap(int Left1, int Top1, int Right1, int Bottom1,
                                int Left2, int Top2, int Right2, int Bottom2,
                                int Width, int Height,   int X, int Y) {
        mOLeft1 = Left1;
        mOTop1 = Top1;
        mORight1 = Right1;
        mOBottom1 = Bottom1;
        mOLeft2 = Left2;
        mOTop2 = Top2;
        mORight2 = Right2;
        mOBottom2 = Bottom2;
        mOWidth = Width;
        mOHeight = Height; // range of overlap
        mOX = X;
        mOY = Y; // point of overlap=impact
    }

    // Return copy of Overlap with 1 and 2 switched.
    public Overlap NewFlip() {
        Overlap newO;
        newO = new Overlap(mOLeft2, mOTop2, mORight2, mOBottom2,
                           mOLeft1, mOTop1, mORight1, mOBottom1,
                           mOWidth, mOHeight, mOX, mOY);
        return newO;
    }

    public int getLeft1() {
        return mOLeft1;
    }

    public int getTop1() {
        return mOTop1;
    }

    public int getRight1() {
        return mORight1;
    }

    public int getBottom1() {
        return mOBottom1;
    }

    public int getLeft2() {
        return mOLeft2;
    }

    public int getTop2() {
        return mOTop2;
    }

    public int getRight2() {
        return mORight2;
    }

    public int getBottom2() {
        return mOBottom2;
    }

    public int getWidth() {
        return mOWidth;
    }

    public int getHeight() {
        return mOHeight;
    }

    public int getOX() {
        return mOX;
    }

    public int getOY() {
        return mOY;
    }
}
