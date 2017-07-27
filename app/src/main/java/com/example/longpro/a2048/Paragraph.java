package com.example.longpro.a2048;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;

/**
 * Created by longpro on 7/22/17.
 */

public class Paragraph extends android.support.v7.widget.AppCompatTextView {
    // colors
    private int textColor = 0;

    public RelativeLayout.LayoutParams params;
    private Logo logo;

    public Paragraph(Context context, Logo logo) {
        super(context);
        this.logo = logo;
        // set paragraph's params
        setParams();
        // set text and style
        this.setText("Join numbers and get to the 2048 tile!");
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        // FIXME: 7/27/17
        if (this.textColor != 0) { this.setTextColor(this.textColor); }
    }

    // need further tests
    private void setParams() {
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW, this.logo.getId());
    }
}
