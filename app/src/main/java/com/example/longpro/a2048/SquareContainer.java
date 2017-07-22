package com.example.longpro.a2048;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by longpro on 7/21/17.
 */

public class SquareContainer extends RelativeLayout {
    public SquareContainer(Context context) {
        super(context);
    }

    public SquareContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return true;
    }
}
