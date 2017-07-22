package com.example.longpro.a2048;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
/**
 * Created by longpro on 7/8/17.
 */

public class Tile extends android.support.v7.widget.AppCompatTextView {
    private int value;
    public int position;

    private final int[][] TILE_PROPERTY = {
            {44, Color.argb(255, 216, 204, 199), Color.argb(205, 45, 42, 42)},
            {44, Color.argb(225, 239, 232, 232), Color.argb(205, 45, 42, 42)},
            {44, Color.argb(200, 252, 246, 224), Color.argb(205, 45, 42, 42)},
            {44, Color.argb(170, 239, 144, 67), Color.argb(255, 247, 244, 244)},
            {42, Color.argb(180, 239, 100, 7), Color.argb(255, 247, 244, 244)},
            {42, Color.argb(140, 234, 79, 44), Color.argb(255, 247, 244, 244)},
            {42, Color.argb(255, 234, 79, 44), Color.argb(255, 247, 244, 244)},
            {35, Color.argb(120, 255, 255, 68), Color.argb(255, 247, 244, 244)},
            {35, Color.argb(180, 237, 237, 40), Color.argb(255, 247, 244, 244)},
            {35, Color.argb(130, 232, 232, 2), Color.argb(255, 247, 244, 244)},
            {28, Color.argb(130, 229, 229, 33), Color.argb(255, 247, 244, 244)},
            {28, Color.argb(150, 219, 219, 15), Color.argb(255, 247, 244, 244)}
    };


    public Tile(Context context, int position, int value) {
        super(context);
        this.value = value;
        this.position = position;
        this.setGravity(Gravity.CENTER);
        this.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        this.createBackground();
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int input) {
        this.value = input;
        GradientDrawable background = (GradientDrawable) this.getBackground();
        if (this.value == 0) {
            this.setText("");
            background.setColor(TILE_PROPERTY[0][1]);
        }
        else {
            int index = (int) (Math.log(this.value) / Math.log(2));;
            this.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) TILE_PROPERTY[index][0]);
            this.setText(Integer.toString(this.value));
            this.setTextColor(TILE_PROPERTY[index][2]);
            background.setColor(TILE_PROPERTY[index][1]);
        }
    }

    private void createBackground() {
        GradientDrawable bg = new GradientDrawable();
        // set background shape
        bg.setShape(GradientDrawable.RECTANGLE);
        // set background radius
        bg.setCornerRadius(5);
        int index;
        if (this.value == 0) { index = 0; }
        else { index = (int) (Math.log(this.value) / Math.log(2)); }
        // set tile's background color based on its value
        bg.setColor(TILE_PROPERTY[index][1]);
        this.setBackground(bg);
    }
}
