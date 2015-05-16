package com.iha.genbrug.give;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Gladiator HelmetFace on 5/16/2015.
 */
public class InterceptScrollView extends ScrollView {

    private enum TouchMode {
        PICK_IMAGE,
        CROP_IMAGE,
        SHOW_IMAGE
    }

    public InterceptScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
