package com.example.longpro.a2048;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.RelativeLayout;

/**
 * Created by longpro on 7/26/17.
 */

// currently nothing goes in here
public class HighScore extends android.support.v7.widget.AppCompatTextView {
    // colors..
    private final int bgColor = Color.argb(200, 196, 174, 166);


    public RelativeLayout.LayoutParams params;
    public int width;
    public int height;
    private final int leftMargin;
    private final int topMargin;
    private final int rightMargin;
    public final int btmMargin;
    // set highscore here
    private int highScore;
    private CurrentScore currentScore;


    public HighScore(Context context, CurrentScore currentScore, int layoutMargin) {
        super(context);
        this.currentScore = currentScore;
        this.width = this.currentScore.width;
        this.height = this.currentScore.height;
        this.leftMargin = 0;
        this.topMargin = 0;
        this.rightMargin = 0;
        this.btmMargin = layoutMargin / 2;
        this.setParams();
        // set background - fix later
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setCornerRadius(5);
        background.setColor(bgColor);
        this.setBackground(background);
        // set id
        this.setId(generateViewId());
    }

    private void setParams() {
        // set highScore's dimension equals to currentScore's dimension
        params = new RelativeLayout.LayoutParams(this.width, this.height);
        // set margin
        params.setMargins(leftMargin, topMargin, rightMargin, btmMargin);
        params.addRule(RelativeLayout.RIGHT_OF, this.currentScore.getId());
    }
}
