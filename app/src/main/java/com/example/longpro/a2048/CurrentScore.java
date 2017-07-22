package com.example.longpro.a2048;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;

/**
 * Created by longpro on 7/22/17.
 */

public class CurrentScore extends android.support.v7.widget.AppCompatTextView {
    private int currentScore;
    private SpannableStringBuilder string;
    private int textSize = 18;
    private int scoreSize;
    private Context context;

    public CurrentScore(Context context) {
        super(context);
        this.context = context;
        currentScore = 0;
        // set string
        string = new SpannableStringBuilder("SCORE\n" +
                Integer.toString(this.currentScore));
        int textPxValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                textSize, this.context.getResources().getDisplayMetrics());
        string.setSpan(new AbsoluteSizeSpan(textPxValue), 0, 5, 0);
        string.setSpan(new ForegroundColorSpan(Color.argb(255, 216, 204, 199)), 0, 5, 0);
        scoreSize = 30;
        int scorePxValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                scoreSize, context.getResources().getDisplayMetrics());
        string.setSpan(new AbsoluteSizeSpan(scorePxValue), "SCORE".length() + 1,
                string.length(), 0);
        string.setSpan(new ForegroundColorSpan(Color.argb(255, 247, 244, 244)), "SCORE".length() + 1,
                string.length(), 0);
        this.setText(string);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
        this.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        // set background
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setCornerRadius(5);
        int bgColor = Color.argb(200, 196, 174, 166);
        background.setColor(bgColor);
        this.setBackground(background);
        // set id
        this.setId(generateViewId());
    }

    // FIXME: 7/22/17 need to be fixed
    public void setNewScore(int newScore) {
        newScore = 10000;
        this.currentScore = newScore;
        // change AbsoluteSizeSpan
        if (this.currentScore > 999)  {
            AbsoluteSizeSpan[] spanList = this.string.getSpans("SCORE".length() + 1, string.length(),
                    AbsoluteSizeSpan.class);
            // remove AbsoluteSizeSpan
            for (AbsoluteSizeSpan i : spanList) {
                this.string.removeSpan(i);
            }
            // set scoreSize for each threshold
            if (this.currentScore > 99999) { scoreSize = 20; }
            else if (this.currentScore > 9999) { scoreSize = 24; }
            else { scoreSize = 26;}
            int scorePxValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    scoreSize, this.context.getResources().getDisplayMetrics());
            // add new AbsoluteSizeSpan
            this.string.setSpan(new AbsoluteSizeSpan(scorePxValue), "SCORE".length() + 1,
                    string.length(), 0);
        }
        string.replace("SCORE".length() + 1, string.length(), Integer.toString(this.currentScore));
        this.setText(string);
    }
}
