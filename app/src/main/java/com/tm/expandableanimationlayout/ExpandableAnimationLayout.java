package com.tm.expandableanimationlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

/**
 * Created by Tian on 2017/7/5.
 */

public class ExpandableAnimationLayout extends FrameLayout {

    private LayoutInflater inflater;

    private ValueAnimator valueAnimator;
    private ObjectAnimator objectAnimator;
    private ObjectAnimator alphaAnimator;
    private AnimatorSet animatorSet;
    private float currentTransitionY = 1;

    private int height;

    private View viewContent;
    private Bitmap bitmap;

    private boolean isAnimating = false;

    public ExpandableAnimationLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ExpandableAnimationLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandableAnimationLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflater = LayoutInflater.from(context);

        initAnimator();
    }

    private void initAnimator() {
        objectAnimator = new ObjectAnimator();
        objectAnimator.setDuration(300);
        objectAnimator.setProperty(View.TRANSLATION_Y);
        objectAnimator.setTarget(this);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentTransitionY = (float) animation.getAnimatedValue();
                if (currentTransitionY < 0 && currentTransitionY > -height) {
                    isAnimating = true;
                } else {
                    isAnimating = false;
                }
                requestLayout();
            }
        });

        alphaAnimator = new ObjectAnimator();
        alphaAnimator.setDuration(100);
        alphaAnimator.setProperty(View.ALPHA);
        alphaAnimator.setTarget(this);
        alphaAnimator.setInterpolator(new LinearInterpolator());

        animatorSet = new AnimatorSet();
        animatorSet.setTarget(this);
        animatorSet.playTogether(alphaAnimator, objectAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                ExpandableAnimationLayout.this.setFocusable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ExpandableAnimationLayout.this.setFocusable(true);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (currentTransitionY <= -height) {
            int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
            setMeasuredDimension(widthMeasureSpec, spec);
            return;
        }
        if (isAnimating) {
            int spec = MeasureSpec.makeMeasureSpec((int) (height + currentTransitionY), MeasureSpec.EXACTLY);
            setMeasuredDimension(widthMeasureSpec, spec);
        }
    }

    public void setLayout(@NonNull int layoutId, final boolean isExpand) {
        viewContent = inflater.inflate(layoutId, this);

        this.post(new Runnable() {
            @Override
            public void run() {
                height = ExpandableAnimationLayout.this.getHeight();
                if (isExpand) {
                    currentTransitionY = 0;
                } else {
                    currentTransitionY = -height;
                    requestLayout();
                }
//                viewContent.setDrawingCacheEnabled(true);
//                viewContent.buildDrawingCache();
//                Bitmap drawingCache = viewContent.getDrawingCache();
//                bitmap = drawingCache;
//                bitmap = Bitmap.createBitmap(drawingCache.getWidth(), drawingCache.getHeight(), Bitmap.Config.ARGB_8888);
//                viewContent.draw(new Canvas(bitmap));
//                viewContent.setDrawingCacheEnabled(false);
//                viewContent.destroyDrawingCache();
//
//                ivShot.setImageBitmap(bitmap);
//                addView(ivShot);
//                viewContent.setVisibility(INVISIBLE);
//                ivShot.setScaleY(0);
//                ivShot.setVisibility(VISIBLE);
//                viewContent.setScaleY(0);
            }
        });
    }

    public void setExpand() {
        objectAnimator.setFloatValues(-height, 0);
        alphaAnimator.setFloatValues(0, 1f);
        alphaAnimator.setDuration(300);
        animatorSet.start();
    }

    public void setShrink() {
        objectAnimator.setFloatValues(0, -height);
        alphaAnimator.setFloatValues(1f, 0);
        alphaAnimator.setDuration(200);
        animatorSet.start();
    }
}
