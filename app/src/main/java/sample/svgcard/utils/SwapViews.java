package sample.svgcard.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;

import sample.svgcard.application.AppStartupManager;

public final class SwapViews implements Runnable {
	private boolean mIsFirstView;
	private final static int DURATION = 500;
	View view1;
	View view2;
	float start;
	float end;
	Context context;

	public SwapViews(Context context, boolean isFirstView, View view1, View view2, float start, float end) {
		mIsFirstView = isFirstView;
		this.view1 = view1;
		this.view2 = view2;
		this.start = start;
		this.end = end;
		this.context = context;
	}

	public void run() {
		final float centerX = (view1.getWidth() / 2.0f);
		final float centerY = (view1.getHeight() / 2.0f);
		Log.i("SWAPVIEW_POS", "centerX:"+centerX+"/centerY:"+centerY);
		Flip3dAnimation rotation;
		if (mIsFirstView) {
			view1.setVisibility(View.GONE);
			view2.setVisibility(View.VISIBLE);
			view2.requestFocus();
			rotation = new Flip3dAnimation(-end, start, centerX, centerY);
		} else {
			view2.setVisibility(View.GONE);
			view1.setVisibility(View.VISIBLE);
			view1.requestFocus();
			rotation = new Flip3dAnimation(-end, start, centerX, centerY);
		}
		int duration = DURATION*Math.abs((int) (end-start))/90;
		rotation.setDuration(duration);
		rotation.setFillAfter(true);
		rotation.setZAdjustment(Animation.ZORDER_TOP);
		rotation.setInterpolator(new DecelerateInterpolator());
		if (mIsFirstView) {
			view2.startAnimation(rotation);
		} else {
			view1.startAnimation(rotation);
		}
		rotation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {	
				AppStartupManager.setFlipAllowed(true);				
			}
		});
	}
}