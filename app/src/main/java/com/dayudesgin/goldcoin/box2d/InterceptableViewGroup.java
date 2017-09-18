package com.dayudesgin.goldcoin.box2d;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 拦截触摸事件的View
 */
public class InterceptableViewGroup extends RelativeLayout {

    private boolean intercept = false;

    public InterceptableViewGroup(Context context) {
        super(context);
    }

    public InterceptableViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptableViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (intercept)
            return true;
        return super.onInterceptTouchEvent(ev);
    }
    public void setIntercept(boolean intercept) {
        this.intercept = intercept;
    }
}
