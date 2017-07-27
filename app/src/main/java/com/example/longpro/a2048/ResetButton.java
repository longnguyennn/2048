package com.example.longpro.a2048;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by longpro on 7/27/17.
 */

public class ResetButton extends android.support.v7.widget.AppCompatTextView {
    // colors
    private final int bgColor = Color.argb(150, 219, 219, 15);
    // change this...
    private int textColor = 0;

    private HighScore highScore;
    private final int dimension;
    private final int leftMargin;
    private final int topMargin = 0;
    private final int rightMargin;
    private final int btmMargin = 0;
    private int iconSize = 18;
    public RelativeLayout.LayoutParams params;

    public ResetButton(Context context, Logo logo, HighScore highScore) {
        super(context);
        this.highScore = highScore;
        this.dimension = logo.dimension - highScore.height - highScore.btmMargin;
        this.leftMargin = highScore.btmMargin;
        this.rightMargin = (highScore.width - 2 * dimension - leftMargin) / 2;
        this.setParams();
        // set something up here / or move to MainActivity ?
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        // for testing purpose
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setCornerRadius(5);
        background.setColor(this.bgColor);
        this.setBackground(background);
        // set id
        this.setId(View.generateViewId());
        // set icon
        this.setText(R.string.fa_reset);
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                                "fontawesome-webfont.ttf");
        this.setTypeface(tf);
        this.setGravity(Gravity.CENTER);
        this.setTextSize(this.iconSize);

        // FIXME: 7/27/17
        if (this.textColor != 0) { this.setTextColor(this.textColor); }
    }

    private void setParams() {
        params = new RelativeLayout.LayoutParams(this.dimension, this.dimension);
        params.addRule(RelativeLayout.BELOW, this.highScore.getId());
        params.addRule(RelativeLayout.ALIGN_RIGHT, this.highScore.getId());
        // set margin here
        params.setMargins(this.leftMargin, this.topMargin, this.rightMargin,
                                    this.btmMargin);
    }

    public int getDimension() { return this.dimension; }
}
