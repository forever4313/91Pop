package com.u91porn.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author flymegoc
 * @date 2017/12/29
 */

public class FABScrollBehavior extends FloatingActionButton.Behavior {
    private static final String TAG = "ScaleDownShowBehavior";
    private int toolbarHeight;
    private boolean hide;

    public FABScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull final FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (dyConsumed > 5 && child.getScaleY() == 1) {
            hide = true;
            child.animate().scaleY(0).scaleX(0).start();
        } else if (dyConsumed < 0 && child.getScaleY() == 0) {
            hide = false;
            child.animate().scaleY(1).scaleX(1).start();
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

}
