package com.ai.androidinvaders3;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Fred6502 on 8/5/2017.
 */

public class AIShapeTable {
    private Bitmap mBitmap[];
    private Bitmap mBitmapScaled[];
    private Resources mRss;
    private int mScale;
    // ---- Last Overlap ---- Overlap range normalized to zero.
    private int mOLeft1, mOTop1, mORight1, mOBottom1;
    private int mOLeft2, mOTop2, mORight2, mOBottom2;
    private int mOWidth, mOHeight; // range of overlap
    private int mOX, mOY; // point of overlap

    public AIShapeTable(Resources rss,int width) {
        mRss = rss;
        Matrix matrix = new Matrix();
        // Bitmap bottom_invader = BitmapFactory.decodeResource(mRss,GetDrawableRID(DrawableEnum.INVADER_BOTTOM0));
        // int invader_width = bottom_invader.getWidth();
        int invader_width = getWidth(DrawableEnum.INVADER_BOTTOM0);
        mScale = (width/17) / invader_width;
        Log.d("DEBUG","AIShapeTable: bottom invader width="+width+" invader_width"+invader_width+" scale="+mScale);
        matrix.setScale(mScale,mScale);

        // ASS.U.MEs enum starts at 0 and increments 1 for each enxt enum ordinal.
        mBitmap       = new Bitmap[DrawableEnum.NUM_SHAPES.ordinal()];
        mBitmapScaled = new Bitmap[DrawableEnum.NUM_SHAPES.ordinal()];
        for (DrawableEnum de : DrawableEnum.values()) {
            if (de == DrawableEnum.NUM_SHAPES) continue;
            Bitmap bm = BitmapFactory.decodeResource(mRss,GetDrawableRID(de));
            mBitmap[de.ordinal()] = bm;
            mBitmapScaled[de.ordinal()] = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,false);
        }
    }

    public Bitmap GetBitmap(DrawableEnum de) {
        return GetBitmap(de.ordinal());
    }
    public Bitmap GetBitmap(int de_ordinal) {
        if (de_ordinal < 0 ||  de_ordinal >= DrawableEnum.NUM_SHAPES.ordinal() ) {
            Log.d("ERROR", "AIShapeTable::GetBitmap int bad index");
            System.exit(-1);
        }
        return mBitmap[de_ordinal];
    }

    public Bitmap GetBitmapScaledCopy(DrawableEnum de) {
        Bitmap bitmap;
        bitmap = GetBitmapScaled(de);
        return bitmap.copy(bitmap.getConfig(), true);
    }
    public Bitmap[] GetBitmapScaledCopies(DrawableEnum[] deList) {
        Bitmap[] bmlist = new Bitmap[deList.length];
        for (int i = 0; i < deList.length; i++) {
            bmlist[i] = GetBitmapScaledCopy(deList[i]);
        }
        return bmlist;
    }

    public Bitmap GetBitmapScaled(DrawableEnum de) {
        return GetBitmapScaled(de.ordinal());
    }
    public Bitmap GetBitmapScaled(int de_ordinal) {
        if (de_ordinal < 0 ||  de_ordinal >= DrawableEnum.NUM_SHAPES.ordinal() ) {
            Log.d("ERROR", "AIShapeTable::GetBitmapScaled int bad index");
            System.exit(-1);
        }
        return mBitmapScaled[de_ordinal];
    }
    public int getScale() {
        return mScale;
    }

    public enum DrawableEnum {
        ALIEN_SHOT_A0, ALIEN_SHOT_A1, ALIEN_SHOT_A2, ALIEN_SHOT_B0, ALIEN_SHOT_B1, ALIEN_SHOT_B2, ALIEN_SHOT_C0, ALIEN_SHOT_C1, ALIEN_SHOT_C2, BARRICADE, BLAST_BLACK, BLAST_WHITE, EXPLOSION, INVADER_BOTTOM0, INVADER_BOTTOM1, INVADER_MIDDLE0, INVADER_MIDDLE1, INVADER_TOP0, INVADER_TOP1, PLAYER, PLAYER_DEAD, PLAYER_EXPLOSION1, PLAYER_EXPLOSION2, PLAYER_MISSILE0, PLAYER_MISSILE1, PLAYER_MISSILE2, PLAYER_MISSILE3, PLAYER_MISSILE4, PLAYER_MISSILE5, UFO, NUM_SHAPES
    };

    public int GetDrawableRID(DrawableEnum drawableEnum) {
        switch (drawableEnum) {
            case ALIEN_SHOT_A0:
                return R.drawable.alien_shot_a0;
            case ALIEN_SHOT_A1:
                return R.drawable.alien_shot_a1;
            case ALIEN_SHOT_A2:
                return R.drawable.alien_shot_a2;
            case ALIEN_SHOT_B0:
                return R.drawable.alien_shot_b0;
            case ALIEN_SHOT_B1:
                return R.drawable.alien_shot_b1;
            case ALIEN_SHOT_B2:
                return R.drawable.alien_shot_b2;
            case ALIEN_SHOT_C0:
                return R.drawable.alien_shot_c0;
            case ALIEN_SHOT_C1:
                return R.drawable.alien_shot_c1;
            case ALIEN_SHOT_C2:
                return R.drawable.alien_shot_c2;
            case BARRICADE:
                return R.drawable.barricade;
            case BLAST_BLACK:
                return R.drawable.blast_black;
            case BLAST_WHITE:
                return R.drawable.blast_white;
            case EXPLOSION:
                return R.drawable.explosion;
            case INVADER_BOTTOM0:
                return R.drawable.invader_bottom0;
            case INVADER_BOTTOM1:
                return R.drawable.invader_bottom1;
            case INVADER_MIDDLE0:
                return R.drawable.invader_middle0;
            case INVADER_MIDDLE1:
                return R.drawable.invader_middle1;
            case INVADER_TOP0:
                return R.drawable.invader_top0;
            case INVADER_TOP1:
                return R.drawable.invader_top1;
            case PLAYER:
                return R.drawable.player;
            case PLAYER_DEAD:
                return R.drawable.player_dead;
            case PLAYER_EXPLOSION1:
                return R.drawable.player_explosion1;
            case PLAYER_EXPLOSION2:
                return R.drawable.player_explosion2;
            case PLAYER_MISSILE0:
                return R.drawable.player_missile0;
            case PLAYER_MISSILE1:
                return R.drawable.player_missile1;
            case PLAYER_MISSILE2:
                return R.drawable.player_missile2;
            case PLAYER_MISSILE3:
                return R.drawable.player_missile3;
            case PLAYER_MISSILE4:
                return R.drawable.player_missile5;
            case PLAYER_MISSILE5:
                return R.drawable.player_missile5;
            case UFO:
                return R.drawable.ufo;
            default:
                Log.d("ERROR", "GetDrawable Called with invalid enum="
                        + drawableEnum.ordinal() + "=" + drawableEnum.toString());
                return 0;
        }
    }
    public int getWidth(DrawableEnum drawableEnum) {
        return BitmapFactory.decodeResource(mRss,GetDrawableRID(DrawableEnum.INVADER_BOTTOM0)).getWidth();
    }
    public int getHeight(DrawableEnum drawableEnum) {
        return BitmapFactory.decodeResource(mRss,GetDrawableRID(DrawableEnum.INVADER_BOTTOM0)).getHeight();
    }
    public int getWidthScaled(DrawableEnum drawableEnum) {
        return mBitmapScaled[drawableEnum.ordinal()].getWidth();
        //return BitmapFactory.decodeResource(mRss,GetDrawableRID(DrawableEnum.INVADER_BOTTOM0)).getWidth() * mScale;
    }
    public int getHeightScaled(DrawableEnum drawableEnum) {
        return mBitmapScaled[drawableEnum.ordinal()].getHeight();
        //return BitmapFactory.decodeResource(mRss,GetDrawableRID(DrawableEnum.INVADER_BOTTOM0)).getHeight() * mScale;
    }

    public void SetOverlapPoint(int Left1, int Top1, int Right1, int Bottom1,
            int Left2, int Top2, int Right2, int Bottom2,
            int Width, int Height,   int X, int Y) {
        mOLeft1=Left1;  mOTop1=Top1;  mORight1=Right1;  mOBottom1=Bottom1;
        mOLeft2=Left2;  mOTop2=Top2;  mORight2=Right2;  mOBottom2=Bottom2;
        mOWidth=Width;  mOHeight=Height; // range of overlap
        mOX=X;  mOY=Y; // point of overlap=impact

    } // point of overlap

    /* If part of image1 which has non-zero alpha and non-zero RorGorB overlaps a pixel in image2 with nonzero A and nonzero RorGorB.
     */
    public static Overlap OverlapCheck(int step, int xleft1, int ytop1, Bitmap image1, int xleft2, int ytop2, Bitmap image2) {
        int xright1, ybottom1, xright2, ybottom2;
        int xwidth, yheight;
        int i, j;
        Overlap ocResult;
        xright1  = xleft1 + image1.getWidth() - 1;
        if (xleft2 > xright1) return null; // false; // Horizontally no overlap
        xright2  = xleft2 + image2.getWidth()  - 1;
        if (xleft1 > xright2) return null; //false; // Horizontally no overlap
        ybottom1 = ytop1 + image1.getHeight() - 1;
        if (ytop2 > ybottom1) return null; //false; // Vertically no overlap
        ybottom2 = ytop2  + image2.getHeight() - 1;
        if (ytop1 > ybottom2) return null; //false; // Vertically no overlap
        Log.d("DEBUG","BOX Overlap("+xleft1+","+ytop1+","+ xright1 +","+ybottom1+") xleft2="+xleft2+" ytop2="+ytop2);
        // Normailze coordintates so greatest left and greatest top are 0.
        if (xleft1 > xleft2) {
            xleft2  -= xleft1;
            xright2 -= xleft1;
            xright1 -= xleft1;
            xleft1 = 0;
        } else { // xleft2 > xleft1
            xleft1  -= xleft2;
            xright1 -= xleft2;
            xright2 -= xleft2;
            xleft2 = 0;
        }
        if (xright1 < xright2) xwidth = xright1+1; else xwidth = xright2+1; // Overlap is smaller of the two
        if (ytop1 > ytop2) {
            ytop2    -= ytop1;
            ybottom2 -= ytop1;
            ybottom1 -= ytop1;
            ytop1 = 0;
        } else { // ytop2 > ytop1
            ytop1    -= ytop2;
            ybottom1 -= ytop2;
            ybottom2 -= ytop2;
            ytop2 = 0;
        }
        if (ybottom1 < ybottom2) yheight = ybottom1+1; else yheight = ybottom2+1; // Overlap is smaller of the two

        int pixel1, pixel2;
        int xLeftDelta, yTopDelta;
        xLeftDelta = (xleft1 < 0) ? xleft1 : xleft2;
        yTopDelta  = (ytop1  < 0) ? ytop1  : ytop2;
        // Log.d("DEBUG", "xLeftDelta=" + xLeftDelta + "yTopDelta" + yTopDelta);
        for (i=0; i<xwidth; i+=step) {
            for (j=0; j<yheight; j+=step) {
                pixel1 = image1.getPixel(i - xleft1,j - ytop1);
                if ( Color.alpha(pixel1) != 0 ) { // If ALPHA==0 doesn't matter what color it is.. if any.
                    if ( (pixel1 & 0x00ffffff) != 0 ) { // Non transparent non black pixel
                        pixel2 = image2.getPixel(i - xleft2, j - ytop2);
                        if ( Color.alpha(pixel2) != 0 ) {
                            if ( (pixel2 & 0x00ffffff) != 0 ) { // Non transparent non black pixel
                                // **** PIXEL OVERLAP of non black non alpha==0 pixels ****
                                Log.d("DEBUG","OVERLAP - i=" + i + " j=" + j);
                                ocResult = new Overlap(xleft1, ytop1, xright1, ybottom1,   xleft2, ytop2, xright2, ybottom2,
                                                     xwidth, yheight,  i, j);
                                return ocResult; // **** BANG !!? ****
                            }
                        }
                    }
                }
            }
        }
        return null; //false; // No pixel overlap = no impact.
    }

    // Subtract overlapping pixels of bitmap2 from bitmap1
    public static int BitmapSubtract(Bitmap bm1, int x1, int y1, Bitmap bm2, int x2, int y2, int width, int height, int delColor) {
        int pixel2;
        int pixelCount=0;
        int bm1_width  = bm1.getWidth();
        int bm1_height = bm1.getHeight();
        int bm2_width  = bm2.getWidth();
        int bm2_height = bm2.getHeight();
        Log.d("DEBUG", "BitmapSubytact x1:"+x1+" y1:"+y1+" w1:"+bm1.getWidth()+" h1:"+bm1.getHeight()+
                " x2:"+x2+" y2:"+y2+" w2:"+bm2.getWidth()+" h2:"+bm2.getHeight()+" width:"+width+" height:"+height);
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++ ) {
                int px = i-x2, py = j-y2;
                if ( px <0 || px >= bm2_width ) {
                    Log.d("DEBUG", "px="+px+" <0 || >=bm2_width="+bm2_width);
                    continue;
                }
                if ( py < 0 || py >= bm2_height ) {
                    Log.d("DEBUG", "py="+py+" <0 || >=bm2_height"+bm2_height);
                    continue;
                }
                pixel2 = bm2.getPixel(px,py);
                px = i-x1;   py = j-y1;
                if ( px <0 || px >= bm1_width ) {
                    Log.d("DEBUG", "px="+px+" <0 || >=bm1_width="+bm1_width);
                    continue;
                }
                if ( py < 0 || py >= bm1_height ) {
                    Log.d("DEBUG", "py="+py+" <0 || >=bm1_height"+bm1_height);
                    continue;
                }

                if (Color.alpha(pixel2) != 0 && ((pixel2 & 0x00ffffff)!= 0) ) {
                    bm1.setPixel(px,py, delColor);
                    pixelCount++;
                }
            }
        }
        return pixelCount;
    }
}
