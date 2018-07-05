package com.gu.devel.sinaweibo.userinfo.mvp.view.widget.layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.gu.devel.sinaweibo.userinfo.R;
import com.gu.mvp.utils.dimen.DimenUtils;

/** 整个view的parent，判断是否touch事件是否发生在child上； 解决问题：1.能独立处理滚动和点击的冲突问题 2.解决前台透点问题 */
public class TouchInterceptLayout extends FrameLayout {
  private int mDownMotionY;
  private int mActivePointerId;
  private static final int INVALID_POINTER = -1;
  private static final String TAG = TouchInterceptLayout.class.getSimpleName();
  private boolean mHeaderPerformClick;
  private int mTouchSlop;
  private ImageView headerImg;
  private ImageView image;
  private OnTouchInFrontImageViewListener mOnTouchInFrontImageViewListener;

  public void setOnTouchInFrontImageViewListener(OnTouchInFrontImageViewListener listener) {
    this.mOnTouchInFrontImageViewListener = listener;
  }

  public void clear() {
    mOnTouchInFrontImageViewListener = null;
    image = null;
    headerImg = null;
  }

  public interface OnTouchInFrontImageViewListener {
    void notifyInterceptOnce(boolean intercept);
  }

  public TouchInterceptLayout(Context context) {
    this(context, null);
  }

  public TouchInterceptLayout(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TouchInterceptLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    headerImg = findViewById(R.id.headerimg);
    image = findViewById(R.id.image);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    switch (ev.getActionMasked()) {
      case MotionEvent.ACTION_DOWN:
        mHeaderPerformClick = isTouchPointInView(headerImg, (int) ev.getX(), (int) ev.getY());
        // 点击事件在front imageView上，通知fragment阻止recyclerView item的透点
        if (isTouchPointInView(image, (int) ev.getX(), (int) ev.getY())
            && mOnTouchInFrontImageViewListener != null) {
          mOnTouchInFrontImageViewListener.notifyInterceptOnce(true);
        }
        mDownMotionY = (int) ev.getY();
        mActivePointerId = ev.getPointerId(0);
        break;

      case MotionEvent.ACTION_MOVE:
        if (!mHeaderPerformClick) break;
        final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
        if (activePointerIndex == INVALID_POINTER) {
          Log.e(TAG, "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
          break;
        }
        if (Math.abs((int) ev.getY() - mDownMotionY) > mTouchSlop) {
          mHeaderPerformClick = false;
        }
        Log.e(TAG, "onTouchEvent: Y=" + ev.getY());
        break;

      case MotionEvent.ACTION_UP:
        if (mHeaderPerformClick) performHeaderImgClick();
        break;
    }
    return super.dispatchTouchEvent(ev);
  }

  // 核心方法，判断在parent的点击事件是否发生在child上
  private boolean isTouchPointInView(View targetView, int xAxis, int yAxis) {
    if (targetView == null) {
      return false;
    }
    int[] location = new int[2];
    targetView.getLocationOnScreen(location);
    int statusBarHeight = DimenUtils.getStatusBarHeight(getContext());
    int left = location[0];
    int top = location[1] - statusBarHeight;
    int right = left + targetView.getMeasuredWidth();
    int bottom = top + targetView.getMeasuredHeight();
    return yAxis >= top && yAxis <= bottom && xAxis >= left && xAxis <= right;
  }

  // 由于是demo此处就不暴露接口给fragment，实际项目中应该提供自定义onclick接口给前台处理逻辑
  private void performHeaderImgClick() {
    Toast.makeText(getContext().getApplicationContext(), "点击头像了！", Toast.LENGTH_SHORT).show();
  }
}
