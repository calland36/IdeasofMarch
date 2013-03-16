package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cedarstreettimes.ideasofmarch.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;


public class IndicatorLayout extends FrameLayout implements AnimationListener {

	static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;

	private Animation mInAnim, mOutAnim;
	private ImageView mArrowImageView;

	private final Animation mRotateAnimation, mResetRotateAnimation;

	public IndicatorLayout(Context context, PullToRefreshBase.Mode mode) {
		super(context);

		mArrowImageView = new ImageView(context);
		final int padding = getResources().getDimensionPixelSize(
				R.dimen.indicator_internal_padding);
		mArrowImageView.setPadding(padding, padding, padding, padding);
		addView(mArrowImageView);

		int inAnimResId, outAnimResId;
		switch (mode) {
		case PULL_UP_TO_REFRESH:
			inAnimResId = R.anim.slide_in_from_bottom;
			outAnimResId = R.anim.slide_out_to_bottom;
			setBackgroundResource(R.drawable.indicator_bg_bottom);// QUITAR
			mArrowImageView.setImageResource(R.drawable.arrow_up);
			break;
		default:
		case PULL_DOWN_TO_REFRESH:
			inAnimResId = R.anim.slide_in_from_top;
			outAnimResId = R.anim.slide_out_to_top;
			setBackgroundResource(R.drawable.indicator_bg_top);// QUITAR
			mArrowImageView.setImageResource(R.drawable.arrow_down);
			break;
		}

		mInAnim = AnimationUtils.loadAnimation(context, inAnimResId);
		mInAnim.setAnimationListener(this);

		mOutAnim = AnimationUtils.loadAnimation(context, outAnimResId);
		mOutAnim.setAnimationListener(this);

		final Interpolator interpolator = new LinearInterpolator();
		mRotateAnimation = new RotateAnimation(0, -180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setInterpolator(interpolator);
		mRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setFillAfter(true);

		mResetRotateAnimation = new RotateAnimation(-180, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mResetRotateAnimation.setInterpolator(interpolator);
		mResetRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		mResetRotateAnimation.setFillAfter(true);

	}

	public final boolean isVisible() {
		Animation currentAnim = getAnimation();
		if (null != currentAnim) {
			return mInAnim == currentAnim;
		}

		return getVisibility() == View.VISIBLE;
	}

	public void hide() {
		startAnimation(mOutAnim);
	}

	public void show() {
		mArrowImageView.clearAnimation();
		startAnimation(mInAnim);
	}

	public void onAnimationEnd(Animation animation) {
		if (animation == mOutAnim) {
			mArrowImageView.clearAnimation();
			setVisibility(View.GONE);
		} else if (animation == mInAnim) {
			setVisibility(View.VISIBLE);
		}

		clearAnimation();
	}

	public void onAnimationRepeat(Animation animation) {
		// NO-OP
	}

	public void onAnimationStart(Animation animation) {
		setVisibility(View.VISIBLE);
	}

	public void releaseToRefresh() {
		mArrowImageView.startAnimation(mRotateAnimation);
	}

	public void pullToRefresh() {
		mArrowImageView.startAnimation(mResetRotateAnimation);
	}

}
