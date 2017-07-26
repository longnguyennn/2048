package com.example.longpro.a2048;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by longpro on 7/27/17.
 */

public class ResetButton extends android.support.v7.widget.AppCompatTextView {
    private Logo logo;
    private HighScore highScore;
    private final int dimension;
    private final int leftMargin;
    private final int topMargin = 0;
    private final int rightMargin = 0;
    private final int btmMargin = 0;
    public RelativeLayout.LayoutParams params;

    public ResetButton(Context context, Logo logo, HighScore highScore) {
        super(context);
        this.logo = logo;
        this.highScore = highScore;
        this.dimension = logo.dimension - highScore.height - highScore.btmMargin;
        this.leftMargin = highScore.btmMargin;
        this.setParams();
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
        int bgColor = Color.argb(150, 219, 219, 15);
        background.setColor(bgColor);
        this.setBackground(background);
        // set id
        this.setId(View.generateViewId());
    }

    private void setParams() {
        params = new RelativeLayout.LayoutParams(this.dimension, this.dimension);
        // these addRule are not really necessary
        params.addRule(RelativeLayout.BELOW, this.highScore.getId());
        params.addRule(RelativeLayout.ALIGN_RIGHT, this.highScore.getId());
        params.addRule(RelativeLayout.ALIGN_BOTTOM, this.logo.getId());
        // set margin here
        params.setMargins(this.leftMargin, this.topMargin, this.rightMargin,
                                    this.btmMargin);
    }

    public int getDimension() { return this.dimension; }
}
