package com.truonglam.tl_hotel.ui.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class IconTextView extends TextView {
    public IconTextView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createView();
    }

    public IconTextView(Context context) {
        super(context);
        createView();
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createView();
    }

    private void createView() {
        if(!isInEditMode()){
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/FontAwesome.ttf");
            setTypeface(tf);
        }

    }
}
