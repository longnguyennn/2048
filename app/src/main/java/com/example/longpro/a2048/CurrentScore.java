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
import android.widget.RelativeLayout;

/**
 * Created by longpro on 7/22/17.
 */

public class CurrentScore extends android.support.v7.widget.AppCompatTextView {
    // colors
    private final int bgColor = Color.argb(200, 196, 174, 166);
    private final int textColor = Color.argb(255, 216, 204, 199);
    private final int scoreColor = Color.argb(255, 247, 244, 244);

    private int currentScore;
    private SpannableStringBuilder string;
    private final int textSize = 18;
    private final int initScoreSize = 30;
    private int scoreSize;
    private Context context;
    // params for calculating layout.params
    private final int layoutWidth;
    private final int layoutHeight;
    private final int layoutMargin;
    private final int leftMargin;
    private final int topMargin = 0;
    private final int rightMargin;
    private final int btmMargin;
    private final Logo logo;
    public int width;
    public int height;
    public RelativeLayout.LayoutParams params;


    public CurrentScore(Context context, int layoutWidth, int layoutHeight, int layoutMargin, Logo logo) {
        super(context);
        this.context = context;
        this.layoutWidth = layoutWidth;
        this.layoutHeight = layoutHeight;
        this.layoutMargin = layoutMargin;
        this.logo = logo;
        currentScore = 0;
        // set params
        this.leftMargin = this.layoutMargin;
        this.rightMargin = this.layoutMargin;
        this.btmMargin = this.layoutMargin / 2;
        this.setParams();
        // set string = "SCORE\ncurrentScore"
        string = new SpannableStringBuilder("SCORE\n" +
                Integer.toString(this.currentScore));
        // Apply style for "Text"
        // convert dp to px
        int textPxValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                textSize, this.context.getResources().getDisplayMetrics());
        string.setSpan(new AbsoluteSizeSpan(textPxValue), 0, 5, 0);
        // set text color to ..
        string.setSpan(new ForegroundColorSpan(this.textColor), 0, 5, 0);
        // Apply style for "currentScore"
        this.scoreSize = this.initScoreSize;
        // convert dp to px
        int scorePxValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                this.scoreSize, context.getResources().getDisplayMetrics());
        string.setSpan(new AbsoluteSizeSpan(scorePxValue), "SCORE".length() + 1,
                string.length(), 0);
        // set score color to white
        string.setSpan(new ForegroundColorSpan(this.scoreColor), "SCORE".length() + 1,
                string.length(), 0);
        this.setText(string);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
        this.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        // set background
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setCornerRadius(5);
        background.setColor(this.bgColor);
        this.setBackground(background);
        // set id
        this.setId(generateViewId());
    }

    // FIXME: 7/22/17 need to be fixed
    public void setNewScore(int newScore) {
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
//            if (this.currentScore > 99999) { scoreSize = 20; }
            if (this.currentScore > 9999) { scoreSize = 24; }
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

    private void setParams() {
        height = this.layoutHeight * 11 / 20;
        width = (this.layoutWidth - 2 * this.layoutMargin - logo.dimension) / 2;
        this.params = new RelativeLayout.LayoutParams(width, height);
        this.params.addRule(RelativeLayout.RIGHT_OF, logo.getId());
        this.params.setMargins(this.leftMargin, this.topMargin, this.rightMargin, this.btmMargin);
    }
}
