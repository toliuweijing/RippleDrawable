package com.example.weijingliu.rippledrawable;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import static com.example.weijingliu.rippledrawable.RectUtils.*;

public class RippleDrawable extends Drawable implements View.OnTouchListener {

  private View mHost = null;  // host is default to null.

  private Paint mPaint = new Paint();

  private final AnimationImpl mAnimationImpl = new AnimationImpl();

  public RippleDrawable() {
    setAlpha(100);
    setColor(520093696 /* R.color.ripple_material_light */);
  }

  public void attachToView(View view) {
    LayerDrawable layer = new LayerDrawable(
        view.getBackground() != null ?
            new Drawable[] { view.getBackground(), this } :
            new Drawable[] { this });
    view.setBackgroundDrawable(layer);
    view.setOnTouchListener(this);
    setHost(view);
    setVisible(false, false);
  }

  public void setHost(View host) {
    mHost = host;
  }

  public void setColor(int color) {
    mPaint.setColor(color);
  }

  @Override
  public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs)
      throws XmlPullParserException, IOException {
    throw new UnsupportedOperationException("Inflating from xml not supported, do it in code");
  }

  @Override
  public void draw(Canvas canvas) {
    if (isVisible()) {
      canvas.drawCircle(
          getBounds().centerX(),
          getBounds().centerY(),
          RectUtils.getRadius(getBounds()),
          mPaint);
    }
  }

  @Override
  public void setAlpha(int alpha) {
    mPaint.setAlpha(alpha);
  }

  @Override
  public void setColorFilter(ColorFilter cf) {
    // ColorFilter isn't meaningful to ripple effect.
    throw new UnsupportedOperationException("color filter not supported");
  }

  @Override
  public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    switch (event.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
        mAnimationImpl.setCenter((int) event.getX(), (int) event.getY());
        mAnimationImpl.enterPressAnimation();
        break;
      case MotionEvent.ACTION_UP:
        mAnimationImpl.setCenter((int) event.getX(), (int) event.getY());
        mAnimationImpl.enterReleaseAnimation();
        break;
      case MotionEvent.ACTION_MOVE:
        mAnimationImpl.setCenter((int) event.getX(), (int) event.getY());
        break;
    }
    // Always return false here because Drawable doesn't take over but only listen to
    // the touch event.
    return false;
  }

  private class AnimationImpl {
    private ObjectAnimator mAnimator;

    public void setRadius(int radius) {
      setBounds(adjustRadius(getBounds(), radius));
      invalidateSelf();
    }

    public int getRadius() {
      return RectUtils.getRadius(getBounds());
    }

    public void setCenter(int x, int y) {
      setBounds(adjustCenter(getBounds(), x, y));
      invalidateSelf();
    }

    public void enterPressAnimation() {
      assert mHost != null;
      cancel();
      setVisible(true, true);
      mAnimator = ObjectAnimator.ofInt(this, "radius", (int) OtherUtils.toPx(mHost, 16), (int) OtherUtils.toPx(mHost, 32));
      mAnimator.setDuration(200);
      mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
      mAnimator.start();
    }

    public void enterReleaseAnimation() {
      assert mHost != null;
      cancel();
      mAnimator = ObjectAnimator.ofInt(this, "radius", getRadius(), OtherUtils.maxEdge(mHost));
      mAnimator.setDuration(400);
      mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
      mAnimator.addListener(
          new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
              // reset to invisible.
              setVisible(false, true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
          }
      );
      mAnimator.start();
    }

    private void cancel() {
      if (mAnimator != null && mAnimator.isRunning()) {
        mAnimator.cancel();
      }
    }
  }
}
