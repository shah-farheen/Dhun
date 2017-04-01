package com.bits.farheen.dhun.helperviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.bits.farheen.dhun.R;

/**
 * Created by farheen on 12/6/16
 */

public class CustomView extends View{

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);

        try {
            boolean showText = typedArray.getBoolean(R.styleable.CustomView_showText, false);
        } finally {
            typedArray.recycle();
        }
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
