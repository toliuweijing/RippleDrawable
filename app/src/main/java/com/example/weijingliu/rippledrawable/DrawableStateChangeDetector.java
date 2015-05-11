package com.example.weijingliu.rippledrawable;

/**
 * Used to detect drawable state changes.
 */
public class DrawableStateChangeDetector {
  private final int[] mStateIds;
  private final Listener mListener;

  /**
   * @param stateIds a list of state ids of our interests.
   * @param listener the listener to who notification is sent.
   */
  public DrawableStateChangeDetector(int[] stateIds, Listener listener) {
    mStateIds = stateIds;
    mListener = listener;
  }

  interface Listener {
    void onStateChanged(int stateId, boolean value);
  }

  public void compare(int[] former, int[] later) {
    for (int id : mStateIds) {
      if (contains(former, id) != contains(later, id)) {
        mListener.onStateChanged(id, contains(later, id));
      }
    }
  }

  private boolean contains(int[] array, int element) {
    for (int e : array) {
      if (e == element) {
        return true;
      }
    }
    return false;
  }
}
