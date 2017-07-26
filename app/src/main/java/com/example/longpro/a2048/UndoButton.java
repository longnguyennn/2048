package com.example.longpro.a2048;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by longpro on 7/26/17.
 */

public class UndoButton extends android.support.v7.widget.AppCompatTextView {
    public UndoButton(Context context, RelativeLayout viewGroup) {
        super(context);
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }
}
