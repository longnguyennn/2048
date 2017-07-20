package com.example.longpro.a2048;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.Gravity;

/**
 * Created by longpro on 7/8/17.
 */

public class Tile extends android.support.v7.widget.AppCompatTextView {
    private int value;
    public int position;

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

    public Tile(Context context) {
        super(context);
        this.value = 0;
        this.position = 0;
        this.setGravity(Gravity.CENTER);
    }

    public Tile(Context context, int position) {
        super(context);
        this.value = 0;
        this.position = position;
        this.setGravity(Gravity.CENTER);
        this.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
    }

    public int getValue() {
        return this.value;
    }

    // FIXME: 7/13/17 This method should be fixed - need to move setBackgroundResource in here
    public void setValue(int input) {
        this.value = input;
        if (this.value == 0) { this.setText(""); }
        else {
            if (input > 1000) {
                this.setTextSize(28);
            }
            else if (input > 100) {
                this.setTextSize(30);
            }
            else if (input > 10) {
                this.setTextSize(34);
            }
            else {
                this.setTextSize(36);
            }
            // LN
            Drawable background = this.getBackground();
//            background.mutate();
//            gradientDrawable.mutate();
//            gradientDrawable.getShape();
//            gradientDrawable.setColor(0xffffff);
            this.setText(Integer.toString(this.value));
        }

    }

}
