package com.iha.genbrug.give;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Gladiator HelmetFace on 5/24/2015.
 */
public class InterceptLinearLayout extends LinearLayout {

    private boolean mNeedsScroll=true;

    public InterceptLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mNeedsScroll)  {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //((InterceptRelativeLayout)getParent()).setEnableScroll(false);
                    break;
                case MotionEvent.ACTION_UP:
                    //((InterceptRelativeLayout)getParent()).setEnableScroll(true);
                    break;
            }
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
