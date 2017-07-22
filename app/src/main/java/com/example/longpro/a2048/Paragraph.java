package com.example.longpro.a2048;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;

/**
 * Created by longpro on 7/22/17.
 */

public class Paragraph extends android.support.v7.widget.AppCompatTextView {
    public Paragraph(Context context) {
        super(context);
        this.setText("Join numbers and get to the 2048 tile!");
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
    }
}
