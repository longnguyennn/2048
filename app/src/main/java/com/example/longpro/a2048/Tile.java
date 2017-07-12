package com.example.longpro.a2048;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by longpro on 7/8/17.
 */

public class Tile extends android.support.v7.widget.AppCompatTextView {
    private int value;
    private int prevPosition;

    private final int[] TILE_COLOR = {
            0xcdc0b0,
            0xeee4da,
            0xede0c8,
            0xf2b179,
            0xf59563,
            0xf67c5f,
            0xf65e3b,
            0xedcf72,
            0xedcc61,
            0xedc850,
            0xedc53f,
            0xedc22e,
    };

    public Tile(Context context, int initPosition) {
        super(context);
        this.value = 0;
        this.prevPosition = initPosition;
//        setTileBackground();
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int input) {
        this.value = input;
        this.setText(Integer.toString(this.value));
    }

    public int getPrevPosition() { return this.prevPosition; }

    public void setPrevPosition(int newPosition) { this.prevPosition = newPosition; }













    // this might not be needed
    private void setTileBackground() {
        setBackgroundResource(R.drawable.component);
        setText(Integer.toString(this.value));
        if (this.value == 0) setBackgroundColor(this.TILE_COLOR[0]);
        else {
            int index = (int) Math.log(this.value) / (int) Math.log(2);
            setBackgroundColor(this.TILE_COLOR[index]);
        }
    }

}
