package com.iha.genbrug.give;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Gladiator HelmetFace on 5/16/2015.
 */
public class InterceptScrollView extends ScrollView {

    // SOURCE: http://stackoverflow.com/questions/20590236/scrollview-with-children-view-how-to-intercept-scroll-conditionally

    public InterceptScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

}
