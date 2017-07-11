package com.example.longpro.a2048;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by longpro on 7/2/17.
 */

public class Board {
    public int[][] array;
    public boolean hasChanged = false;
    public List<int[]> newNumbers = new ArrayList<>();
    public List<int[]> trackChanges = new ArrayList<>();

    public Board() {
        array = new int[4][4];
        setNewNumber();
        setNewNumber();
    }

    public void setNewNumber() {
        int i = new Random().nextInt(4);
        int j = new Random().nextInt(4);
        while (array[i][j] != 0) {
            i = new Random().nextInt(4);
            j = new Random().nextInt(4);
        }
        array[i][j] = 2;

        int[] newNumber = {i,j};
        Log.i("TESTING", Arrays.toString(newNumber));
        newNumbers.add(newNumber);

    }

    private void swapZero(int index, int[] row) {

        int i = index + 1;
        while (i < 4) {
            if (row[i] != 0) {
                row[index] = row[i];
                row[i] = 0;

                break;
            }
            i = i + 1;
        }
    }

    public void swipeLeft() {
        int[] row;
        for (int x = 0; x < 4; x = x + 1) {
            row = array[x];
            for (int y = 0; y < 3; y = y + 1) {
                if (row[y] == 0) {
                    swapZero(y, row);
                    if (row[y] != 0) {
                        hasChanged = true;
                    }
                    else {
                        break;
                    }
                }
                for (int z = y + 1; z < 4; z = z + 1) {
                    if (row[z] != 0) {
                        if (row[y] == row[z]) {
                            row[y] = 2 * row[y];
                            row[z] = 0;
//                          check the ArrayList here
                            int[] differences = new int[2];
                            differences[0] = x;
                            differences[1] = y;
                            trackChanges.add(differences);
//                            Log.d("GETTING", "1");
                            hasChanged = true;
//                            Log.d("TRACK CHANGE SIZE", Integer.toString(trackChanges.size()));
//                            Log.d("TRACK CHANGE COMPONENT", Arrays.toString(trackChanges.get(trackChanges.size() -1)));
                        }
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < trackChanges.size(); i = i + 1) {
            Log.d("COMPONENTS", Arrays.toString(trackChanges.get(i)));
        }
    }

    public void swipeRight() {
        reverseHorizontal();
        swipeLeft();
        reverseHorizontal();
    }

    public void swipeUp() {
        transpose();
        swipeLeft();
        transpose();
    }

    public void swipeDown() {
        transpose();
        swipeRight();
        transpose();
    }



    private void reverseHorizontal() {
        board_reverseHorizontal();
        trackChanges_reverseHorizontal();
    }

    private void board_reverseHorizontal() {
        for (int x = 0; x < 4; x = x + 1) {
            for (int y = 0; y < 2; y = y + 1) {
                int temp = array[x][y];
                array[x][y] = array[x][3-y];
                array[x][3-y] = temp;
            }
        }
    }

    private void trackChanges_reverseHorizontal() {
        int size = trackChanges.size();
        for (int i = 0; i < size; i = i + 1) {
            Log.i("BEFORE REVERSE " + Integer.toString(i), Arrays.toString(trackChanges.get(i)));
        }
        for (int i = 0; i < size; i = i + 1) {
            int[] trackArray = trackChanges.get(i);
            trackArray[1] = 3 - trackArray[1];
            trackChanges.set(i, trackArray);
        }

        for (int i = 0; i < size; i = i + 1) {
            Log.i("AFTER REVERSE " + Integer.toString(i), Arrays.toString(trackChanges.get(i)));
        }
    }

    private void transpose() {
        board_transpose();
        trackChanges_transpose();
    }



    private void board_transpose() {
        int[][] temp = new int[4][4];
        for (int x = 0; x < 4; x = x + 1) {
            for (int y = 0; y < 4; y = y + 1) {
                temp[x][y] = array[y][x];
            }
        }
        array = temp;
    }

    private void trackChanges_transpose() {
        int size = trackChanges.size();
        for (int i = 0; i < size; i = i + 1) {
            Log.i("BEFORE TRANSPOSE " + Integer.toString(i), Arrays.toString(trackChanges.get(i)));

        }
        for (int i = 0; i < size; i = i + 1) {
            int[] temp = new int[2];
            temp[0] = trackChanges.get(i)[1];
            temp[1] = trackChanges.get(i)[0];
            trackChanges.set(i, temp);
        }
        for (int i = 0; i < size; i = i + 1) {
            Log.i("AFTER TRANSPOSE " + Integer.toString(i), Arrays.toString(trackChanges.get(i)));
        }
    }
}

