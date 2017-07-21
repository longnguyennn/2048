package com.example.longpro.a2048;

import android.animation.AnimatorSet;
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

import android.view.ViewManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;


/**
 * Created by longpro on 6/17/17.
 */

public class MainActivity extends AppCompatActivity {
    private GestureDetectorCompat mDetector;
    View.OnTouchListener mListener;
    private boolean scroll = false;
    private Tile[][] tileArray;
    private Context context;
    private Game game;
    private int tileMargin;
    private int tileDimension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tileArray = new Tile[4][4];
        context = this;
        final RelativeLayout game_container = (RelativeLayout) findViewById(R.id.game_container);
        mDetector = new GestureDetectorCompat(context, new MyGestureDetector());
        mListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                game_container.onTouchEvent(event);
                mDetector.onTouchEvent(event);
                return true;
            }
        };
        game_container.setOnTouchListener(mListener);

        game_container.post(new Runnable() {
            @Override
            public void run() {
                int gcDimension = game_container.getHeight();
                int gcMargin = gcDimension / 30;
                LinearLayout.LayoutParams gcParams = (LinearLayout.LayoutParams) game_container.getLayoutParams();
                gcParams.setMargins(gcMargin, gcMargin, gcMargin, gcMargin);
                int actualDimension = gcDimension - 2 * gcMargin;
                tileMargin = actualDimension / 35;
                int btwMargin = tileMargin / 2;
                tileDimension = (actualDimension - (5 * tileMargin)) / 4;

                for (int i = 0; i < 4; i = i + 1) {
                    if ( i == 0 ) {
                        for (int j = 0; j < 4; j = j + 1) {
                            RelativeLayout.LayoutParams TileParams =
                                    new RelativeLayout.LayoutParams(tileDimension, tileDimension);
                            int position = 10 * i + j;
                            Tile tile = new Tile(context, position, 0);
                            // FIXME: 7/17/17 - don't need setBackgroundResource here
//                            tile.setBackgroundResource(R.drawable.component);
                            tile.setId(Tile.generateViewId());
                            if ( j == 0 ) {
                                TileParams.setMargins(tileMargin, tileMargin, btwMargin, btwMargin);
                            }

                            else {
                                TileParams.setMargins(btwMargin, tileMargin, btwMargin, btwMargin);
                                TileParams.addRule(RelativeLayout.RIGHT_OF, tileArray[i][j - 1].getId());
                            }
                            game_container.addView(tile, TileParams);
                            tileArray[i][j] = tile;
                        }
                    }

                    else {
                        for (int j = 0; j < 4; j = j + 1) {
                            RelativeLayout.LayoutParams TileParams =
                                    new RelativeLayout.LayoutParams(tileDimension, tileDimension);
                            int position = 10 * i + j;
                            Tile tile = new Tile(context, position, 0);
                            tile.setId(Tile.generateViewId());
                            if ( j == 0 ) {
                                TileParams.setMargins(tileMargin, btwMargin, btwMargin, btwMargin);
                                TileParams.addRule(RelativeLayout.BELOW, tileArray[i - 1][j].getId());
                            }
                            else {
                                TileParams.setMargins(btwMargin, btwMargin, btwMargin, btwMargin);
                                TileParams.addRule(RelativeLayout.RIGHT_OF, tileArray[i][j - 1].getId());
                                TileParams.addRule(RelativeLayout.BELOW, tileArray[i - 1][j].getId());
                            }
                            game_container.addView(tile, TileParams);
                            tileArray[i][j] = tile;
                        }
                    }
                }
                game = new Game(game_container);
            }
        });
    }














    private class Game {
        private RelativeLayout gameContainer;
        private Animator animator;
        private boolean hasChanged;
        // array used for the game logic (use tileArray for view animation)
        private int[][] valueArray;

        private Game(RelativeLayout gameContainer) {
            this.gameContainer = gameContainer;
            animator = new Animator();
            this.valueArray = new int[4][4];
            for (int counter = 0; counter < 2; counter = counter + 1) {
                int i = new Random().nextInt(4);
                int j = new Random().nextInt(4);
                int tileValue = tileArray[i][j].getValue();
                while (tileValue != 0) {
                    i = new Random().nextInt(4);
                    j = new Random().nextInt(4);
                    tileValue = tileArray[i][j].getValue();
                }
                tileArray[i][j].setValue(2);
                this.valueArray[i][j] = 2;
            }

        }

        // use runtime value from valueArray
        private void setRandomTile() {
            int i = new Random().nextInt(4);
            int j = new Random().nextInt(4);
            int tileValue = valueArray[i][j];
            while (tileValue != 0) {
                i = new Random().nextInt(4);
                j = new Random().nextInt(4);
                tileValue = valueArray[i][j];
            }
//            set tile value in setNewTile
            animator.setNewTile(tileArray[i][j]);
            valueArray[i][j] = 2;
        }

        // FIXME: 7/14/17 - TESTING - this needs to be fixed later
        private void swipeHandler(MotionEvent event1, MotionEvent event2) {
            float x_difference = Math.abs(event1.getX() - event2.getX());
            float y_difference = Math.abs(event1.getY() - event2.getY());
            if (y_difference > x_difference) {
                if (event1.getY() > event2.getY()) {
                    swipeUp();
                }
                else {
                    swipeDown();

                }
            }

            else {
                if (event1.getX() > event2.getX()) {
                    swipeLeft();
                }
                else {
                    swipeRight();

                }
            }
            // only set new Tile when game state is changed
            if (this.hasChanged) {
                setRandomTile();
                this.hasChanged = false;
            }

        }


        // FIXME: 7/17/17 - need test
        private void swipeLeft() {
            for (int i = 0; i < 4; i = i + 1) {
                Tile[] row = tileArray[i];
                int[] valueRow = valueArray[i];
                for (int currentIndex = 1; currentIndex < 4; currentIndex = currentIndex + 1) {
                    Tile currentTile = row[currentIndex];
//                    int currentValue = currentTile.getValue();
                    int currentValue = valueRow[currentIndex];
                    if (currentValue == 0) { continue; }
                    int targetIndex = currentIndex - 1;
                    while (targetIndex > -1) {
//                        compare value using valueArray instead of tileArray
//                        int targetValue = row[targetIndex].getValue();
                        int targetValue = valueRow[targetIndex];
                        if (targetValue != 0) {
                            if (targetValue == currentValue) {
                                // FIXME: 7/17/17 - need test
                                animator.merge(currentTile, row[targetIndex], "H");
                                valueRow[targetIndex] = 2 * valueRow[targetIndex];
                                valueRow[currentIndex] = 0;
                                // add hasChanged
                                this.hasChanged = true;
                            } else {
                                // only move if targetTile is not currentTile
                                if (currentIndex != (targetIndex + 1)) {
                                    Tile targetTile = row[targetIndex + 1];
                                    animator.move(currentTile, targetTile, "H");
                                    valueRow[targetIndex + 1] = currentValue;
                                    valueRow[currentIndex] = 0;
                                    this.hasChanged = true;
                                }
                            }
                            break;
                        }
                        // if it get's to index 0 then move the tile to position 0
                        else if (targetIndex == 0) {
                            animator.move(row[currentIndex], row[targetIndex], "H");
                            valueRow[targetIndex] = currentValue;
                            valueRow[currentIndex] = 0;
                            this.hasChanged = true;
                            break;
                        }
                        targetIndex = targetIndex - 1;
                    }
                }
            }
        }

        private void swipeRight() {
            for (int i = 0; i < 4; i = i + 1) {
                Tile[] row = tileArray[i];
                int[] valueRow = valueArray[i];
                for (int currentIndex = 2; currentIndex > -1; currentIndex = currentIndex - 1) {
                    Tile currentTile = row[currentIndex];
                    // FIXME: 7/17/17 - need test
//                    int currentValue = currentTile.getValue();
                    int currentValue = valueRow[currentIndex];
                    if (currentValue == 0) { continue; }
                    int targetIndex = currentIndex + 1;
                    while (targetIndex < 4) {
//                        int targetValue = row[targetIndex].getValue();
                        int targetValue = valueRow[targetIndex];
                        if (targetValue != 0) {
                            if (targetValue == currentValue) {
                                animator.merge(currentTile, row[targetIndex], "H");
                                valueRow[targetIndex] = 2 * valueRow[targetIndex];
                                valueRow[currentIndex] = 0;
                                this.hasChanged = true;
                            }
                            else {
                                // only move if targetTile is not currentTile
                                if (currentIndex != (targetIndex - 1)) {
                                    Tile targetTile = row[targetIndex - 1];
                                    animator.move(currentTile, targetTile, "H");
                                    valueRow[targetIndex - 1] = currentValue;
                                    valueRow[currentIndex] = 0;
                                    this.hasChanged = true;
                                }
                            }
                            break;
                        }
                        else if (targetIndex == 3) {
                            animator.move(row[currentIndex], row[targetIndex], "H");
                            valueRow[targetIndex] = currentValue;
                            valueRow[currentIndex] = 0;
                            this.hasChanged = true;
                            break;
                        }
                        targetIndex = targetIndex + 1;
                    }
                }
            }
        }

        private void swipeUp() {
            for (int i = 0; i < 4; i = i + 1) {
                for (int currentIndex = 1; currentIndex < 4; currentIndex = currentIndex + 1) {
                    Tile currentTile = tileArray[currentIndex][i];
                    // FIXME: 7/17/17 - need test
//                    int currentValue = currentTile.getValue();
                    int currentValue = valueArray[currentIndex][i];
                    if (currentValue == 0) { continue; }
                    int targetIndex = currentIndex - 1;
                    while (targetIndex > -1) {
//                        int targetValue = tileArray[targetIndex][i].getValue();
                        int targetValue = valueArray[targetIndex][i];
                        if (targetValue != 0) {
                            if (targetValue == currentValue) {
                                animator.merge(currentTile, tileArray[targetIndex][i], "V");
                                valueArray[targetIndex][i] = 2 * valueArray[targetIndex][i];
                                valueArray[currentIndex][i] = 0;
                                this.hasChanged = true;
                            }
                            else {
                                // only move if targetTile is not currentTile
                                if (currentIndex != (targetIndex + 1)) {
                                    Tile targetTile = tileArray[targetIndex + 1][i];
                                    animator.move(currentTile, targetTile, "V");
                                    valueArray[targetIndex + 1][i] = currentValue;
                                    valueArray[currentIndex][i] = 0;
                                    this.hasChanged = true;
                                }
                            }
                            break;
                        }
                        else if (targetIndex == 0) {
                            animator.move(currentTile, tileArray[targetIndex][i], "V");
                            valueArray[targetIndex][i] = currentValue;
                            valueArray[currentIndex][i] = 0;
                            this.hasChanged = true;
                            break;
                        }
                        targetIndex = targetIndex - 1;
                    }
                }
            }
        }

        private void swipeDown() {
            for (int i = 0; i < 4; i = i + 1) {
                for (int currentIndex = 2; currentIndex > -1; currentIndex = currentIndex - 1) {
                    Tile currentTile = tileArray[currentIndex][i];
//                    int currentValue = currentTile.getValue();
                    int currentValue = valueArray[currentIndex][i];
                    if (currentValue == 0) { continue; }
                    int targetIndex = currentIndex + 1;
                    while (targetIndex < 4) {
//                        int targetValue = tileArray[targetIndex][i].getValue();
                        int targetValue = valueArray[targetIndex][i];
                        if (targetValue != 0) {
                            if (targetValue == currentValue) {
                                animator.merge(currentTile, tileArray[targetIndex][i], "V");
                                valueArray[targetIndex][i] =  2 * valueArray[targetIndex][i];
                                valueArray[currentIndex][i] = 0;
                                this.hasChanged = true;
                            }
                            else {
                                if (targetIndex != (currentIndex + 1)) {
                                    Tile targetTile = tileArray[targetIndex - 1][i];
                                    animator.move(currentTile, targetTile, "V");
                                    valueArray[targetIndex - 1][i] = currentValue;
                                    valueArray[currentIndex][i] = 0;
                                    this.hasChanged = true;
                                }
                            }
                            break;
                        }
                        else if (targetIndex == 3) {
                            animator.move(currentTile, tileArray[targetIndex][i], "V");
                            valueArray[targetIndex][i] = currentValue;
                            valueArray[currentIndex][i] = 0;
                            this.hasChanged = true;
                            break;
                        }
                        targetIndex = targetIndex + 1;
                    }
                }
            }
        }



        private class Animator {
            private int scaleDuration;
            private long moveDuration;


            private Animator() {
                this.scaleDuration = 150;
                this.moveDuration = 0;
            }

            private void merge(final Tile currentTile, final Tile targetTile, String orientation) {
                AnimatorSet mergeAnimator = new AnimatorSet();
//                final Tile animateTile = new Tile(context, targetTile.position);
                final Tile animateTile = new Tile(context, currentTile.position, currentTile.getValue());
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) currentTile.getLayoutParams();
                // set background resource here for testing purpose
//                animateTile.setValue(currentTile.getValue());
//                animateTile.setBackgroundResource(R.drawable.test);
                animateTile.setValue(currentTile.getValue());
                gameContainer.addView(animateTile, params);
                long tileTravel;
                float distance;
                ObjectAnimator moveAnimator;
                if (orientation.equals("H")) {
                    tileTravel = currentTile.position - targetTile.position;
                    distance = targetTile.getX() - currentTile.getX();
                    float distanceTravel = (tileTravel > 0) ? distance + tileMargin : distance - tileMargin;
                    moveAnimator = ObjectAnimator.ofFloat(animateTile, "translationX", distanceTravel);
                }
                else {
                    tileTravel = currentTile.position/10 - targetTile.position/10;
                    distance = targetTile.getY() - currentTile.getY();
                    float distanceTravel = (tileTravel > 0) ? distance + tileMargin : distance - tileMargin;

                    moveAnimator = ObjectAnimator.ofFloat(animateTile, "translationY", distanceTravel);
                }
                long duration = 75 * Math.abs(tileTravel);
                // get the move duration for setNewTile's delay
                if (duration > this.moveDuration) { this.moveDuration = duration; }
                moveAnimator.setDuration(duration);
                // add listener on mergeAnimator ?
                moveAnimator.addListener(new android.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(android.animation.Animator animator) {
                        currentTile.setValue(0);
                    }

                    @Override
                    public void onAnimationEnd(android.animation.Animator animator) {
                        targetTile.setValue(2 * targetTile.getValue());
                        ((ViewManager) animateTile.getParent()).removeView(animateTile);
                    }

                    @Override
                    public void onAnimationCancel(android.animation.Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(android.animation.Animator animator) {

                    }
                });
                ObjectAnimator scaleHeight = ObjectAnimator.ofFloat(targetTile, "scaleY",
                        1f, 1.15f, 1f);
                scaleHeight.setDuration(scaleDuration);

                ObjectAnimator scaleWidth = ObjectAnimator.ofFloat(targetTile, "scaleX",
                        1f, 1.15f, 1f);
                scaleWidth.setDuration(scaleDuration);
                mergeAnimator.play(scaleHeight).with(scaleWidth).after(moveAnimator);
                mergeAnimator.start();
            }

            private void move(final Tile currentTile, final Tile targetTile, String orientation) {
                final int value = currentTile.getValue();
                int currentPosition = currentTile.position;
                int targetPosition = targetTile.position;
                final Tile animateTile = new Tile(context, currentPosition, value);
                animateTile.setValue(value);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) currentTile.getLayoutParams();
                gameContainer.addView(animateTile, params);
                // calculate tileTravel and distanceTravel based on given "orientation"
                ObjectAnimator move;
                long tileTravel = (orientation.equals("H")) ?
                        Math.abs(targetPosition - currentPosition) : Math.abs(targetPosition/10 - currentPosition/10);
                float distanceTravel = (orientation.equals("H")) ?
                        targetTile.getX() - currentTile.getX() : targetTile.getY() - currentTile.getY();
                if (orientation.equals("H")) {
                    move = ObjectAnimator.ofFloat(animateTile, "translationX", distanceTravel);
                }
                else {
                    move = ObjectAnimator.ofFloat(animateTile, "translationY", distanceTravel);
                }
                long duration = 75 * tileTravel;
                if (duration > this.moveDuration) { this.moveDuration = duration; }
                move.setDuration(duration);
                move.addListener(new android.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(android.animation.Animator animator) {
                        currentTile.setValue(0);
                    }

                    @Override
                    public void onAnimationEnd(android.animation.Animator animator) {
                        targetTile.setValue(value);
                        ((ViewManager) currentTile.getParent()).removeView(animateTile);
                    }

                    @Override
                    public void onAnimationCancel(android.animation.Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(android.animation.Animator animator) {

                    }
                });
                move.start();
            }

            private void setNewTile(final Tile targetTile) {
                ObjectAnimator scaleHeight = ObjectAnimator.ofFloat(targetTile, "scaleY",
                        0.5f, 1f);
                scaleHeight.setDuration(scaleDuration);

                ObjectAnimator scaleWidth = ObjectAnimator.ofFloat(targetTile, "scaleX",
                        0.5f, 1f);
                scaleWidth.setDuration(scaleDuration);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(scaleHeight).with(scaleWidth);
                animatorSet.addListener(new android.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(android.animation.Animator animator) {
                        targetTile.setValue(2);
                    }

                    @Override
                    public void onAnimationEnd(android.animation.Animator animator) {

                    }

                    @Override
                    public void onAnimationCancel(android.animation.Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(android.animation.Animator animator) {

                    }
                });
                // wait for moves to finish
                animatorSet.setStartDelay(this.moveDuration/2);
                animatorSet.start();
            }

        }

    }










    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            scroll = false;
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent event1, MotionEvent event2,
                                float velocityX, float velocityY) {
            if (!scroll) {
//                 need condition here -- not swipe when distance < threshold

                game.swipeHandler(event1, event2);
                scroll = true;
            }
            return true;
        }
    }

}


