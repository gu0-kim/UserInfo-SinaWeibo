package com.gu.devel.synscrollinghelper.scrollobservable.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.gu.devel.synscrollinghelper.scrollobservable.ScrollCallBack;
import com.gu.devel.synscrollinghelper.scrollobservable.Scrollable;

/** 带有滚动监听的 RecyclerView */
public class ScrollListenRecyclerView extends RecyclerView implements Scrollable {

  private ScrollCallBack mScrollCallBack;
  private InnerScrollListener mInnerScrollListener;

  public void setScrollCallBack(ScrollCallBack callBack) {
    this.mScrollCallBack = callBack;
  }

  /** release scroll listener */
  public void releaseListener() {
    removeOnScrollListener(mInnerScrollListener);
    mInnerScrollListener = null;
    mScrollCallBack = null;
  }

  @Override
  public int getCurrentScrollY() {
    return mInnerScrollListener.getCurrentScrollY();
  }

  public ScrollListenRecyclerView(Context context) {
    this(context, null);
  }

  public ScrollListenRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mInnerScrollListener = new InnerScrollListener();
    addOnScrollListener(mInnerScrollListener);
  }

  class InnerScrollListener extends OnScrollListener {
    private int mCurrentScrollY;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {}

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      mCurrentScrollY += dy;
      if (mScrollCallBack != null) {
        mScrollCallBack.onScroll(mCurrentScrollY, dy);
      }
    }

    public int getCurrentScrollY() {
      return mCurrentScrollY;
    }
  }
}
