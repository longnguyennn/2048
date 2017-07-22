package com.example.longpro.a2048;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;

/**
 * Created by longpro on 7/21/17.
 */

public class Logo extends android.support.v7.widget.AppCompatTextView {
    public Logo(Context context) {
        super(context);
        // set Logo text
        int textColor = Color.argb(255, 247, 244, 244);
        this.setText("2048");
        this.setGravity(Gravity.CENTER);
        this.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
        this.setTextColor(textColor);
        // set Logo background
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setCornerRadius(5);
        int bgColor = Color.argb(150, 219, 219, 15);
        background.setColor(bgColor);
        this.setBackground(background);
        // set id
        this.setId(generateViewId());
    }
}
