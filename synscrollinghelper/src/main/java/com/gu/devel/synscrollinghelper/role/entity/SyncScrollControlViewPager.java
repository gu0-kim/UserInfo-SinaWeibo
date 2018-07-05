package com.gu.devel.synscrollinghelper.role.entity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gu.devel.synscrollinghelper.role.IChildView;
import com.gu.devel.synscrollinghelper.role.IParentView;

/**
 * @author developergu
 * @version v1.0.0
 * @since 2017/10/25
 */
public class SyncScrollControlViewPager<T extends FragmentStatePagerAdapter> extends ViewPager
    implements IParentView {

  private UIScrollCallBack mCallBack;

  public void setCallBack(UIScrollCallBack callback) {
    this.mCallBack = callback;
  }

  private boolean canHorizontalScroll;

  private T mAdapter;

  public void setAdapter(@Nullable T adapter) {
    super.setAdapter(adapter);
    this.mAdapter = adapter;
  }

  public void clear() {
    this.mAdapter = null;
    this.mCallBack = null;
  }

  public SyncScrollControlViewPager(Context context) {
    super(context);
  }

  public SyncScrollControlViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    return canHorizontalScroll && super.onTouchEvent(ev);
  }

  public void canHorizontalScroll(boolean can) {
    this.canHorizontalScroll = can;
  }

  // ------------- IParentView ------------- //

  @Override
  public int getCurrentIndex() {
    return getCurrentItem();
  }

  @Override
  public void onChildScrollChanged(IChildView childView, int scrollY, int deltaY) {
    if (deltaY > 0) {
      // 手向上滑动
      moveUpBy(deltaY);
    } else {
      // 手向下滑动
      if (needMoveDown(scrollY)) {
        moveDownTo(scrollY);
      }
    }
  }

  @Override
  public void dispatchScroll2BackgroundChildren(int deltaY) {
    // Set scrollY for the active fragments
    for (int i = 0; i < getChildCount(); i++) {
      // Skip current item
      if (i == getCurrentIndex()) {
        continue;
      }

      // Skip destroyed or not created item
      IChildView f = (IChildView) mAdapter.getItem(i);
      if (f == null) {
        continue;
      }

      View view = f.getView();
      if (view == null) {
        continue;
      }
      f.syncMove(deltaY);
    }
  }

  public void moveUpBy(int deltaY) {
    if (mCallBack != null) mCallBack.onMoveUpBy(deltaY);
  }

  public void moveDownTo(int scrollY) {
    if (mCallBack != null) mCallBack.onMoveDownTo(scrollY);
  }

  public boolean needMoveDown(int scrollY) {
    return mCallBack != null && mCallBack.canMoveDown(scrollY);
  }

  public interface UIScrollCallBack {
    void onMoveUpBy(int deltaY);

    void onMoveDownTo(int scrollY);

    boolean canMoveDown(int scrollY);
  }
}
