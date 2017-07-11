package com.example.longpro.a2048;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by longpro on 6/20/17.
 */

public class GameContainer extends RelativeLayout {


    public GameContainer(Context context) {
        super(context);
    }

    public GameContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GameContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
