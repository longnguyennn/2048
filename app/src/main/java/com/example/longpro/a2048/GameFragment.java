package com.example.longpro.a2048;

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
                int gcDimension = game_container.getHeight();
                margin = gcDimension / 30;
                int btwMargin = margin / 2;
                tileDimension = (gcDimension - (5 * margin)) / 4;



                // FIXME: 7/10/17 - need to set view's reference to null onDestroy
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

        private Game(RelativeLayout gameContainer) {
            this.gameContainer = gameContainer;
            setNewNumber();
            setNewNumber();
        }

        private void setNewNumber() {
            int i = new Random().nextInt(4);
            int j = new Random().nextInt(4);
            int tileValue = tileArray[i][j].getValue();
            while (tileValue != 0) {
                i = new Random().nextInt(4);
                j = new Random().nextInt(4);
            }
            tileArray[i][j].setValue(2);
        }

        private void swipeHandler() {
//            Animator animator = new Animator();
//            animator.addMerge(tileArray[0][0]);
            Animator animator = new Animator();
            moveTile(tileArray[0][1], tileArray[0][0], animator);

        }

        private void swipeLeft(Animator animator) {
            int arraySize = 4;
            for (int i = 0; i < arraySize; i = i + 1) {
                Tile[] row = tileArray[i];
                for (int j = 1; j < arraySize; j = j + 1) {
                    Tile currentTile = row[j];
                    int currentValue = currentTile.getValue();
                    if (currentValue == 0) {
                        continue;
                    }
                    int prevIndex = j - 1;
                    while (prevIndex > -1) {
                        Tile prevTile = row[prevIndex];
                        int prevValue = prevTile.getValue();
                        if (prevValue != 0) {
                            if (prevValue == currentValue) {
                                // set isMerged to true
                                currentTile.isMerged = true;
                                mergeTile(currentTile, prevTile, animator);
                            } else {
                                // move current tile to the tile next to tile at prevIndex
                                Tile targetTile = row[prevIndex + 1];
                                moveTile(currentTile, targetTile, animator);
                            }
                            break;
                        } else {
                            // move to prevIndex because there's no more to go
                            if (prevIndex == 0) {
                                Tile targetTile = row[prevIndex];
                                moveTile(currentTile, targetTile, animator);
                                break;
                            }
                            prevIndex = prevIndex - 1;
                        }
                    }
                }
            }
        }

        // Merge current tile with another tile
        private void mergeTile(Tile currentTile, Tile targetTile, Animator animator) {
            int value = currentTile.getValue();
            int newValue = 2 * value;
            targetTile.setValue(newValue);
            currentTile.setValue(0);
            // add a merge object animator here .. i.e: addMerge
            animator.addMerge(targetTile);
        }

        // Move current tile to target tile
        private void moveTile(Tile currentTile, Tile targetTile, Animator animator) {
            // clone currentTile on top of currentTile and call addMove on it

            int value = currentTile.getValue();
            // FIXME: 7/13/17 TESTING
            Log.i("This is working", "oh yeah");
            Tile animateTile = new Tile(context, 1000, 1000);
            RelativeLayout.LayoutParams TileParams =
                    new RelativeLayout.LayoutParams(tileDimension, tileDimension);
            TileParams.addRule(RelativeLayout.START_OF, currentTile.getId());
            gameContainer.addView(animateTile, TileParams);
            targetTile.setValue(value);
            currentTile.setValue(0);
            // add a move object animator here .. i.e: addMove
            animator.addMove(currentTile, targetTile);
        }

        private class Animator {
            private List<AnimatorSet> moveSet;
            private List<AnimatorSet> mergeSet;
            private int scaleDuration;
//            private ObjectAnimator newTile;

            private Animator() {
                moveSet = new ArrayList<>();
                mergeSet = new ArrayList<>();
                scaleDuration = 500;
//                newTile = new ObjectAnimator();
            }

            public void addMerge(Tile tile) {
                AnimatorSet scaleAnimator = new AnimatorSet();
                ObjectAnimator scaleHeight = ObjectAnimator.ofFloat(tile, "scaleY",
                        1f, 1.1f, 1f);
                scaleHeight.setDuration(scaleDuration);

                ObjectAnimator scaleWidth = ObjectAnimator.ofFloat(tile, "scaleX",
                        1f, 1.1f, 1f);
                scaleWidth.setDuration(scaleDuration);
                scaleAnimator.play(scaleHeight).with(scaleWidth);
                mergeSet.add(scaleAnimator);
            }

            public void addMove(Tile moveTile, Tile targetTile) {

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
//                board.setNewNumber();
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