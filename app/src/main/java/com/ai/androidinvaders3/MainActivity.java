package com.ai.androidinvaders3;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import static android.view.KeyEvent.KEYCODE_A;
import static android.view.KeyEvent.KEYCODE_DPAD_LEFT;
import static android.view.KeyEvent.KEYCODE_DPAD_RIGHT;

public class MainActivity extends AppCompatActivity  {
    private Bundle mSavedInstanceState;
    private AICanvas mAICanvas;
    private StateMachine mTask=null;
    private Handler mHandler;
    private ViewTreeObserver mVTO;
    private AI3Layout mAI3Layout;
    private int mNoLayout=0;
    private AIWall mWall;
    private AIGround mAIGround;
    private AIShapeTable mAIShapeTable;
    private AIAnimeTable mAIAnimeTable;
    private AIBarricade mAIBarricade;
    private AIPlayer mAIPlayer;
    private AIPlayerMissile mAIPlayerMissile;
    private AIDebrisArray mAIDebrisArray;
    private AIControl mAIControl;
    private AIDrawAll mAIDrawAll;
    private Resources mRSS;
    private int KEYCODE_LEFT_ARROW = 21;
    private int KEYCODE_RIGHT_ARROW = 22;
    private final int mMSperDraw = 20;
    private int someVarA;
    private String someVarB;
    private View.OnClickListener mCanvasClickListener;
    private View.OnTouchListener mCanvasTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG","onCreate savedInstanceState=" + savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* This is for the round mail icon
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        mRSS = getResources();
        mAICanvas = (AICanvas) findViewById(R.id.AICanvas);
        mAICanvas.setLayout(mAI3Layout);

        mCanvasClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("INFO","mCanvasListener onClick(View v=" + v + ")");
                // do something when the button is clicked
            }
        };
        mAICanvas.setOnClickListener(mCanvasClickListener);
        mCanvasTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x,y;
                int got_event;
                got_event = event.getAction();
                if (got_event == MotionEvent.ACTION_DOWN) {
                    x = event.getX();   y = event.getY();
                    Log.d("DEBUG", "Touch coordinates x:" + String.valueOf(x) + " y:" + String.valueOf(y));
                    if ( mAIControl!=null) mAIControl.ActionDown((int)x, (int)y);
                } else if (got_event == MotionEvent.ACTION_UP) {
                    x = event.getX();   y = event.getY();
                    if ( mAIControl!=null) mAIControl.ActionUp((int)x,(int)y);
                }

                return false; //true; if consumed event so nobody else gets it.
            }
        };
        mAICanvas.setOnTouchListener(mCanvasTouchListener);

        mVTO = mAICanvas.getViewTreeObserver(); // Notify initial layout complete
        mVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override public void onGlobalLayout() {
                //here you should stop listening to it... so it will be
                //called only on the initial layout
                mAIShapeTable = new AIShapeTable(mRSS,mAICanvas.getWidth());
                mAIAnimeTable = new AIAnimeTable(mAIShapeTable,mAICanvas.getWidth());
                mAI3Layout = new AI3Layout(mAICanvas.getWidth(),mAICanvas.getHeight(),mAIShapeTable);
                mAICanvas.setLayout(mAI3Layout);
                mAI3Layout.mLayoutComplete = true;
                Bundle wallBundle;
                if ( mSavedInstanceState==null ) wallBundle = null;
                else wallBundle = mSavedInstanceState.getBundle("WallBundle");
                mWall = new AIWall(wallBundle, mAI3Layout, mAIAnimeTable);
                //mAIShapeTable = new AIShapeTable(mRSS,mAICanvas.getWidth());
                mAIGround = new AIGround(mAI3Layout);
                mAIBarricade = new AIBarricade(mAIAnimeTable,mAIShapeTable,mAI3Layout);
                mAIPlayerMissile = new AIPlayerMissile(mAIAnimeTable, 3);
                mAIPlayer = new AIPlayer(mAIAnimeTable, mAIShapeTable, mAI3Layout, mAIPlayerMissile);
                mAIDebrisArray = new AIDebrisArray( 800, mAICanvas.getWidth(), mAICanvas.getHeight(),mAIShapeTable.getScale()+2,mAIShapeTable.getScale()+2);
                mAIControl = new AIControl(mAI3Layout, mAIPlayer, 20);
                mAIDrawAll = new AIDrawAll(mAIAnimeTable, mAIShapeTable, mAI3Layout, mWall, mAIGround, mAIBarricade, mAIPlayer,mAIPlayerMissile, mAIDebrisArray, mAIControl);
                mAICanvas.setDrawAll(mAIDrawAll);
                Log.d("DEBUG","AI3Layout complete: Width:" + mAI3Layout.mWidth +" Height:" + mAI3Layout.mHeight + " NoLayoutCount:" + mNoLayout);
                // Bundle
                /*
                if (mSavedInstanceState != null) {
                    Bundle wallBundle = mSavedInstanceState.getBundle("WallBundle");
                    if ( wallBundle==null ) {
                        Log.d("DEBUG","main: WallBundle==null");
                    } else {
                        Log.d("DEBUG", "main: WallBundle=" + wallBundle);
                        if ( mWall==null ) {
                            Log.d("DEBUG", "mWall==null");
                        } else  mWall.RestoreInstanceState(wallBundle);
                    }
                } else {
                    Log.d("DEBUG", "Main: savedInstanceState=null");
                }
                */
            }
        } );



        Log.d("DEBUG","Width:" + mAICanvas.getWidth() +" Height:" + mAICanvas.getHeight());
        mTask = new StateMachine();
        mHandler = new Handler();
        mHandler.postDelayed(mTask,10);
        someVarA=12345678;
        someVarB="This is only a test. Don't Panic!";
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        someVarA++;
        outState.putInt("someVarA", someVarA);
        outState.putString("someVarB", someVarB);
        outState.putBundle("WallBundle", mWall.SaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("DEBUG","Bundle=" + savedInstanceState);
        someVarA = savedInstanceState.getInt("someVarA");
        someVarB = savedInstanceState.getString("someVarB");
        someVarA++;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class StateMachine implements Runnable {
        public void run() {
            if ( mAI3Layout !=null && mAI3Layout.mLayoutComplete == false ) mNoLayout++;
            else mAICanvas.invalidate();
            mHandler.postDelayed(mTask, mMSperDraw);
            //Log.d("DEBUG","StateMachine mNoLayout=" + mNoLayout);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int unicode;
        // String key = KeyEvent.keyCodeToString(keyCode);
        // //keyCodeToString(keyCode);
        unicode = event.getUnicodeChar(event.getMetaState());
        // Log.d("DEBUG","Down KeyEvent:"+keyCode+" Unicode:"+unicode+"("+(char)unicode+")"); Log.d("DEBUG","KeyCode=" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            Log.d("DEBUG","KeyCode=" + keyCode);
			/*
			 * if (mMediaPlayerFire != null) {
			 * mMediaPlayerFire.setAudioStreamType(AudioManager.STREAM_MUSIC);
			 * mMediaPlayerFire.start(); } else
			 * Log.d("DEBUG","mMediaPlayerFire==null");
			 */
            mAIPlayer.StilesCanYouHearMe_Fire();
        } else if (keyCode == KEYCODE_DPAD_LEFT /*KEYCODE_LEFT_ARROW*/ ) // LEFT ARROW   KeyEvent.KEYCODE_1)
            mAIPlayer.MoveLeft(true);
        else if (keyCode == KEYCODE_DPAD_RIGHT /*KEYCODE_RIGHT_ARROW*/ ) // RIGHT ARROW   KeyEvent.KEYCODE_3)
            mAIPlayer.MoveRight(true);
        else if (keyCode == KEYCODE_A) {
            Log.d("INFO","someVarA"+someVarA+" someVarB"+someVarB);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // String key = KeyEvent.keyCodeToString(keyCode);
        // //keyCodeToString(keyCode);
        if (keyCode == KEYCODE_LEFT_ARROW ) // LEFT ARROW   KeyEvent.KEYCODE_1)
            mAIPlayer.MoveLeft(false); //SetLeftDown(false);
        if (keyCode == KEYCODE_RIGHT_ARROW ) // RIGHT ARROW   KeyEvent.KEYCODE_3)
            mAIPlayer.MoveRight(false); //SetRightDown(false);
        return super.onKeyDown(keyCode, event);
    }
}
