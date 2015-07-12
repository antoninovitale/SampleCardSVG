package sample.svgcard.utils;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class UpDownAnimation extends Animation {
	private final float x;
	private final float y;
	private final float mCenterX;
	private final float mCenterY;
	private Camera mCamera;

	public UpDownAnimation(float x, float y, float centerX,
			float centerY) {
		this.x = x;
		this.y = y;
		mCenterX = centerX;
		mCenterY = centerY;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final float centerX = mCenterX;
		final float centerY = mCenterY;
		final Camera camera = mCamera;
		final Matrix matrix = t.getMatrix();
		camera.save();
		camera.translate(x*interpolatedTime, y*interpolatedTime, 0);
		camera.getMatrix(matrix);
		camera.restore();
		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}

}