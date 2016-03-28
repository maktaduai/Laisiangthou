package com.zogamonline.laisiangthou.listeners;

/**
 * Creator Maktaduai on 31-08-2015.
 */

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.zogamonline.laisiangthou.R;

public class ActivitySwipeDetector implements View.OnTouchListener {

    private final GestureDetector gestureDetector;

    public ActivitySwipeDetector(Context context) {
        gestureDetector = new GestureDetector(context, new OnSwipeGestureListener(context));
        gestureDetector.setIsLongpressEnabled(false);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @SuppressWarnings("unused")
    private final class OnSwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

        private final int minSwipeDelta;
        private final int minSwipeVelocity;
        private final int maxSwipeVelocity;

        private OnSwipeGestureListener(Context context) {
            ViewConfiguration configuration = ViewConfiguration.get(context);
            // We think a swipe scrolls a full page.
            //minSwipeDelta = configuration.getScaledTouchSlop();
            minSwipeDelta = configuration.getScaledPagingTouchSlop();
            minSwipeVelocity = configuration.getScaledMinimumFlingVelocity();
            maxSwipeVelocity = configuration.getScaledMaximumFlingVelocity();
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
                               float velocityY) {
            boolean result = false;
            try {
                float deltaX = event2.getX() - event1.getX();
                float deltaY = event2.getY() - event1.getY();
                float absVelocityX = Math.abs(velocityX);
                @SuppressWarnings("UnusedAssignment") float absVelocityY = Math.abs(velocityY);
                float absDeltaX = Math.abs(deltaX);
                float absDeltaY = Math.abs(deltaY);
                if (absDeltaX > absDeltaY) {
                    if (absDeltaX > minSwipeDelta && absVelocityX > minSwipeVelocity
                            && absVelocityX < maxSwipeVelocity) {
                        if (deltaX < 0) {
                            onSwipeLeft();
                        } else {
                            onSwipeRight();
                        }
                    }
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }


    public void onSwipeLeft() {

    }

    public void onSwipeRight() {
    }
}
