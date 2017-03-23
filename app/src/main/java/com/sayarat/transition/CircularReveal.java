package com.sayarat.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

/**
 * Created by sotra on 8/26/2016.
 */
public class CircularReveal extends Visibility {
    public CircularReveal() {
    }

    public CircularReveal(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, final View view, TransitionValues startValues, TransitionValues endValues) {
        float radius = calculateMaxRadius(view);
        final float originalAlpha = view.getAlpha();
        view.setAlpha(0f);
        Animator reveal = createAnimator(view, 0, radius);
        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setAlpha(originalAlpha);
            }
        });
        return reveal;

    }


    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues,
                                TransitionValues endValues) {
        float radius = calculateMaxRadius(view);
        return createAnimator(view, radius, 0);
    }

    private Animator createAnimator(View view, float startRadius, float endRadius) {
        int centerX = view.getWidth() / 2;
        int centerY = view.getHeight() / 2;
        Animator reveal = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, startRadius, endRadius);
        return new AnimUtils.NoPauseAnimator(reveal);
    }
    static float calculateMaxRadius(View view) {
        float widthSquared = view.getWidth() * view.getWidth();
        float heightSquared = view.getHeight() * view.getHeight();
        float radius = (float )Math.sqrt(widthSquared + heightSquared) / 2;
        return radius;
    }

}
