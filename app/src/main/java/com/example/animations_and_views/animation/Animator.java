package com.example.animations_and_views.animation;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;

import androidx.annotation.Nullable;

import java.util.Random;

/**
 * найпростіший наприклад, спочатку перший аніматор,
 * потім другий, далі третій. Використати прнаймі
 * два методи для анімації, наприклад розмір та колір
 * об'єкту.
 */
public class Animator extends View {

    private float centerX;
    private float centerY;
    private float currSize;
    private float size;

    private final Paint mPaint = new Paint();
    private static final int ANIMATION_DURATION = 5000;
    private final AnimatorSet mAnimatorSet = new AnimatorSet();

    public Animator(Context context) {
        this(context, null);
    }

    public Animator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(centerX - size, centerY + size, centerX + size, centerY - size, mPaint);
    }

    public void setSize(float size) {
        this.size = size;
        mPaint.setColor(Color.RED + (int) size );
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        currSize = (float) (Math.random() * (getWidth() / 2 - getWidth() / 10) + getWidth() / 10);

        ObjectAnimator growAnimator = ObjectAnimator.ofFloat(this, "size",
                0, currSize);
        growAnimator.setDuration(ANIMATION_DURATION);
        growAnimator.setInterpolator(new BounceInterpolator());

        ObjectAnimator shrink = ObjectAnimator.ofFloat(this, "size", currSize, 0);
        shrink.setDuration(ANIMATION_DURATION);
        shrink.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator repeat = ObjectAnimator.ofFloat(this, "size", 0, currSize);
        repeat.setDuration(ANIMATION_DURATION);
        repeat.setRepeatCount(2);
        repeat.setRepeatMode(ValueAnimator.REVERSE);

        mAnimatorSet.play(growAnimator).before(shrink);
        mAnimatorSet.play(repeat).after(shrink);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            centerX = event.getX();
            centerY = event.getY();

            if (mAnimatorSet.isRunning()) {
                mAnimatorSet.cancel();
            }
            mAnimatorSet.start();
        }
        return super.onTouchEvent(event);
    }
}
