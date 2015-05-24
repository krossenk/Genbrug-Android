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
    private boolean mDisableScrolling = true;

    public InterceptScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Enables or disables ScrollView scroll */
    public void setScrollEnabled (boolean status) {
        mDisableScrolling=status;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mDisableScrolling)
            return false;

        return super.onInterceptTouchEvent(ev);
    }
}
