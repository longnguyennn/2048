package com.example.longpro.a2048;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;

/**
 * Created by longpro on 7/21/17.
 */

public class Logo extends android.support.v7.widget.AppCompatTextView {
    // colors
    private final int bgColor = Color.argb(150, 219, 219, 15);
    private final int textColor = Color.argb(255, 247, 244, 244);

    private final String text = "2048";

    public RelativeLayout.LayoutParams params;
    public int dimension;
    private final int layoutWidth;
    private final int layoutHeight;

    public Logo(Context context, int layoutWidth, int layoutHeight) {
        super(context);
        // set logo's params
        this.layoutWidth = layoutWidth;
        this.layoutHeight = layoutHeight;
        this.setParams();
        // set Logo text
        this.setText(this.text);
        this.setGravity(Gravity.CENTER);
        this.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
        this.setTextColor(this.textColor);
        // set Logo background
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setCornerRadius(5);
        background.setColor(this.bgColor);
        this.setBackground(background);
        // set id
        this.setId(generateViewId());
    }

    private void setParams() {;
        dimension = this.layoutHeight * 6 / 7;
        this.params = new RelativeLayout.LayoutParams(dimension, dimension);
    }
}
