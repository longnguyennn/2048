package com.example.longpro.a2048;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by longpro on 7/26/17.
 */

public class Header extends RelativeLayout {
    private final int layoutWidth;
    private final int layoutMargin;
    private final int layoutHeight;
    private Logo logo;
    private Paragraph paragraph;
    // set currentScore to public so it can be accessed in the game
    private CurrentScore currentScore;

    public Header(Context context, int width, int height) {
        super(context);
        // initialize header
        this.layoutMargin = width / 25;
        this.layoutWidth = width - 2 * layoutMargin;
        this.layoutHeight = height - 2 * layoutMargin;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.getLayoutParams();
        layoutParams.setMargins(this.layoutMargin, this.layoutMargin, this.layoutMargin, this.layoutMargin);
        // add 2048 Logo
        this.logo = new Logo(context, this.layoutWidth, this.layoutHeight);
        this.addView(this.logo, this.logo.params);
        // add paragraph below logo
        paragraph = new Paragraph(context, this.logo);
        this.addView(this.paragraph, this.paragraph.params);
        // add currentScore
        currentScore = new CurrentScore(context, this.layoutWidth, this.layoutHeight,
                                    this.layoutMargin, this.logo);
        this.addView(currentScore, currentScore.params);
        // add highScore
    }
}
