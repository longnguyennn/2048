package com.example.longpro.a2048;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * Created by longpro on 7/9/17.
 */

public class GameFragment extends Fragment {
    private GestureDetectorCompat mDetector;
    View.OnTouchListener mListener;
    private boolean scroll = false;
    private Tile[][] tileArray;
    private Context context;
    private Game game;
    private int margin;
    private int tileDimension;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        tileArray = new Tile[4][4];
        final View gameView = inflater.inflate(R.layout.game_container, container, false);
        final RelativeLayout game_container = gameView.findViewById(R.id.game_container);
        mDetector = new GestureDetectorCompat(context, new MyGestureDetector());
        mListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                game_container.onTouchEvent(event);
                mDetector.onTouchEvent(event);
                return true;
            }
        };
        game_container.setOnTouchListener(mListener);


        // initialize game
        game_container.post(new Runnable() {
            @Override
            public void run() {
                Log.i("Executing", "runnable");
                int gcDimension = game_container.getHeight();
                margin = gcDimension / 30;
                int btwMargin = margin / 2;
                tileDimension = (gcDimension - (5 * margin)) / 4;



                // FIXME: 7/10/17 - rewrite this
                // initialize an array of tiles
                for (int i = 0; i < 4; i = i + 1) {
                    if ( i == 0 ) {
                        for (int j = 0; j < 4; j = j + 1) {
                            RelativeLayout.LayoutParams TileParams =
                                    new RelativeLayout.LayoutParams(tileDimension, tileDimension);
                            int position = 10 * i + j;
                            Tile tile = new Tile(context, position);
                            // FIXME: 7/13/17 - doesn't need setBackgroundResource here ....
                            tile.setBackgroundResource(R.drawable.component);
                            tile.setId(Tile.generateViewId());
                            if ( j == 0 ) {
                                TileParams.setMargins(margin, margin, btwMargin, btwMargin);
                            }

                            else {
                                TileParams.setMargins(btwMargin, margin, btwMargin, btwMargin);
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
                            Tile tile = new Tile(context, position);
                            tile.setBackgroundResource(R.drawable.component);
                            tile.setId(Tile.generateViewId());
                            if ( j == 0 ) {
                                TileParams.setMargins(margin, btwMargin, btwMargin, btwMargin);
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
        return gameView;
    }


    private class Game {
        private RelativeLayout gameContainer;
        private Animator animator;

        private Game(RelativeLayout gameContainer) {
            this.gameContainer = gameContainer;
            animator = new Animator();
            int i = new Random().nextInt(4);
            int j = new Random().nextInt(4);
            int tileValue = tileArray[i][j].getValue();
            for (int counter = 0; counter < 2; counter = counter + 1) {
                while (tileValue != 0) {
                    i = new Random().nextInt(4);
                    j = new Random().nextInt(4);
                    tileValue = tileArray[i][j].getValue();
                }
                tileArray[i][j].setValue(2);
            }

        }

        private void setRandomTile() {
            int i = new Random().nextInt(4);
            int j = new Random().nextInt(4);
            int tileValue = tileArray[i][j].getValue();
            while (tileValue != 0) {
                i = new Random().nextInt(4);
                j = new Random().nextInt(4);
                tileValue = tileArray[i][j].getValue();
            }
//            set tile value in setNewTile
//            tileArray[i][j].setValue(2);
            animator.setNewTile(tileArray[i][j]);
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
            setRandomTile();
        }

        private void swipeLeft() {
            for (int i = 0; i < 4; i = i + 1) {
                Tile[] row = tileArray[i];
                for (int currentIndex = 1; currentIndex < 4; currentIndex = currentIndex + 1) {
                    Tile currentTile = row[currentIndex];
                    int currentValue = currentTile.getValue();
                    if (currentValue == 0) { continue; }
                    int targetIndex = currentIndex - 1;
                    while (targetIndex > -1) {
                        int targetValue = row[targetIndex].getValue();
                        if (targetValue != 0) {
                            if (targetValue == currentValue) {
                                animator.merge(currentTile, row[targetIndex], "H");
                            } else {
                                // only move if targetTile is not currentTile
                                if (currentIndex != (targetIndex + 1)) {
                                    Tile targetTile = row[targetIndex + 1];
                                    animator.move(currentTile, targetTile, "H");
                                }
                            }
                            break;
                        }
                        // if it get's to index 0 then move the tile to position 0
                        else if (targetIndex == 0) {
                            animator.move(row[currentIndex], row[targetIndex], "H");
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
                for (int currentIndex = 2; currentIndex > -1; currentIndex = currentIndex - 1) {
                    Tile currentTile = row[currentIndex];
                    int currentValue = currentTile.getValue();
                    if (currentValue == 0) { continue; }
                    int targetIndex = currentIndex + 1;
                    while (targetIndex < 4) {
                        int targetValue = row[targetIndex].getValue();
                        if (targetValue != 0) {
                            if (targetValue == currentValue) {
                                animator.merge(currentTile, row[targetIndex], "H");
                            }
                            else {
                                // only move if targetTile is not currentTile
                                if (currentIndex != (targetIndex - 1)) {
                                    Tile targetTile = row[targetIndex - 1];
                                    animator.move(currentTile, targetTile, "H");
                                }
                            }
                            break;
                        }
                        else if (targetIndex == 3) {
                            animator.move(row[currentIndex], row[targetIndex], "H");
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
                    int currentValue = currentTile.getValue();
                    if (currentValue == 0) { continue; }
                    int targetIndex = currentIndex - 1;
                    while (targetIndex > -1) {
                        int targetValue = tileArray[targetIndex][i].getValue();
                        if (targetValue != 0) {
                            if (targetValue == currentValue) {
                                animator.merge(currentTile, tileArray[targetIndex][i], "V");
                            }
                            else {
                                // only move if targetTile is not currentTile
                                if (currentIndex != (targetIndex + 1)) {
                                    Tile targetTile = tileArray[targetIndex + 1][i];
                                    animator.move(currentTile, targetTile, "V");
                                }
                            }
                            break;
                        }
                        else if (targetIndex == 0) {
                            animator.move(currentTile, tileArray[targetIndex][i], "V");
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
                    int currentValue = currentTile.getValue();
                    if (currentValue == 0) { continue; }
                    int targetIndex = currentIndex + 1;
                    while (targetIndex < 4) {
                        int targetValue = tileArray[targetIndex][i].getValue();
                        if (targetValue != 0) {
                            if (targetValue == currentValue) {
                                animator.merge(currentTile, tileArray[targetIndex][i], "V");
                            }
                            else {
                                if (targetIndex != (currentIndex + 1)) {
                                    Tile targetTile = tileArray[targetIndex - 1][i];
                                    animator.move(currentTile, targetTile, "V");
                                }
                            }
                            break;
                        }
                        else if (targetIndex == 3) {
                            animator.move(currentTile, tileArray[targetIndex][i], "V");
                            break;
                        }
                        targetIndex = targetIndex + 1;
                    }
                }
            }
        }



        private class Animator {
            private int scaleDuration;


            private Animator() {
                scaleDuration = 400;
            }

            public void merge(final Tile currentTile, final Tile targetTile, String orientation) {
                AnimatorSet mergeAnimator = new AnimatorSet();
                final Tile animateTile = new Tile(context, targetTile.position);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) currentTile.getLayoutParams();
                // set background resource here for testing purpose
                animateTile.setValue(currentTile.getValue());
                animateTile.setBackgroundResource(R.drawable.test);
                gameContainer.addView(animateTile, params);
                long tileTravel;
                float distance;
                ObjectAnimator moveAnimator;
                if (orientation.equals("H")) {
                    tileTravel = currentTile.position - targetTile.position;
                    distance = targetTile.getX() - currentTile.getX();
                    float distanceTravel = (tileTravel > 0) ? distance + margin : distance - margin;
                    moveAnimator = ObjectAnimator.ofFloat(animateTile, "translationX", distanceTravel);
                }
                else {
                    tileTravel = currentTile.position/10 - targetTile.position/10;
                    distance = targetTile.getY() - currentTile.getY();
                    float distanceTravel = (tileTravel > 0) ? distance + margin : distance - margin;

                    moveAnimator = ObjectAnimator.ofFloat(animateTile, "translationY", distanceTravel);
                }
                long duration = 100 * Math.abs(tileTravel);
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

            // FIXME: 7/14/17 - horizontal move only at the moment
            public void move(final Tile currentTile, final Tile targetTile, String orientation) {
                final int value = currentTile.getValue();
                int currentPosition = currentTile.position;
                int targetPosition = targetTile.position;
                // doesn't really matter which position here
                final Tile animateTile = new Tile(context, targetPosition);
                animateTile.setValue(value);
                animateTile.setBackgroundResource(R.drawable.test);
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
                long duration = 100 * tileTravel;
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

            public void setNewTile(final Tile targetTile) {
                ObjectAnimator scaleHeight = ObjectAnimator.ofFloat(targetTile, "scaleY",
                        0.5f, 1f);
                scaleHeight.setDuration(scaleDuration/2);

                ObjectAnimator scaleWidth = ObjectAnimator.ofFloat(targetTile, "scaleX",
                        0.5f, 1f);
                scaleWidth.setDuration(scaleDuration/2);
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
                animatorSet.start();
            }

        }

    }










    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

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

















//        private void swipeHandler(MotionEvent event1, MotionEvent event2) {
//            String DEBUG_TAG = "Array";
//            int[] differences = new int[2];
//
//            float x_difference = Math.abs(event1.getX() - event2.getX());
//            float y_difference = Math.abs(event1.getY() - event2.getY());
//            if (y_difference > x_difference) {
//                if (event1.getY() > event2.getY()) {
//                    board.swipeUp();
//                }
//                else {
//                    board.swipeDown();
//
//                }
//            }
//            else {
//                if (event1.getX() > event2.getX()) {
//                    board.swipeLeft();
//                }
//                else {
//                    board.swipeRight();
//
//                }
//            }
//
//            if (board.hasChanged) {
//                board.setRandomTile();
////                renderBoard();
//            }
////             check trackChange's functionality
//            for (int a = 0; a < board.trackChanges.size(); a = a + 1) {
//                Log.d("TRACK CHANGE SIZE", Integer.toString(board.trackChanges.size()));
//                Log.d("TRACK CHANGES: ", Arrays.toString(board.trackChanges.get(a)));
//            }
//
//            board.trackChanges.clear();
//            board.hasChanged = false;
//
//
//            // Long Nguyen - this should work
//            for (int a = 0; a < 4; a = a + 1) {
//                Log.d(DEBUG_TAG, Arrays.toString(board.array[a]));
//            }
//
//        }