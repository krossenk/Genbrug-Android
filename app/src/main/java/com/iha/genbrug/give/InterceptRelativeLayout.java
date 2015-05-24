package com.iha.genbrug.give;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Gladiator HelmetFace on 5/24/2015.
 */
public class InterceptRelativeLayout extends RelativeLayout {


    public InterceptRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setEnableScroll(boolean status) {
        ((InterceptScrollView)getParent()).setScrollEnabled(status);
    }
}
