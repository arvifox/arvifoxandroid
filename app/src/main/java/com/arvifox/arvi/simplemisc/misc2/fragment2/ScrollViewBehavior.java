package com.arvifox.arvi.simplemisc.misc2.fragment2;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.arvifox.arvi.R;

public class ScrollViewBehavior extends CoordinatorLayout.Behavior<ViewGroup> {

    public ScrollViewBehavior(Activity root) {
        rootActivity = root;
    }

    /**
     * Height of child view
     */
    private int height;
    /**
     * Flag for running show animation
     */
    private boolean showAnimationRunning;
    /**
     * Flag for running hide animation
     */
    private boolean hideAnimationRunning;

    private Context rootActivity;

    /**
     * Animation listener
     */
    private Animator.AnimatorListener showAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            showAnimationRunning = true;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            showAnimationRunning = false;
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            // nop
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            // nop
        }
    };
    /**
     * Animation listener
     */
    private Animator.AnimatorListener hideAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            hideAnimationRunning = true;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            hideAnimationRunning = false;
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            // nop
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            // nop
        }
    };

    private boolean isLocked = false;

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, ViewGroup child, int layoutDirection) {
        height = child.getHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull ViewGroup child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull ViewGroup child,
                               @NonNull View target,
                               int dxConsumed,
                               int dyConsumed,
                               int dxUnconsumed,
                               int dyUnconsumed,
                               @ViewCompat.NestedScrollType int type) {

        if (isLocked) {
            return;
        }

        // check if top view exists in layout & add to scroll behavior
        View topView = coordinatorLayout.findViewById(R.id.matToolBar);

        if (dyConsumed > 0) {
            slideDown(child, topView);
        } else if (dyConsumed < 0) {
            slideUp(child, topView);
        }

        // if we can't scroll down then show topview
        if (!target.canScrollVertically(1)) {
            slideUp(child, topView);
        }
    }

    /**
     * Slide up
     *
     * @param child   child view
     * @param topView top view
     */
    public void slideUp(ViewGroup child, View topView) {
        if (showAnimationRunning) {
            return;
        }

        child.clearAnimation();
        child.animate().translationY(0).setDuration(300).setListener(showAnimatorListener);

        if (topView != null) {
            topView.clearAnimation();
            topView.animate().translationY(0).setDuration(300);
        }
    }

    /**
     * Slide down
     *
     * @param child   child view
     * @param topView top view
     */
    public void slideDown(ViewGroup child, View topView) {
        if (hideAnimationRunning) {
            return;
        }

        child.clearAnimation();
        child.animate().translationY(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                rootActivity.getResources().getDimension(R.dimen.bottomNavigationHeight),
                rootActivity.getResources().getDisplayMetrics()))
                .setDuration(300).setListener(hideAnimatorListener);

        if (topView != null) {
            topView.clearAnimation();
            topView.animate().translationY(topView.getHeight() * -1).setDuration(300);
        }
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
}
