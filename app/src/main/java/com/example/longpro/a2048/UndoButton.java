package com.example.longpro.a2048;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by longpro on 7/26/17.
 */

public class UndoButton extends android.support.v7.widget.AppCompatTextView {
    public RelativeLayout.LayoutParams params;
    private HighScore highScore;
    private ResetButton resetButton;
    private final int dimension;
    private int iconSize = 18;

    public UndoButton(Context context, HighScore highScore, ResetButton resetButton) {
        super(context);
        this.highScore = highScore;
        this.resetButton = resetButton;
        // set dimension equals to resetButton's dimension
        this.dimension = resetButton.getDimension();
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
        // set icon
        this.setText(R.string.fa_undo);
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fontawesome-webfont.ttf");
        this.setTypeface(tf);
        this.setGravity(Gravity.CENTER);
        this.setTextSize(this.iconSize);
    }

    private void setParams() {
        params = new RelativeLayout.LayoutParams(this.dimension, this.dimension);
        params.addRule(RelativeLayout.LEFT_OF, resetButton.getId());
        params.addRule(RelativeLayout.BELOW, highScore.getId());
    }

    public int getDimension() { return this.dimension; }
}
