package com.example.longpro.a2048;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;


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
    private SizedStack<String> moveHistory;
    private CurrentScore currentScore;
    private Logo logo;
    private Paragraph paragraph;
    private HighScore highScore;
    private UndoButton undoButton;
    private ResetButton resetButton;
    private RelativeLayout gameHeader;
    private RelativeLayout gameContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // limit the stack size to 10 moves
        moveHistory = new SizedStack<>(10);
        tileArray = new Tile[4][4];
        context = this;
        gameContainer = (RelativeLayout) findViewById(R.id.game_container);
        mDetector = new GestureDetectorCompat(context, new MyGestureDetector());
        mListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                gameContainer.onTouchEvent(event);
                mDetector.onTouchEvent(event);
                return true;
            }
        };
        gameContainer.setOnTouchListener(mListener);

        gameHeader = (RelativeLayout) findViewById(R.id.game_header);
        gameHeader.post(new Runnable() {
            @Override
            public void run() {
                int width = gameHeader.getWidth();
                int height = gameHeader.getHeight();
                int layoutMargin = width / 25;
                int layoutWidth = width - 2 * layoutMargin;
                int layoutHeight = height - 2 * layoutMargin;
                LinearLayout.LayoutParams headerParams = (LinearLayout.LayoutParams) gameHeader.getLayoutParams();
                headerParams.setMargins(layoutMargin, layoutMargin, layoutMargin, layoutMargin);
                // add Logo to game_header
                logo = new Logo(context, layoutWidth, layoutHeight);
                gameHeader.addView(logo, logo.params);
                // add paragraph
                paragraph = new Paragraph(context, logo);
                gameHeader.addView(paragraph, paragraph.params);
                // add currentScore
                currentScore = new CurrentScore(context, layoutWidth, layoutHeight, layoutMargin, logo);
                gameHeader.addView(currentScore, currentScore.params);
                // add highScore here...
                highScore = new HighScore(context, currentScore, layoutMargin);
                gameHeader.addView(highScore, highScore.params);
                // add resetButton with onTouchListener
                resetButton = new ResetButton(context, logo, highScore);
                gameHeader.addView(resetButton, resetButton.params);
                // add undoButton with onTouchListener
                undoButton = new UndoButton(context, highScore, resetButton);
                gameHeader.addView(undoButton, undoButton.params);



            }
        });

        gameContainer.post(new Runnable() {
            @Override
            public void run() {
                int layoutWidth = gameContainer.getWidth();
                int layoutMargin = layoutWidth / 25;
                LinearLayout.LayoutParams gcParams = (LinearLayout.LayoutParams) gameContainer.getLayoutParams();
                gcParams.setMargins(layoutMargin, 0, layoutMargin, layoutMargin);
                int actualDimension = layoutWidth - 2 * layoutMargin;
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
                            tile.setId(Tile.generateViewId());
                            if ( j == 0 ) {
                                TileParams.setMargins(tileMargin, tileMargin, btwMargin, btwMargin);
                            }

                            else {
                                TileParams.setMargins(btwMargin, tileMargin, btwMargin, btwMargin);
                                TileParams.addRule(RelativeLayout.RIGHT_OF, tileArray[i][j - 1].getId());
                            }
                            gameContainer.addView(tile, TileParams);
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
                            gameContainer.addView(tile, TileParams);
                            tileArray[i][j] = tile;
                        }
                    }
                }
                game = new Game(gameContainer);
            }
        });


    }














    private class Game {
        private RelativeLayout gameContainer;
        private Animator animator;
        private boolean hasChanged;
        // array used for the game logic (use tileArray for view animation)
        private int[][] valueArray;
        // game's current score
        private int gameScore = 0;
        private int increaseScore;

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
            // nextInt = get random values range [0,4)
            int i = new Random().nextInt(4);
            int j = new Random().nextInt(4);
            int tileValue = valueArray[i][j];
            while (tileValue != 0) {
                i = new Random().nextInt(4);
                j = new Random().nextInt(4);
                tileValue = valueArray[i][j];
            }
//            set tile value pseudo-randomly
            int randomValue = new Random().nextInt(6);
            int setValue = (randomValue < 5) ? 2 : 4;
            animator.setNewTile(tileArray[i][j], setValue);
            valueArray[i][j] = setValue;
        }


        private void swipeHandler(MotionEvent event1, MotionEvent event2) {
            // set increaseScore to 0 at the beginning of each swipe
            increaseScore = 0;
            float x_difference = Math.abs(event1.getX() - event2.getX());
            float y_difference = Math.abs(event1.getY() - event2.getY());
            // call swipeUp/Down/Left/Right according to MotionEvent params
            // and push move to moveHistory stack
            if (y_difference > x_difference) {
                if (event1.getY() > event2.getY()) {
                    swipeUp();
                    moveHistory.push("swipeUp");
                }
                else {
                    swipeDown();
                    moveHistory.push("swipeDown");
                }
            }

            else {
                if (event1.getX() > event2.getX()) {
                    swipeLeft();
                    moveHistory.push("swipeLeft");
                }
                else {
                    swipeRight();
                    moveHistory.push("swipeRight");
                }
            }
            // only set new Tile when game state is changed
            if (this.hasChanged) {
                setRandomTile();
                if (increaseScore != 0) {
                    this.gameScore += this.increaseScore;
                    currentScore.setNewScore(gameScore);
                }
                this.hasChanged = false;
            }

        }

        private void swipeLeft() {
            for (int i = 0; i < 4; i = i + 1) {
                Tile[] row = tileArray[i];
                int[] valueRow = valueArray[i];
                for (int currentIndex = 1; currentIndex < 4; currentIndex = currentIndex + 1) {
                    Tile currentTile = row[currentIndex];
                    int currentValue = valueRow[currentIndex];
                    if (currentValue == 0) { continue; }
                    int targetIndex = currentIndex - 1;
                    while (targetIndex > -1) {
//                        compare value using valueArray instead of tileArray
                        int targetValue = valueRow[targetIndex];
                        if (targetValue != 0) {
                            if (targetValue == currentValue) {
                                animator.merge(currentTile, row[targetIndex], "H");
                                valueRow[targetIndex] = 2 * valueRow[targetIndex];
                                valueRow[currentIndex] = 0;
                                // increment score
                                increaseScore += (valueRow[targetIndex]);
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
                    int currentValue = valueRow[currentIndex];
                    if (currentValue == 0) { continue; }
                    int targetIndex = currentIndex + 1;
                    while (targetIndex < 4) {
                        int targetValue = valueRow[targetIndex];
                        if (targetValue != 0) {
                            if (targetValue == currentValue) {
                                animator.merge(currentTile, row[targetIndex], "H");
                                valueRow[targetIndex] = 2 * valueRow[targetIndex];
                                valueRow[currentIndex] = 0;
                                // increment score
                                increaseScore += valueRow[targetIndex];
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
                    int currentValue = valueArray[currentIndex][i];
                    if (currentValue == 0) { continue; }
                    int targetIndex = currentIndex - 1;
                    while (targetIndex > -1) {
                        int targetValue = valueArray[targetIndex][i];
                        if (targetValue != 0) {
                            if (targetValue == currentValue) {
                                animator.merge(currentTile, tileArray[targetIndex][i], "V");
                                valueArray[targetIndex][i] = 2 * valueArray[targetIndex][i];
                                valueArray[currentIndex][i] = 0;
                                // increment score
                                increaseScore += valueArray[targetIndex][i];
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
                    int currentValue = valueArray[currentIndex][i];
                    if (currentValue == 0) { continue; }
                    int targetIndex = currentIndex + 1;
                    while (targetIndex < 4) {
                        int targetValue = valueArray[targetIndex][i];
                        if (targetValue != 0) {
                            if (targetValue == currentValue) {
                                animator.merge(currentTile, tileArray[targetIndex][i], "V");
                                valueArray[targetIndex][i] =  2 * valueArray[targetIndex][i];
                                valueArray[currentIndex][i] = 0;
                                // increment score
                                increaseScore += valueArray[targetIndex][i];
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
                final Tile animateTile = new Tile(context, currentTile.position, currentTile.getValue());
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) currentTile.getLayoutParams();
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

            private void setNewTile(final Tile targetTile, final int value) {
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
                        targetTile.setValue(value);
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
            scroll = true;
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent event1, MotionEvent event2,
                                float velocityX, float velocityY) {
            if (scroll) {
//                 need condition here -- not swipe when distance < threshold

                game.swipeHandler(event1, event2);
                scroll = false;
            }
            return true;
        }
    }

}


