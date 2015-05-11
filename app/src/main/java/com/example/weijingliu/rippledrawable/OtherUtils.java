package com.example.weijingliu.rippledrawable;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;

public final class OtherUtils {
  public static float toPx(Resources res, int dp) {
    float px = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        res.getDisplayMetrics());
    return px;
  }

  public static float toPx(View v, int dp) {
    return toPx(v.getResources(), dp);
  }

  public static int maxEdge(View view) {
    return Math.max(view.getHeight(), view.getWidth());
  }

  private OtherUtils() {}
}
