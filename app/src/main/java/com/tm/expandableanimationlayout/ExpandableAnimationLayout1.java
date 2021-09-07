package com.tm.expandableanimationlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;


import org.jetbrains.annotations.Nullable;

/**
 * Created by Tian on 2021/9/7.
 * 部分折叠，且渐变
 */

public class ExpandableAnimationLayout1 extends LinearLayout {

    private LayoutInflater inflater;

    private ValueAnimator valueAnimator;
    private ValueAnimator objectAnimator;
    private ObjectAnimator alphaAnimator;
    private AnimatorSet animatorSet;
    private float currentTransitionY = 1;

    private int height;

    private View viewContent;
    private Bitmap bitmap;

    private boolean isAnimating = false;

    private boolean isExpand = true;

    private int shrinkValue = 0;
    private static final float SHRINK_ALPHA_VALUE = 0.2f;

    public ExpandableAnimationLayout1(Context context) {
        super(context);
        init(context, null);
    }

    public ExpandableAnimationLayout1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandableAnimationLayout1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        shrinkValue = DensityUtil.dp2px(context, 42);
        setOrientation(VERTICAL);
        inflater = LayoutInflater.from(context);
        initAnimator();
        initChildHeight();
    }

    private void initAnimator() {
        objectAnimator = new ValueAnimator();
        objectAnimator.setDuration(300);
//        objectAnimator.setProperty(View.TRANSLATION_Y);
//        objectAnimator.setTarget(this);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentTransitionY = (float) animation.getAnimatedValue();
                if (currentTransitionY < 0 && currentTransitionY > -height) {
//                    LogUtils.e("currentTransitionY : " + currentTransitionY);
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
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                ExpandableAnimationLayout.this.setFocusable(false);
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
        this.isExpand = isExpand;
        initChildHeight();
    }

    private void initChildHeight() {
        this.post(new Runnable() {
            @Override
            public void run() {
                height = ExpandableAnimationLayout1.this.getHeight();
                if (isExpand) {
                    currentTransitionY = 0;
                } else {
                    currentTransitionY = -height;
                    requestLayout();
                }
//                LogUtils.e("height : " + height);
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
        objectAnimator.setFloatValues(-height + shrinkValue, 0);
        alphaAnimator.setFloatValues(SHRINK_ALPHA_VALUE, 1f);
        alphaAnimator.setDuration(300);
        animatorSet.start();
    }

    public void setShrink() {
        objectAnimator.setFloatValues(0, -height + shrinkValue);
        alphaAnimator.setFloatValues(1f, SHRINK_ALPHA_VALUE);
        alphaAnimator.setDuration(200);
        animatorSet.start();
    }
}
