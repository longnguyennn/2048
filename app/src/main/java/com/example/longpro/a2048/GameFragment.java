package com.example.longpro.a2048;

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

import java.util.Arrays;
import java.util.Random;


/**
 * Created by longpro on 7/9/17.
 */

public class GameFragment extends Fragment {
    private GestureDetectorCompat mDetector;
    View.OnTouchListener mListener;
    private boolean scroll = false;
    private Tile[][] initArray;
    private Context context;
    private Game game;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        initArray = new Tile[4][4];
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
                int Margin = gcDimension / 30;
                int btwMargin = Margin / 2;
                int TileDimension = (gcDimension - (5 * Margin)) / 4;



                // FIXME: 7/10/17 - need to set view's reference to null onDestroy
                // initialize an array of tiles
                for (int i = 0; i < 4; i = i + 1) {
                    if ( i == 0 ) {
                        for (int j = 0; j < 4; j = j + 1) {
                            RelativeLayout.LayoutParams TileParams =
                                    new RelativeLayout.LayoutParams(TileDimension, TileDimension);
                            int[] position = new int[2];
                            position[0] = i;
                            position[1] = j;
                            Tile tile = new Tile(context, position);
                            tile.setBackgroundResource(R.drawable.component);
                            tile.setId(Tile.generateViewId());
                            if ( j == 0 ) {
                                TileParams.setMargins(Margin, Margin, btwMargin, btwMargin);
                            }

                            else {
                                TileParams.setMargins(btwMargin, Margin, btwMargin, btwMargin);
                                TileParams.addRule(RelativeLayout.RIGHT_OF, initArray[i][j - 1].getId());
                            }
                            game_container.addView(tile, TileParams);
                            initArray[i][j] = tile;
                        }
                    }

                    else {
                        for (int j = 0; j < 4; j = j + 1) {
                            RelativeLayout.LayoutParams TileParams =
                                    new RelativeLayout.LayoutParams(TileDimension, TileDimension);
                            int[] position = new int[2];
                            position[0] = i;
                            position[1] = j;
                            Tile tile = new Tile(context, position);
                            tile.setBackgroundResource(R.drawable.component);
                            tile.setId(Tile.generateViewId());
                            if ( j == 0 ) {
                                TileParams.setMargins(Margin, btwMargin, btwMargin, btwMargin);
                                TileParams.addRule(RelativeLayout.BELOW, initArray[i - 1][j].getId());
                            }
                            else {
                                TileParams.setMargins(btwMargin, btwMargin, btwMargin, btwMargin);
                                TileParams.addRule(RelativeLayout.RIGHT_OF, initArray[i][j - 1].getId());
                                TileParams.addRule(RelativeLayout.BELOW, initArray[i - 1][j].getId());
                            }
                            game_container.addView(tile, TileParams);
                            initArray[i][j] = tile;
                        }
                    }
                }

                game = new Game(initArray);

            }
        });
        return gameView;
    }


    private class Game {
        private Tile[][] tileArray;

        private Game(Tile[][] tileArray) {
            this.tileArray = tileArray;
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

        // FIXME: 7/12/17 this needs to be swipeLeft instead of swipeLeftRow
        private void swipeLeft() {
            int arraySize = 4;
            for (int i = 0; i < arraySize; i = i + 1) {
                Tile[] row = this.tileArray[i];
                for (int j = 1; j < arraySize; j = j + 1) {
                    Tile currentTile = row[j];
                    int currentValue = currentTile.getValue();
                    if (currentValue == 0) {
                        continue;
                    }
                    currentTile.setPrevPosition(i, j);
                    int prevIndex = j - 1;
                    while (prevIndex > -1) {
                        int preValue = row[prevIndex].getValue();
                        int[] newPosition = new int[2];
                        newPosition[0] = i;
                        if ( preValue != 0 ) {
                            if ( preValue == currentValue ) {
                                currentTile.isMerged = true;
                                newPosition[1] = prevIndex;
                            }
                            else {
                                newPosition[1] = prevIndex + 1;
                            }
                            break;
                        } else {
                            if ( prevIndex == 0 ) {
                                newPosition[1] = prevIndex;
                            }
                            currentTile.isMoved = true;
                            prevIndex = prevIndex - 1;
                        }
                        // call method to switch position here -- ?
                        // need a move tile method here
                        move(currentTile, newPosition);
                    }
                }
            }
        }

        // set the Tile to the new position in the tileArray
        // and set the prevPosition to new Tile
        private void move(Tile tile, int[] newPosition) {
            if ( tile.isMoved ) {
                tileArray[newPosition[0]][newPosition[1]] = tile;
                int[] prevPosition = tile.getPrevPosition();
                tileArray[prevPosition[0]][prevPosition[1]] = new Tile(context, prevPosition);
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