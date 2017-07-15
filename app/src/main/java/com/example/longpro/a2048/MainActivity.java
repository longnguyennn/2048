package com.example.longpro.a2048;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;


/**
 * Created by longpro on 6/17/17.
 */

public class MainActivity extends AppCompatActivity {
    private Game game;
    private GestureDetectorCompat mDetector;
    private View.OnTouchListener mListener;
    public static Context AppContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppContext = getApplicationContext();
//        game = new Game();
//        mDetector = new GestureDetectorCompat(this, new MyGestureDetector());
//        mListener = new View.OnTouchListener() {
//            View game_container = findViewById(R.id.game_container);
//            public boolean onTouch(View v, MotionEvent event) {
//                game_container.onTouchEvent(event);
//                mDetector.onTouchEvent(event);
//                return true;
//            }
//        };
//        findViewById(R.id.game_container).setOnTouchListener(mListener);


//
//        final RelativeLayout game_container = (RelativeLayout) findViewById(R.id.game_container);
//
//        game_container.post(new Runnable() {
//            @Override
//            public void run() {
//                int gcDimension = game_container.getHeight();
//                int TileMargin = gcDimension / 35;
//                int TileDimension = (gcDimension - (5 * TileMargin)) / 4;
//
//                RelativeLayout.LayoutParams TileParams = new RelativeLayout.LayoutParams(TileDimension,TileDimension);
//                TileParams.setMargins(TileMargin, TileMargin, TileMargin, TileMargin);
//
//
//                TextView tv = new TextView(context);
//                tv.setBackgroundResource(R.drawable.component);
//                game_container.addView(tv, TileParams);
//
//            }
//        });









    }


    class Game {
        Board board;
        TextView[][] tvArray = new TextView[4][4];

        public Game() {
            board = new Board();
//            tvArray[0][0] = (TextView) findViewById(R.id.tv00);
//            tvArray[0][1] = (TextView) findViewById(R.id.tv01);
//            tvArray[0][2] = (TextView) findViewById(R.id.tv02);
//            tvArray[0][3] = (TextView) findViewById(R.id.tv03);
//            tvArray[1][0] = (TextView) findViewById(R.id.tv10);
//            tvArray[1][1] = (TextView) findViewById(R.id.tv11);
//            tvArray[1][2] = (TextView) findViewById(R.id.tv12);
//            tvArray[1][3] = (TextView) findViewById(R.id.tv13);
//            tvArray[2][0] = (TextView) findViewById(R.id.tv20);
//            tvArray[2][1] = (TextView) findViewById(R.id.tv21);
//            tvArray[2][2] = (TextView) findViewById(R.id.tv22);
//            tvArray[2][3] = (TextView) findViewById(R.id.tv23);
//            tvArray[3][0] = (TextView) findViewById(R.id.tv30);
//            tvArray[3][1] = (TextView) findViewById(R.id.tv31);
//            tvArray[3][2] = (TextView) findViewById(R.id.tv32);
//            tvArray[3][3] = (TextView) findViewById(R.id.tv33);
//            renderBoard();
        }




        public void swipeHandler(MotionEvent event1, MotionEvent event2) {
            String DEBUG_TAG = "Array";
            int[] differences = new int[2];

            float x_difference = Math.abs(event1.getX() - event2.getX());
            float y_difference = Math.abs(event1.getY() - event2.getY());
            if (y_difference > x_difference) {
                if (event1.getY() > event2.getY()) {
                    board.swipeUp();
                }
                else {
                    board.swipeDown();

                }
            }
            else {
                if (event1.getX() > event2.getX()) {
                    board.swipeLeft();
                }
                else {
                    board.swipeRight();

                }
            }

            if (board.hasChanged) {
                board.setRandomTile();
//                renderBoard();
            }
//             check trackChange's functionality
            for (int a = 0; a < board.trackChanges.size(); a = a + 1) {
                Log.d("TRACK CHANGE SIZE", Integer.toString(board.trackChanges.size()));
                Log.d("TRACK CHANGES: ", Arrays.toString(board.trackChanges.get(a)));
            }

            board.trackChanges.clear();
            board.hasChanged = false;


            // Long Nguyen - this should work
            for (int a = 0; a < 4; a = a + 1) {
                Log.d(DEBUG_TAG, Arrays.toString(board.array[a]));
            }

        }

//        private void renderBoard() {
//            ObjectAnimator animation = ObjectAnimator.ofInt(((ShapeDrawable) tvArray[0][0].getBackground()).getPaint(), "color", 0xb8ad9e);
//            animation.setDuration(175);
//            animation.start();
//        }

    }


//
//    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
//        private static final String DEBUG_TAG = "Gestures";
//
//        @Override
//        public boolean onDown(MotionEvent event) {
//            scroll = false;
//            return true;
//        }
//
//        @Override
//        public boolean onScroll(MotionEvent event1, MotionEvent event2,
//                                float velocityX, float velocityY) {
//            if (scroll == false) {
////                 need condition here -- not swipe when distance < threshold
//
//
//
//                game.swipeHandler(event1, event2);
//                scroll = true;
//            }
//            return true;
//        }
//
//    }

}


