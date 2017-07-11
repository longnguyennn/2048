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


/**
 * Created by longpro on 7/9/17.
 */

public class GameFragment extends Fragment {
    private GestureDetectorCompat mDetector;
    View.OnTouchListener mListener;
    private boolean scroll = false;
    private Tile[][] TileArray;
    private Context context;
    private Game game;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        TileArray = new Tile[4][4];
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
                for (int i = 0; i < 4; i = i + 1) {
                    if ( i == 0 ) {
                        for (int j = 0; j < 4; j = j + 1) {
                            RelativeLayout.LayoutParams TileParams =
                                    new RelativeLayout.LayoutParams(TileDimension, TileDimension);
                            Tile tile = new Tile(context);
                            tile.setBackgroundResource(R.drawable.component);
                            tile.setId(Tile.generateViewId());
                            if ( j == 0 ) {
                                TileParams.setMargins(Margin, Margin, btwMargin, btwMargin);
                            }

                            else {
                                TileParams.setMargins(btwMargin, Margin, btwMargin, btwMargin);
                                TileParams.addRule(RelativeLayout.RIGHT_OF, TileArray[i][j - 1].getId());
                            }
                            game_container.addView(tile, TileParams);
                            TileArray[i][j] = tile;
                        }
                    }

                    else {
                        for (int j = 0; j < 4; j = j + 1) {
                            RelativeLayout.LayoutParams TileParams =
                                    new RelativeLayout.LayoutParams(TileDimension, TileDimension);
                            Tile tile = new Tile(context);
                            tile.setBackgroundResource(R.drawable.component);
                            tile.setId(Tile.generateViewId());
                            if ( j == 0 ) {
                                TileParams.setMargins(Margin, btwMargin, btwMargin, btwMargin);
                                TileParams.addRule(RelativeLayout.BELOW, TileArray[i - 1][j].getId());
                            }
                            else {
                                TileParams.setMargins(btwMargin, btwMargin, btwMargin, btwMargin);
                                TileParams.addRule(RelativeLayout.RIGHT_OF, TileArray[i][j - 1].getId());
                                TileParams.addRule(RelativeLayout.BELOW, TileArray[i - 1][j].getId());
                            }
                            game_container.addView(tile, TileParams);
                            TileArray[i][j] = tile;
                        }
                    }
                }

                game = new Game(TileArray);

            }
        });
        return gameView;
    }


    private class Game {
        private Board board;
        private Tile[][] tileArray;

        private Game(Tile[][] tileArray) {
            this.tileArray = tileArray;
            this.board = new Board();
            for (int[] i : board.newNumbers) {
                int x = i[0];
                int y = i[1];
                this.tileArray[x][y].setText(Integer.toString(this.board.array[x][y]));
            }
        }

        private void swipeHandler(MotionEvent event1, MotionEvent event2) {
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
                board.setNewNumber();
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
