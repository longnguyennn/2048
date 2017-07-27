package com.example.longpro.a2048;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.RelativeLayout;

/**
 * Created by longpro on 7/27/17.
 */

public class GameMenu extends android.support.v7.widget.AppCompatTextView {
    // colors
    private final int bgColor = Color.argb(150, 219, 219, 15);
    // modify this to change textColor
    private int textColor = 0;

    public RelativeLayout.LayoutParams params;
    private int cScoreId;
    private int logoId;
    private final String text = "MENU";
    private final int textSize = 20;

    public GameMenu(Context context, CurrentScore currentScore, Logo logo) {
        super(context);
        this.cScoreId = currentScore.getId();
        this.logoId = logo.getId();
        this.setParams();
        // for testing purpose
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setCornerRadius(5);
        background.setColor(this.bgColor);
        this.setBackground(background);
        // set Text
        this.setGravity(Gravity.CENTER);
        this.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        this.setText(text);
        this.setTextSize(textSize);
        // FIXME: 7/27/17
        if (this.textColor != 0) { this.setTextColor(this.textColor); }
    }

    private void setParams() {
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_RIGHT, this.cScoreId);
        params.addRule(RelativeLayout.ALIGN_LEFT, this.cScoreId);
        params.addRule(RelativeLayout.BELOW, this.cScoreId);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, this.logoId);
    }
}
