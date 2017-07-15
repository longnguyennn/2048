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
            setRandomTile();
            setRandomTile();
            animator = new Animator();
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
            tileArray[i][j].setValue(2);
        }

        // FIXME: 7/14/17 - TESTING - this needs to be fixed later 
        private void swipeHandler() {
            swipeLeft();
            setRandomTile();
        }

        private void swipeLeft() {
            for (int i = 0; i < 4; i = i + 1) {
                Tile[] row = tileArray[i];
                for (int currentIndex = 1; currentIndex < 4; currentIndex = currentIndex + 1) {
                    Tile currentTile = row[currentIndex];
                    int currentValue = currentTile.getValue();
                    if (currentValue == 0) { continue; }
                    int prevIndex = currentIndex - 1;
                    while (prevIndex > -1) {
                        int prevValue = row[prevIndex].getValue();
                        if (prevValue != 0) {
                            if (prevValue == currentValue) {
                                animator.merge(currentTile, row[prevIndex]);
                            } else {
                                animator.move(currentTile, row[prevIndex + 1]);
                            }
                            break;
                        }
                        else if (prevIndex == 0) {
                            animator.move(row[currentIndex], row[prevIndex]);
                            break;
                        }
                        prevIndex = prevIndex - 1;
                    }
                }
            }
        }

        private class Animator {
            private List<AnimatorSet> mergeSet;
            private int scaleDuration;

            private ObjectAnimator newTile;

            private Animator() {
                mergeSet = new ArrayList<>();
                scaleDuration = 400;
//                newTile = new ObjectAnimator();
            }

            public void merge(final Tile currentTile, final Tile targetTile) {
                AnimatorSet mergeAnimator = new AnimatorSet();
                final Tile animateTile = new Tile(context, targetTile.position);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) currentTile.getLayoutParams();
                // doesn't need set background resource here
                animateTile.setValue(currentTile.getValue());
                gameContainer.addView(animateTile, params);

                long tileTravel = Math.abs(currentTile.position - targetTile.position);
                float distanceTravel = currentTile.getX() - targetTile.getX();
                long duration = 75 * tileTravel;
                ObjectAnimator moveAnimator = ObjectAnimator.ofFloat(animateTile, "translationX", distanceTravel);
                moveAnimator.setDuration(duration);
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
                        1f, 1.1f, 1f);
                scaleHeight.setDuration(scaleDuration);

                ObjectAnimator scaleWidth = ObjectAnimator.ofFloat(targetTile, "scaleX",
                        1f, 1.1f, 1f);
                scaleWidth.setDuration(scaleDuration);
                mergeAnimator.play(scaleHeight).with(scaleWidth).after(moveAnimator);
                mergeAnimator.start();
            }

            // FIXME: 7/14/17 - horizontal move only at the moment
            public void move(final Tile currentTile, final Tile targetTile) {
                final int value = currentTile.getValue();
                int currentPosition = currentTile.position;
                int targetPosition = targetTile.position;
                // doesn't really matter which position here
                final Tile animateTile = new Tile(context, targetPosition);
                animateTile.setValue(value);
                animateTile.setBackgroundResource(R.drawable.test);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) currentTile.getLayoutParams();
                gameContainer.addView(animateTile, params);

                long tileTravel = Math.abs(targetPosition - currentPosition);
                float distanceTravel = targetTile.getX() - currentTile.getX();
                ObjectAnimator move = ObjectAnimator.ofFloat(animateTile, "translationX", distanceTravel);
                long duration = 75 * tileTravel;
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
//                        animateTile = null;
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


                game.swipeHandler();
//                game.swipeHandler(event1, event2);
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